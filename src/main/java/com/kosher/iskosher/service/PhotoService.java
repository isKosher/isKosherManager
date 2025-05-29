package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.entity.BusinessPhoto;

import java.util.List;
import java.util.UUID;

public interface PhotoService {

    BusinessPhotoDto createBusinessPhoto(UUID businessId, BusinessPhotoDto dto);
    List<BusinessPhotoDto> getBusinessPhotos(UUID businessId);
    BusinessPhotoDto updateBusinessPhoto(BusinessPhotoDto dto);
    void deletePhotoFromBusiness(UUID photoId);
    List<BusinessPhoto> getRandomPhotosByBusinessType(List<BusinessPhotoDto> photoDtos, String businessType);
}
