package com.pandora.backend.model.request.user;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserAddressRequest {
    @Pattern(regexp = "^[0-9a-zA-ZÀ-ÿ\\s#,\\-]+$")
    @Size(min = 3, max = 240)
    private String address;

    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$")
    @Size(min = 3, max = 100)
    private String city;

    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$")
    @Size(min = 2, max = 40)
    private String state;

    @Pattern(regexp = "^[0-9-]*$")
    @Size(min = 5, max = 9)
    private String zip;

    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$")
    @Size(min = 3, max = 40)
    private String country;
}
