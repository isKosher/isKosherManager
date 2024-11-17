package com.kosher.iskosher.repository;

public interface CityRepository extends org.springframework.data.jpa.repository.JpaRepository<com.kosher.iskosher.entity.City, java.util.UUID> ,org.springframework.data.jpa.repository.JpaSpecificationExecutor<com.kosher.iskosher.entity.City> {
}