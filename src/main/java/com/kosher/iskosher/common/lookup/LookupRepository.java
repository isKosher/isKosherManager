package com.kosher.iskosher.common.lookup;

import com.kosher.iskosher.common.interfaces.NamedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface LookupRepository<T extends NamedEntity> extends JpaRepository<T, UUID> {
    Optional<T> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}

