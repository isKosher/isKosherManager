package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.entity.BusinessPhoto;
import com.kosher.iskosher.repository.BusinessPhotoRepository;
import com.kosher.iskosher.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final BusinessPhotoRepository businessPhotoRepository;
    private final UnsplashImageService imageService;

    @Override
    public List<BusinessPhoto> createBusinessPhotos(List<BusinessPhotoDto> photoDtos, String foodCategory) {
            if (photoDtos == null || photoDtos.isEmpty()) {
                String imageUrl = imageService.fetchImage(foodCategory);
                if (imageUrl != null) {
                    BusinessPhoto defaultPhoto = new BusinessPhoto();
                    defaultPhoto.setUrl(imageUrl);
                    defaultPhoto.setPhotoInfo(foodCategory);
                    return Collections.singletonList(businessPhotoRepository.save(defaultPhoto));
                }
                return Collections.emptyList();
            }

            List<BusinessPhoto> photos = photoDtos.stream()
                    .map(photoDto -> {
                        BusinessPhoto photo = new BusinessPhoto();
                        photo.setUrl(photoDto.url());
                        photo.setPhotoInfo(photoDto.photoInfo());
                        return photo;
                    })
                    .collect(Collectors.toList());
            return businessPhotoRepository.saveAll(photos);

    }
}
