package com.pandora.backend.converter.user;

import com.pandora.backend.model.entity.RoleType;
import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.response.user.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserResponseConverter implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setState(user.getState());
        userResponse.setZip(user.getZip());
        userResponse.setCountry(user.getCountry());
        userResponse.setEmailVerified(user.getEmailVerified());
        userResponse.setSocialProvider(user.getSocialProvider());
        userResponse.setSocialId(user.getSocialId());
        userResponse.setRole(user.getRoles());
        return userResponse;
    }

}
