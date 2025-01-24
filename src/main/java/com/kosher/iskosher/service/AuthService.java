package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.response.AuthResponse;
import com.kosher.iskosher.types.LogoutStatus;

public interface AuthService {

    AuthResponse loginWithGoogle(String token);

    LogoutStatus logout(String userId);

}
