package com.pandora.backend.service.admin;

import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.admin.user.UserCreationRequest;
import com.pandora.backend.model.request.admin.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface UserAdminService {
    User newUser(UserCreationRequest userCreationRequest);

    Page<User> getUsers(Pageable pageable);

    User getUserById(Long id);

    User updateUser(Long id, UserUpdateRequest userUpdate);

    void deleteUser(Long id);

}
