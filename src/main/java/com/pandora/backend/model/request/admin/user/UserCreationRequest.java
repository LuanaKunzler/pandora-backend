package com.pandora.backend.model.request.admin.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCreationRequest {

    @NotBlank
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String country;
    private String[] role;
    private Boolean enabled;
    @NotBlank
    private String password;
}
