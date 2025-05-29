package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.dto.BusinessPhotoDto;
import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.BusinessPhoto;
import com.kosher.iskosher.entity.BusinessPhotosBusiness;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.repository.BusinessPhotoRepository;
import com.kosher.iskosher.repository.BusinessPhotosBusinessRepository;
import com.kosher.iskosher.repository.BusinessRepository;
import com.kosher.iskosher.service.PhotoService;
import com.kosher.iskosher.types.mappers.BusinessPhotoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final BusinessPhotosBusinessRepository businessPhotosBusinessRepository;
    private final BusinessPhotoRepository businessPhotoRepository;
    private final BusinessRepository businessRepository;
    private final UnsplashImageService imageService;

    @Override
    public List<BusinessPhoto> getRandomPhotosByBusinessType(List<BusinessPhotoDto> photoDtos, String foodCategory) {
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

    @Override
    public BusinessPhotoDto createBusinessPhoto(UUID businessId, BusinessPhotoDto dto) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", businessId));

        BusinessPhoto photo = new BusinessPhoto();
        photo.setUrl(dto.url());
        photo.setPhotoInfo(dto.photoInfo());
        photo = businessPhotoRepository.save(photo);

        BusinessPhotosBusiness businessPhotosBusiness = new BusinessPhotosBusiness(business, photo);
        businessPhotosBusinessRepository.save(businessPhotosBusiness);

        return BusinessPhotoMapper.INSTANCE.toDTO(photo);
    }

    @Override
    public List<BusinessPhotoDto> getBusinessPhotos(UUID businessId) {
        return businessPhotosBusinessRepository.findByBusinessesId(businessId)
                .stream()
                .map(BusinessPhotosBusiness::getBusinessPhotos)
                .map(BusinessPhotoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BusinessPhotoDto updateBusinessPhoto(BusinessPhotoDto dto) {
        if (dto.id() == null) {
            throw new IllegalArgumentException("Photo ID must be provided for update.");
        }
        BusinessPhoto photo = businessPhotoRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Photo", "id", dto.id()));

        photo.setUrl(dto.url());
        photo.setPhotoInfo(dto.photoInfo());
        BusinessPhoto updated = businessPhotoRepository.save(photo);

        return BusinessPhotoMapper.INSTANCE.toDTO(updated);
    }

    @Override
    @Transactional
    public void deletePhotoFromBusiness(UUID photoId) {
        BusinessPhoto photo = businessPhotoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo", "id", photoId));

        businessPhotoRepository.delete(photo);
    }

}
