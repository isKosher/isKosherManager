package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.FoodTypeBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodTypeBusinessRepository extends JpaRepository<FoodTypeBusiness, UUID> {
}