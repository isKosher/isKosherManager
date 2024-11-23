package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.LocationsBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationsBusinessRepository extends JpaRepository<LocationsBusiness, UUID> {
}