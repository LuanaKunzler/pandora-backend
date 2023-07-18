package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdminRepository extends PagingAndSortingRepository<User, Long> {

    Boolean existsByEmail(String email);

}
