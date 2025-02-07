package com.kosher.iskosher.common.lookup;

import com.kosher.iskosher.common.interfaces.NamedEntity;
import com.kosher.iskosher.common.interfaces.NamedEntityDto;

import java.util.List;
import java.util.UUID;

public interface LookupService<T extends NamedEntity, D extends NamedEntityDto> {
    D findById(UUID id);
    D findByName(String name);
    D getOrCreateDto(String name);
    T getOrCreateEntity(String name);
    List<T> getOrCreateEntities(List<String> names);
    T getExistingEntity(String name);
    List<D> findAll();
}