package com.pandora.backend.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GoogleSignUpRequest {

    @NotBlank(message = "O campo 'email' é obrigatório.")
    private String email;

    @NotBlank(message = "O campo 'firstName' é obrigatório.")
    private String firstName;

    @NotBlank(message = "O campo 'lastName' é obrigatório.")
    private String lastName;

    @NotBlank(message = "O campo 'providerId' é obrigatório.")
    private String providerId;

    @NotBlank(message = "O campo 'provider' é obrigatório.")
    private String provider;

    private String idToken;
}