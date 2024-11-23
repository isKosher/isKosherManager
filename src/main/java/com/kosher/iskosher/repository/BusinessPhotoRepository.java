package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.BusinessPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessPhotoRepository extends JpaRepository<BusinessPhoto, UUID> {
}