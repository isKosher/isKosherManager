package com.kosher.iskosher.controller;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kosher.iskosher.configuration.FirebaseConfig;
import com.kosher.iskosher.configuration.jwt.JwtTokenProvider;
import com.kosher.iskosher.repository.lookups.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
   private final FirebaseAuth firebaseAuth;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String idToken) {

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            System.out.println(decodedToken.getName());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }

        String email = idToken;  //GoogleTokenVerifier.verify(idToken);
        if (email == null) {
            return ResponseEntity.status(401).body("Invalid Google Token");
        }





       /* User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User(email, "ROLE_USER");
            userRepository.save(user);
        }*/

        String accessToken = tokenProvider.createAccessToken(email, "test");
        String refreshToken = tokenProvider.createRefreshToken(email);


        return ResponseEntity.ok(List.of(accessToken, refreshToken));
    }
}
