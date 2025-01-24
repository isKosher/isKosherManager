package com.kosher.iskosher.service.implementation;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kosher.iskosher.configuration.jwt.JwtProvider;
import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.exception.AuthenticationException;
import com.kosher.iskosher.exception.DatabaseAccessException;
import com.kosher.iskosher.repository.lookups.UserRepository;
import com.kosher.iskosher.service.AuthService;
import com.kosher.iskosher.types.LogoutStatus;
import com.kosher.iskosher.types.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


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
    public LogoutStatus logout(String userId) {
        return null;
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
