package com.pandora.backend.model.request.user;

import com.pandora.backend.validator.CustomEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordForgotRequest {
    @NotBlank
    @Size(min = 3, max = 52)
    @CustomEmail
    private String email;
}
