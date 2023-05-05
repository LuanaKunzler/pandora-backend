package com.pandora.backend.model.response.user;

import com.pandora.backend.converter.user.RoleResponse;
import com.pandora.backend.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private Integer emailVerified;
    private String socialProvider;
    private String socialId;
    private Set<Role> role;
    private Boolean enabled;
}
