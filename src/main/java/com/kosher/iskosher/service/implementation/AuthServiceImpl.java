package com.kosher.iskosher.service.implementation;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kosher.iskosher.configuration.jwt.JwtProvider;
import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.exception.AuthenticationException;
import com.kosher.iskosher.exception.DatabaseAccessException;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.exception.JwtValidationException;
import com.kosher.iskosher.repository.lookups.UserRepository;
import com.kosher.iskosher.service.AuthService;
import com.kosher.iskosher.types.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    /**
     * Authenticates a user with Google and returns an AuthResponse.
     * @param token The Google ID token to verify.
     * @return AuthResponse containing access token, refresh token, and user ID.
     * @throws AuthenticationException if Google authentication fails.
     */
    @Override
    public AuthResponse loginWithGoogle(String token) {
        try {
            FirebaseToken decodedToken = googleAuthService.verifyToken(token);
            User user = googleAuthService.mapToApplicationUser(decodedToken);

            User savedUser = userRepository.findByGoogleId(user.getGoogleId())
                    .orElseGet(() -> createNewUser(user));

            return generateAuthResponse(savedUser);
        } catch (FirebaseAuthException e) {
            throw new AuthenticationException("Failed to authenticate with Google", e);
        }
    }



    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new JwtValidationException("Invalid refresh token");
        }

        UUID userId = jwtProvider.extractUserIdFromRefreshToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId));

        return generateAuthResponse(user);
    }


    private User createNewUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseAccessException("Failed to create new user", e);
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        UserDto userDto = UserMapper.mapToUserDto(user);
        String accessToken = jwtProvider.generateAccessToken(userDto);
        String refreshToken = jwtProvider.generateRefreshToken(userDto);

        return new AuthResponse(accessToken, refreshToken, user.getId());
    }
}
