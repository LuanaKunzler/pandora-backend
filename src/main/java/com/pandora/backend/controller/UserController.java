package com.pandora.backend.controller;

import com.pandora.backend.model.request.user.PasswordResetRequest;
import com.pandora.backend.model.request.user.UpdateUserAddressRequest;
import com.pandora.backend.model.request.user.UpdateUserRequest;
import com.pandora.backend.model.response.user.UserResponse;
import com.pandora.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize("hasRole('USER')")
public class UserController extends ApiController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/account")
    public ResponseEntity<UserResponse> getUser() {
        UserResponse userResponse = userService.fetchUser();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/account")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(updateUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/account/address")
    public ResponseEntity<UserResponse> updateUserAddress(
            @RequestBody @Valid UpdateUserAddressRequest updateUserAddressRequest) {
        UserResponse userResponse = userService.updateUserAddress(updateUserAddressRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/account/password/reset")
    public ResponseEntity<HttpStatus> passwordReset(@RequestBody @Valid PasswordResetRequest passwordResetRequest) {
        userService.resetPassword(passwordResetRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/account/status")
    public ResponseEntity<Boolean> getVerificationStatus() {
        Boolean status = userService.getVerificationStatus();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
