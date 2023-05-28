package com.pandora.backend.model.request.user;

import com.pandora.backend.validator.CustomEmail;
import com.pandora.backend.validator.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@PasswordMatches
public class RegisterUserRequest {

    @NotBlank
    @Size(min = 3, max = 52)
    @CustomEmail
    private String email;

    @Size(min = 6, max = 52)
    private String password;

    @Size(min = 6, max = 52)
    private String passwordRepeat;
}
