package com.kosher.iskosher.controller;
import com.kosher.iskosher.repository.lookups.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthController(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String idToken) {

        String email = GoogleTokenVerifier.verify(idToken);
        if (email == null) {
            return ResponseEntity.status(401).body("Invalid Google Token");
        }


        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User(email, "ROLE_USER");
            userRepository.save(user);
        }

        String accessToken = tokenProvider.createAccessToken(email, user.getRole());
        String refreshToken = tokenProvider.createRefreshToken(email);


        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }
}
