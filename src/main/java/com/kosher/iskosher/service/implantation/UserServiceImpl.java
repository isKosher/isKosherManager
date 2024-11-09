package com.kosher.iskosher.service.implantation;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.repository.UserRepository;
import com.kosher.iskosher.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


        private final UserRepository userRepository;


        public UserServiceImpl(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public List<UserDto> getAllUsersWithBusinesses() {
            List<User> users = userRepository.findAll();
            return users.stream().map(this::convertToUserDto).collect(Collectors.toList());
        }

    private UserDto convertToUserDto(User user) {
        List<BusinessDto> businessDtos = user.getUsersVsBusinesses().stream()
                .map(usersBusiness -> {
                    Business business = usersBusiness.getBusiness();
                    return new BusinessDto(
                            business.getId(),
                            business.getName()
                    );
                })
                .collect(Collectors.toList());

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIsManager(),
                businessDtos
        );
    }

    }
