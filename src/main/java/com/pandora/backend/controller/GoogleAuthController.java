package com.pandora.backend.controller;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.request.user.GoogleSignInRequest;
import com.pandora.backend.model.request.user.GoogleSignUpRequest;
import com.pandora.backend.service.AuthService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class GoogleAuthController extends PublicApiController {

    private final AuthService authService;

    @Autowired
    public GoogleAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/google-auth/google-authentication")
    public ResponseEntity<?> authenticateUserFromGoogle(@RequestBody @Valid GoogleSignInRequest googleSignInRequest) {
        try {
            return authService.authenticateFromGoogle(googleSignInRequest);
        } catch (InvalidArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
