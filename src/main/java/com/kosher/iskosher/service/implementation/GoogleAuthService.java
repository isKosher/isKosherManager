package com.kosher.iskosher.service.implementation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleAuthService implements OAuth2Service<FirebaseToken> {

    private final FirebaseAuth firebaseAuth;

    @Override
    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(token);
    }

    @Override
    public <R> R mapToApplicationUser(FirebaseToken userDetails) {
        User user = new User(userDetails.getUid(),
                userDetails.getName(),
                userDetails.getEmail());
        return (R) user;
    }

    @Override
    public String getProviderName() {
        return "Google";
    }
}
