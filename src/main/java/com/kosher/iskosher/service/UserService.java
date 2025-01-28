package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

     List<UserDto> getAllUsersWithBusinesses();

     List<UserOwnedBusinessResponse> getBusinessDetailsByUserId(UUID userId);
}
