package com.pandora.backend.service.serviceImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class GoogleServiceImpl {
    private static final String CLIENT_ID = "pandora-livros";
    private static final List<String> SCOPES = Collections.singletonList("email");
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final GoogleIdTokenVerifier verifier;
    private final FirebasePublicKeyFetcher publicKeyFetcher;

    public GoogleServiceImpl() {
        // Configurações do verificador de token do Google
        this.verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("https://securetoken.google.com/" + CLIENT_ID)
                .setClock(Clock.SYSTEM)
                .build();

        // Instancia a classe para buscar a chave pública do Firebase
        this.publicKeyFetcher = new FirebasePublicKeyFetcher();
    }



    public FirebaseToken verifyToken(String idTokenString) throws GeneralSecurityException, IOException, FirebaseAuthException {
        // Verifica o token e retorna um FirebaseToken se for válido
        GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, idTokenString);
        if (idToken != null) {
            if (this.verifier.verify(idToken)) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                List<String> authorizedScopes = Arrays.asList(payload.get("scope").toString().split("\\s+"));
                if (payload.getEmailVerified() && SCOPES.containsAll(authorizedScopes)) {
                    // Obtém o FirebaseToken a partir do ID do usuário
                    String uid = payload.getSubject();
                    FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idTokenString);
                    return firebaseToken;
                }
            }
        }
        return null;
    }
}
