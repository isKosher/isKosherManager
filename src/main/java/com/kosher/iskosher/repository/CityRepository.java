package com.kosher.iskosher.repository;

import com.kosher.iskosher.dto.CityDto;
import com.kosher.iskosher.entity.City;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

    Optional<City> findByName(String name);
}