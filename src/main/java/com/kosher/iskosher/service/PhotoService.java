package com.kosher.iskosher.service;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.entity.BusinessPhoto;

import java.util.List;

public interface PhotoService {

    List<BusinessPhoto> createBusinessPhotos(List<BusinessPhotoDto> photoDtos, String businessType);
}
