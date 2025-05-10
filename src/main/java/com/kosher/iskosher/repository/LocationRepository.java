package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.Business;
import com.kosher.iskosher.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    @Query("""
    SELECT b FROM Business b
    LEFT JOIN FETCH b.location l
    LEFT JOIN FETCH l.city c
    LEFT JOIN FETCH l.address
    LEFT JOIN FETCH c.region
    WHERE b.id = :id
""")
    Optional<Business> findByIdWithLocation(@Param("id") UUID id);

}
