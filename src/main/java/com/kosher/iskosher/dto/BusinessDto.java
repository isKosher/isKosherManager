package com.kosher.iskosher.dto;

import com.kosher.iskosher.model.KosherCertificateModel;
import com.kosher.iskosher.model.LocationModel;
import com.kosher.iskosher.model.SupervisorModel;
import com.kosher.iskosher.model.types.BusinessTypeModel;
import com.kosher.iskosher.model.types.FoodTypeModel;
import com.kosher.iskosher.model.types.KosherTypeModel;
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
    private SupervisorModel supervisor;
    private LocationModel location;


    public BusinessDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
