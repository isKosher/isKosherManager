package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.UserDto;

import java.util.List;

public interface UserService {

     List<UserDto> getAllUsersWithBusinesses();
}
