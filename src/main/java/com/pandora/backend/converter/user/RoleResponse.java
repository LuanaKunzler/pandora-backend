package com.pandora.backend.converter.user;

import com.pandora.backend.model.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Integer role_id;
    private RoleType name;
}