package com.pandora.backend.service;

import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.user.*;
import com.pandora.backend.model.response.user.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserResponse fetchUser();

    User getUser();

    User saveUser(User user);

    User findByEmail(String email);

    boolean userExists(String email);

    UserResponse updateUser(UpdateUserRequest updateUserRequest);

    UserResponse updateUserAddress(UpdateUserAddressRequest updateUserAddressRequest);

    void resetPassword(PasswordResetRequest passwordResetRequest);

    Boolean getVerificationStatus();

}
