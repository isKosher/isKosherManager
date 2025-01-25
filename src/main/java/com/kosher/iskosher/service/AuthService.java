package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse loginWithGoogle(String idToken);

    AuthResponse refreshToken(String refreshToken);

}
