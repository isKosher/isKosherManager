package com.kosher.iskosher.dto;

import com.kosher.iskosher.types.KosherCertificateModel;
import com.kosher.iskosher.types.LocationDetails;
import com.kosher.iskosher.types.BusinessTypeModel;
import com.kosher.iskosher.types.FoodTypeModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDto {
    private UUID id;
    private String name;
    private String imageUrl;
    private String details;
    private String phone;
    private Integer rating;
    private List<String> kosherLevel;
    private BusinessTypeModel businessType;
    private FoodTypeModel foodType;
 //   private FoodCategoryTypeModel foodCategoryType;
    private KosherCertificateModel kosherCertificate;
    private LocationDetails location;


    public BusinessDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
