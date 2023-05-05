package com.pandora.backend.service.serviceImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.auth.FirebaseToken;
import com.pandora.backend.converter.user.UserResponseConverter;
import com.pandora.backend.model.entity.Role;
import com.pandora.backend.model.entity.RoleType;
import com.pandora.backend.model.request.user.*;
import com.pandora.backend.model.response.user.JwtResponse;
import com.pandora.backend.model.response.user.MessageResponse;
import com.pandora.backend.repository.RoleRepository;
import com.pandora.backend.repository.UserRepository;
import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.error.exception.ResourceNotFoundException;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.response.user.UserResponse;
import com.pandora.backend.security.jwt.JwtUtils;
import com.pandora.backend.security.service.UserDetailsImpl;
import com.pandora.backend.service.GoogleService;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseConverter userResponseConverter;

    private final GoogleServiceImpl googleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserResponseConverter userResponseConverter,
                           GoogleServiceImpl googleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseConverter = userResponseConverter;
        this.googleService = googleService;
    }

    @Override
    public User register(RegisterUserRequest registerUserRequest) {
        if (userExists(registerUserRequest.getEmail())) {
            throw new InvalidArgumentException("Já existe uma conta com este e-mail");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setEmailVerified(0);
        user.setSocialProvider("LOCAL");
        user.setEnabled(true);

        Set<String> strRoles = new HashSet<>();
        Role role = new Role(RoleType.ROLE_USER);
        strRoles.add(role.getName().toString());

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
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
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User registerFromGoogle(GoogleSignUpRequest googleSignUpRequest) {
        if (userExists(googleSignUpRequest.getEmail())) {
            throw new InvalidArgumentException("Já existe uma conta com este e-mail");
        }

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

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
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
        }
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

    @Override
    public ResponseEntity<?> authenticateFromGoogle(GoogleSignInRequest googleSignInRequest) {
        try {
            String googleToken = googleSignInRequest.getIdToken();
            String email = googleSignInRequest.getEmail();
            String providerId = googleSignInRequest.getProviderId();

            // Valide o token do Google
            FirebaseToken firebaseToken = googleService.verifyToken(googleToken);
            if (firebaseToken == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Token inválido"));
            }

            // Verifique se o usuário já existe no banco de dados
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                // Crie uma nova conta de usuário para o usuário do Google
                GoogleSignUpRequest signUpRequest = new GoogleSignUpRequest();
                signUpRequest.setEmail(email);
                signUpRequest.setProviderId(providerId);
                signUpRequest.setProvider("GOOGLE");
                registerFromGoogle(signUpRequest);
            }

            // Autentique o usuário e gere um token JWT
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


    @Override
    public UserResponse fetchUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(userName)) {
            throw new AccessDeniedException("Acesso inválido");
        }

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return userResponseConverter.apply(user.get());
    }

    @Override
    public User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(userName)) {
            throw new AccessDeniedException("Acesso inválido");
        }

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return user.get();
    }

    @Override
    public User saveUser(User user) {
        if (Objects.isNull(user)) {
            throw new InvalidArgumentException("Null user");
        }

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        if (Objects.isNull(email)) {
            throw new InvalidArgumentException("Null email");
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        User user = getUser();
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setPhone(updateUserRequest.getPhone());

        user = userRepository.save(user);
        return userResponseConverter.apply(user);
    }

    @Override
    public UserResponse updateUserAddress(UpdateUserAddressRequest updateUserAddressRequest) {
        User user = getUser();
        user.setAddress(updateUserAddressRequest.getAddress());
        user.setCity(updateUserAddressRequest.getCity());
        user.setState(updateUserAddressRequest.getState());
        user.setZip(updateUserAddressRequest.getZip());
        user.setCountry(updateUserAddressRequest.getCountry());

        user = userRepository.save(user);
        return userResponseConverter.apply(user);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        User user = getUser();
        if (!passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidArgumentException("Senha inválida");
        }

        if (passwordEncoder.matches(passwordResetRequest.getNewPassword(), user.getPassword())) {
            return;
        }

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Boolean getVerificationStatus() {
        User user = getUser();
        return user.getEmailVerified() == 1;
    }
}
