package com.pandora.backend.model.request.user;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequest {

    @Pattern(regexp = "^[0-9a-zA-ZÀ-ÿ\\s#,\\-]+$")
    @Size(min = 3, max = 26)
    private String firstName;

    @Pattern(regexp = "^[0-9a-zA-ZÀ-ÿ\\s#,\\-]+$")
    @Size(min = 3, max = 26)
    private String lastName;

    @Pattern(regexp = "[0-9]+")
    @Size(min = 11, max = 12)
    private String phone;
}
