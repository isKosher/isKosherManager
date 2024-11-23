package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.FoodItemTypeBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemTypeBusinessRepository extends JpaRepository<FoodItemTypeBusiness, UUID> {
}