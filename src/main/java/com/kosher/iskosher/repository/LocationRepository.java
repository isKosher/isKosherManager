package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
