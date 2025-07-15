package com.kosher.iskosher.common.lookup;

import com.kosher.iskosher.common.interfaces.NamedEntity;
import com.kosher.iskosher.common.interfaces.NamedEntityDto;
import com.kosher.iskosher.exception.DatabaseAccessException;
import com.kosher.iskosher.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public abstract class AbstractLookupService<T extends NamedEntity, D extends NamedEntityDto>
        implements LookupService<T, D> {
    protected final LookupRepository<T> repository;
    protected final Class<T> entityClass;

    public D findById(UUID id) {
        try {
            return repository.findById(id)
                    .map(this::mapToDto)
                    .orElseThrow(() -> new EntityNotFoundException(entityClass.getTypeName(), "id", id));
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database while retrieving entity by ID", e);
        }
    }

    public D findByName(String name) {
        try {
            return repository.findByNameIgnoreCase(name)
                    .map(this::mapToDto)
                    .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), "name", name));
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database while retrieving entity by name", e);
        }
    }

    public D getOrCreateDto(String name) {
        try {
            T entity = repository.findByNameIgnoreCase(name)
                    .orElseGet(() -> createNew(name));
            return mapToDto(entity);
        } catch (IllegalArgumentException e) {
            throw new DatabaseAccessException("Error during entity creation due to existing entity", e);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database while creating entity", e);
        }
    }

    @Cacheable(value = "lookupCache", keyGenerator = "lookupKeyGenerator")
    public T getOrCreateEntity(String name) {
        try {
            return repository.findByNameIgnoreCase(name)
                    .orElseGet(() -> createNew(name));
        } catch (IllegalArgumentException e) {
            throw new DatabaseAccessException("Error during entity creation due to existing entity", e);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database while creating entity", e);
        }
    }

    public List<T> getOrCreateEntities(List<String> names) {
        return names.stream()
                .map(this::getOrCreateEntity)
                .collect(Collectors.toList());
    }

    public T getExistingEntity(String name) {
        return repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), "name", name));
    }

    public List<D> findAll() {
        try {
            return repository.findAll().stream()
                    .map(this::mapToDto)
                    .sorted(Comparator.comparing(NamedEntityDto::name))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error accessing the database while retrieving all entities", e);  //
        }
    }
    
    protected abstract T createEntity(String name);

    protected abstract D mapToDto(T entity);

    private T createNew(String name) {
        if (repository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(String.format("%s with the name %s already exists in the system",
                    entityClass.getSimpleName(), name));
        }
        T entity = createEntity(name);
        try {
            return repository.save(entity);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException("Error saving the entity to the database", e);
        }
    }
}
