package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getGoogleId(),
                user.getName(),
                user.getEmail(),
                user.getIsManager(),
                null
        );
    }

}

