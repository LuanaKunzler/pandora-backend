package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.admin.user.UserCreationRequest;
import com.pandora.backend.model.request.admin.user.UserUpdateRequest;
import com.pandora.backend.service.TokenService;
import com.pandora.backend.service.admin.UserAdminService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserAdminController extends ApiAdminController {

    private final UserAdminService userAdminService;
    private final TokenService tokenService;

    @Autowired
    public UserAdminController(UserAdminService userAdminService,
                               TokenService tokenService) {
        this.userAdminService = userAdminService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<HttpStatus> newUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        try {
            var response = userAdminService.newUser(userCreationRequest);
            if (response.getEmailVerified() == 0) {
                tokenService.createEmailConfirmToken(response);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/users")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "registrationDate") String sortField) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> usersPage = userAdminService.getUsers(pageable);
            return new ResponseEntity<>(usersPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User response = userAdminService.getUserById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest updateRequest) {
        log.info(updateRequest.toString());
        User updatedUser = userAdminService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        try {
            userAdminService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
