package com.pandora.backend.controller;

import com.pandora.backend.model.request.user.*;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.service.AuthService;
import com.pandora.backend.service.TokenService;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PublicUserController extends PublicApiController {
    private final AuthService authService;

    private final TokenService tokenService;

    @Autowired
    public PublicUserController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/account/registration")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        User user = authService.register(registerUserRequest);
        tokenService.createEmailConfirmToken(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping(value = "/account/registration/validate")
    public ResponseEntity<HttpStatus> validateEmail(@RequestBody @Valid ValidateEmailRequest validateEmailRequest) {
        tokenService.validateEmail(validateEmailRequest.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot")
    public ResponseEntity<HttpStatus> forgotPasswordRequest(
            @RequestBody @Valid PasswordForgotRequest passwordForgotRequest) {
        tokenService.createPasswordResetToken(passwordForgotRequest.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot/validate")
    public ResponseEntity<HttpStatus> validateForgotPassword(
            @RequestBody @Valid PasswordForgotValidateRequest passwordForgotValidateRequest) {
        tokenService.validateForgotPassword(passwordForgotValidateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/forgot/confirm")
    public ResponseEntity<HttpStatus> confirmForgotPassword(
            @RequestBody @Valid PasswordForgotConfirmRequest passwordForgotConfirmRequest) {
        tokenService.validateForgotPasswordConfirm(passwordForgotConfirmRequest.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
