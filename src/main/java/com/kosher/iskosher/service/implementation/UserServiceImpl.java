package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.UserDto;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.User;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.repository.lookups.UserRepository;
import com.kosher.iskosher.service.UserService;
import com.kosher.iskosher.types.mappers.BusinessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    public List<UserDto> getAllUsersWithBusinesses() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(businessMapper::convertToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserOwnedBusinessResponse> getBusinessDetailsByUserId(UUID userId) {
        List<Business> businesses = businessRepository.findAllBusinessDetailsByUserId(userId);
        return businesses.stream()
                .map(businessMapper::mapToUserOwnedBusinessResponse)
                .collect(Collectors.toList());
    }

}
