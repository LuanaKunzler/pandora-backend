package com.pandora.backend.service.serviceImpl;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserResponseConverter userResponseConverter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseConverter = userResponseConverter;
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
