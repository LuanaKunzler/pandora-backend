package com.pandora.backend.model.request.admin.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pandora.backend.validator.CustomEmail;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 52)
    @CustomEmail
    private String email;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    private String lastName;

    @Pattern(regexp = "[0-9]+")
    @Size(min = 11, max = 12)
    private String phone;

    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @Size(min = 3, max = 240)
    private String address;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 100)
    private String city;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 2, max = 40)
    private String state;

    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 5, max = 8)
    private String zip;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    private String country;

    private Integer emailVerified;

    private Boolean enabled;

    private List<String> roles;

    @JsonProperty("role")
    public List<String> getRoles() {
        return roles;
    }

    @JsonProperty("role")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @JsonProperty("enabled")
    public boolean getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
