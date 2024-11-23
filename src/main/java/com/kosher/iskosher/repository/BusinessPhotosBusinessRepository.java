package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.BusinessPhotosBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessPhotosBusinessRepository extends JpaRepository<BusinessPhotosBusiness, UUID> {
}