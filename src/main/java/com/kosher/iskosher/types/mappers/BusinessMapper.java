package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.*;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;
import com.kosher.iskosher.entity.*;
import com.kosher.iskosher.types.LocationInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessMapper {

    public UserDto convertToUserDto(User user) {
        List<BusinessDto> businessDtos = user.getUsersBusinesses().stream()
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
                user.getGoogleId(),
                user.getName(),
                user.getEmail(),
                user.getIsManager(),
                businessDtos
        );
    }

    public UserOwnedBusinessResponse mapToUserOwnedBusinessResponse(Business business) {
        return UserOwnedBusinessResponse.builder()
                .businessId(business.getId())
                .businessName(business.getName())
                .businessDetails(business.getDetails())
                .businessRating(business.getRating())
                .businessNumber(business.getBusinessNumber())
                .kosherType(business.getKosherType().getName())
                .kosherCertificate(business.getKosherCertificate().getCertificate())
                .expirationDate(business.getKosherCertificate().getExpirationDate())
                .businessType(business.getBusinessType().getName())
                .location(getLocationInfo(business))
                .supervisors(getSupervisorsDto(business))
                .foodTypes(business.getFoodTypeVsBusinesses().stream()
                        .map(ft -> ft.getFoodType().getName())
                        .collect(Collectors.toList()))
                .foodItemTypes(business.getFoodItemTypeVsBusinesses().stream()
                        .map(fit -> fit.getFoodItemType().getName())
                        .collect(Collectors.toList()))
                .businessPhotos(getBusinessPhotosDto(business))
                .build();
    }

    private List<BusinessPhotoDto> getBusinessPhotosDto(Business business) {
        return business.getBusinessPhotosVsBusinesses().stream()
                .map(pb -> new BusinessPhotoDto(
                        pb.getBusinessPhotos().getUrl(),
                        pb.getBusinessPhotos().getPhotoInfo()))
                .collect(Collectors.toList());
    }

    private LocationInfo getLocationInfo(Business business) {
        return business.getLocationsVsBusinesses().stream()
                .findFirst()
                .map(lb -> new LocationInfo(
                        lb.getLocation().getAddress().getName(),
                        lb.getLocation().getStreetNumber(),
                        lb.getLocation().getCity().getName()))
                .orElse(null);
    }

    private List<KosherSupervisorDto> getSupervisorsDto(Business business) {
        return business.getSupervisorsVsBusinesses().stream()
                .map(sb -> new KosherSupervisorDto(
                        sb.getSupervisor().getName(),
                        sb.getSupervisor().getContactInfo(),
                        sb.getSupervisor().getAuthority()))
                .collect(Collectors.toList());
    }
}
