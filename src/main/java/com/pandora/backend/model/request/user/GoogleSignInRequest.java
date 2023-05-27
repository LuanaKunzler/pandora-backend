package com.pandora.backend.model.request.user;

import lombok.Data;

@Data
public class GoogleSignInRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String providerId;
    private String provider;
    private String idToken;
}
