package com.pandora.backend.service.serviceImpl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.Role;
import com.pandora.backend.model.entity.RoleType;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.user.GoogleSignInRequest;
import com.pandora.backend.model.request.user.GoogleSignUpRequest;
import com.pandora.backend.model.request.user.LoginRequest;
import com.pandora.backend.model.request.user.RegisterUserRequest;
import com.pandora.backend.model.response.user.JwtResponse;
import com.pandora.backend.model.response.user.MessageResponse;
import com.pandora.backend.repository.RoleRepository;
import com.pandora.backend.repository.UserRepository;
import com.pandora.backend.security.jwt.JwtUtils;
import com.pandora.backend.security.service.UserDetailsImpl;
import com.pandora.backend.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> registerFromGoogle(GoogleSignUpRequest googleSignUpRequest) {
        try {
                User user = new User();
                user.setEmailVerified(1);
                user.setEmail(googleSignUpRequest.getEmail());
                user.setFirstName(googleSignUpRequest.getFirstName());
                user.setLastName(googleSignUpRequest.getLastName());
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setSocialProvider(googleSignUpRequest.getProvider());
                user.setSocialId(googleSignUpRequest.getProviderId());
                user.setEnabled(true);

                Set<String> strRoles = new HashSet<>();
                Role role = new Role(RoleType.ROLE_USER);
                strRoles.add(role.getName().toString());

                Set<Role> roles = new HashSet<>();

                strRoles.forEach(currentRole -> {
                    if ("admin".equals(currentRole)) {
                        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    } else {
                        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                });
                user.setRoles(roles);
                var userSaved = userRepository.save(user);
                return ResponseEntity.ok(userSaved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro ao cadastrar usuário com o Google"));
        }
    }

    @Override
    public ResponseEntity<?> authenticateFromGoogle(GoogleSignInRequest googleSignInRequest) {
        try {
            String idToken = googleSignInRequest.getIdToken();
            String email = googleSignInRequest.getEmail();
            String firstName = googleSignInRequest.getFirstName();
            String lastName = googleSignInRequest.getLastName();
            String providerId = googleSignInRequest.getProviderId();
            String provider = googleSignInRequest.getProvider();

            // Valide o token do Firebase
            if (!isValidToken(idToken)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Token inválido"));
            }

            // Verifique se o usuário já existe no banco de dados
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                // Crie uma nova conta de usuário para o usuário do Google
                GoogleSignUpRequest signUpRequest = new GoogleSignUpRequest();
                signUpRequest.setEmail(email);
                signUpRequest.setFirstName(firstName);
                signUpRequest.setLastName(lastName);
                signUpRequest.setProviderId(providerId);
                signUpRequest.setProvider(provider);
                registerFromGoogle(signUpRequest);
            }

            // Autentica o usuário e gere um novo token JWT
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Construa a resposta
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro ao autenticar usuário com o Google"));
        }
    }

    private boolean isValidToken(String idToken) {
        try {
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if (firebaseApps.isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/pandora-livros-firebase-adminsdk-70dbk-cdb5524fc6.json");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.verifyIdToken(idToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public User register(RegisterUserRequest registerUserRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerUserRequest.getEmail()))) {
            throw new InvalidArgumentException("Já existe uma conta com este e-mail");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setEmailVerified(0);
        user.setSocialProvider("LOCAL");
        user.setEnabled(true);

        Set<String> strRoles = new HashSet<>();
        strRoles.add(RoleType.ROLE_USER.name());

        Set<Role> roles = new HashSet<>();

        strRoles.forEach(currentRole -> {
            if (strRoles.contains(currentRole)) {
                Role role = roleRepository.findByName(RoleType.valueOf(currentRole))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(role);
            }
        });
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }
}
