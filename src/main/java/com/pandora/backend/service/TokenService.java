package com.pandora.backend.service;

import com.pandora.backend.model.entity.User;
import com.pandora.backend.model.request.user.PasswordForgotValidateRequest;

public interface TokenService {

    void createEmailConfirmToken(User user);

    void createPasswordResetToken(String email);

    void validateEmail(String token);

    void validateForgotPasswordConfirm(String token);

    void validateForgotPassword(PasswordForgotValidateRequest passwordForgotValidateRequest);

}
