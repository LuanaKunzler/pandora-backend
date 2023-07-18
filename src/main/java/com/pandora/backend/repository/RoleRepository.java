package com.pandora.backend.repository;

import com.pandora.backend.model.entity.Role;
import com.pandora.backend.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleType name);

    @Query(value = "SELECT r.* FROM role r JOIN user_role ur ON r.role_id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    List<Role> findByUserId(Long userId);
}
