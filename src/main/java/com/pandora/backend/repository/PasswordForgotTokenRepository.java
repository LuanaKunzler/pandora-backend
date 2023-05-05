package com.pandora.backend.repository;

import com.pandora.backend.model.entity.PasswordForgotToken;
import com.pandora.backend.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordForgotTokenRepository extends CrudRepository<PasswordForgotToken, Long> {
    Optional<PasswordForgotToken> findByToken(String token);
    Optional<PasswordForgotToken> findByUser(User user);
}
