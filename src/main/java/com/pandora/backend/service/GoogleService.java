package com.pandora.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleService {
    Payload verify(String idTokenString) throws GeneralSecurityException, IOException;
}

