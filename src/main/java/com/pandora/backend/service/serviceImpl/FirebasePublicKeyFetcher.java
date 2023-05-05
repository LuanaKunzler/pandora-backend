package com.pandora.backend.service.serviceImpl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class FirebasePublicKeyFetcher {
    private static final String FIREBASE_CERTS_URL = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";

    private final OkHttpClient httpClient;

    public FirebasePublicKeyFetcher() {
        this.httpClient = new OkHttpClient();
    }

    public PublicKey getPublicKey(String keyId) throws IOException {
        String certs = fetchCerts();

        JSONObject jsonCerts = new JSONObject(certs);

        String publicKeyString = jsonCerts.getString(keyId);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse public key: " + e.getMessage(), e);
        }
    }

    private String fetchCerts() throws IOException {
        Request request = new Request.Builder()
                .url(FIREBASE_CERTS_URL)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new RuntimeException("Failed to fetch Firebase public keys: " + response.message());
            }
        }
    }
}
