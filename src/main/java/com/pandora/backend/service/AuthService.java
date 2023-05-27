package com.pandora.backend.service;

import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.user.GoogleSignInRequest;
import com.pandora.backend.model.request.user.GoogleSignUpRequest;
import com.pandora.backend.model.request.user.LoginRequest;
import com.pandora.backend.model.request.user.RegisterUserRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> registerFromGoogle(GoogleSignUpRequest googleSignUpRequest);

    ResponseEntity<?> authenticateFromGoogle(GoogleSignInRequest googleSignInRequest);

    User register(RegisterUserRequest registerUserRequest);

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
}
