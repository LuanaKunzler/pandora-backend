package com.pandora.backend.controller;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.request.user.GoogleSignInRequest;
import com.pandora.backend.model.request.user.GoogleSignUpRequest;
import com.pandora.backend.service.TokenService;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GoogleAuthController extends PublicApiController {

    private final UserService userService;

    private final TokenService tokenService;

    @Autowired
    public GoogleAuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/google-auth/google-registration")
    public ResponseEntity<String> registerUserFromGoogle(@RequestBody @Valid GoogleSignUpRequest googleSignUpRequest) {
        try {
            userService.registerFromGoogle(googleSignUpRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/google-auth/google-authentication")
    public ResponseEntity<String> authenticateUserFromGoogle(@RequestBody @Valid GoogleSignInRequest googleSignInRequest) {
        try {
            userService.authenticateFromGoogle(googleSignInRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
