package com.kosher.iskosher.model.mappers;

import com.kosher.iskosher.dto.BusinessDto;
import com.kosher.iskosher.entity.*;
import com.kosher.iskosher.model.KosherCertificateModel;
import com.kosher.iskosher.model.LocationModel;
import com.kosher.iskosher.model.SupervisorModel;
import com.kosher.iskosher.model.types.BusinessTypeModel;
import com.kosher.iskosher.model.types.FoodTypeModel;
import com.kosher.iskosher.model.types.KosherTypeModel;

import java.util.List;
import java.util.Set;

public class BusinessMapper {

    public static BusinessDto businessToDto(Business business) {
        return BusinessDto.builder()
                .id(business.getId())
                .phone("")
                .imageUrl(business.getImageUrl())
                .businessType(toBusinessTypeToModel(business.getBusinessType()))
                .name(business.getName())
                .kosherLevel(toKosherTypeModel(business.getKosherType()))
                .foodType(toFoodTypeModel(business.getFoodTypeVsBusinesses()))
                .details(business.getDetails())
                .kosherCertificate(toKosherCertificateModel(business.getKosherCertificate()))
                .location(toLocationModel(business.getLocationsVsBusinesses()))
                .rating(business.getRating().intValue())
                .supervisor(toSupervisorModel(business.getSupervisorsVsBusinesses()))
                .build();
    }

    public static BusinessTypeModel toBusinessTypeToModel(BusinessType businessType) {
        return BusinessTypeModel.builder().name(businessType.getName()).build();
    }

    public static List<String> toKosherTypeModel(KosherType kosherType) {
        return List.of(kosherType.getName());
    }

    public static FoodTypeModel toFoodTypeModel(Set<FoodTypeBusiness> foodTypeBusinesses) {
        FoodTypeModel foodTypeModel = new FoodTypeModel();

        for (FoodTypeBusiness foodTypeBusiness : foodTypeBusinesses) {
            FoodType foodType = foodTypeBusiness.getFoodType();

            switch (foodType.getName()) {
                case "חלבי":
                    foodTypeModel.setDairy(true);
                    break;
                case "בשרי":
                    foodTypeModel.setMeat(true);
                    break;
                case "פרווה":
                    foodTypeModel.setPareve(true);
                    break;
                default:
                    break;
            }
        }

        return foodTypeModel;
    }

    public static KosherCertificateModel toKosherCertificateModel(KosherCertificate kosherCertificate) {
        return KosherCertificateModel.builder()
                .certificate(kosherCertificate.getCertificate())
                .expirationDate(kosherCertificate.getExpirationDate())
                .build();
    }

    public static LocationModel toLocationModel(Set<LocationsBusiness> locationsBusiness) {
        LocationModel locationModel = new LocationModel();

        for (LocationsBusiness lb : locationsBusiness) {
            Location location = lb.getLocation();

            if (location != null) {
                locationModel = LocationModel.builder()
                        .addressName(location.getAddress().getName())
                        .apartmentNumber(location.getStreetNumber().toString())
                        .cityName(location.getCity().getName())
                        .region(location.getCity().getRegion().getName())
                        .additionalNotes(location.getDetails())
                        .build();
            }
        }

        return locationModel;
    }



    public static SupervisorModel toSupervisorModel(Set<SupervisorsBusiness> supervisorsBusiness) {
        SupervisorModel supervisorModel = new SupervisorModel();

        for (SupervisorsBusiness sb : supervisorsBusiness) {
            KosherSupervisor supervisor = sb.getSupervisor();

            if (supervisor != null) {
                supervisorModel = SupervisorModel.builder()
                        .name(supervisor.getName())
                        .phone(supervisorModel.getPhone())
                        .training(supervisorModel.getTraining())
                        .build();
            }
        }

        return supervisorModel;
    }

}
