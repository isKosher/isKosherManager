package com.kosher.iskosher.types.mappers;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.KosherCertificateDto;
import com.kosher.iskosher.dto.KosherSupervisorDto;
import com.kosher.iskosher.dto.response.UserOwnedBusinessResponse;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.types.LocationInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessMapper {

    public UserOwnedBusinessResponse mapToUserOwnedBusinessResponse(Business business) {
        return UserOwnedBusinessResponse.builder()
                .businessId(business.getId())
                .businessName(business.getName())
                .businessDetails(business.getDetails())
                .businessRating(business.getRating())
                .businessNumber(business.getBusinessNumber())
                .kosherType(business.getKosherType().getName())
                .businessType(business.getBusinessType().getName())
                .location(getLocationInfo(business))
                .supervisors(getSupervisorsDto(business))
                .certificates(getCertificateDto(business))
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
                        pb.getId(),
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
                .map(sb -> KosherSupervisorMapper.INSTANCE.toDTO(sb.getSupervisor()))
                .collect(Collectors.toList());
    }

    private List<KosherCertificateDto> getCertificateDto(Business business) {
        return business.getCertificateVsBusinesses().stream()
                .map(cb-> KosherCertificateMapper.INSTANCE.toDTO(cb.getCertificate()))
                .collect(Collectors.toList());
    }
}
