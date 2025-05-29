package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.BusinessPhotosBusiness;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessPhotosBusinessRepository extends JpaRepository<BusinessPhotosBusiness, UUID> {
    @EntityGraph(attributePaths = "businessPhotos")
    List<BusinessPhotosBusiness> findByBusinessesId(UUID businessId);
}