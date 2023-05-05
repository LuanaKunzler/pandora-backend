package com.pandora.backend.error.exception;

public class SocialAuthException extends RuntimeException {

    public SocialAuthException(String message) {
        super(message);
    }

    public SocialAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
