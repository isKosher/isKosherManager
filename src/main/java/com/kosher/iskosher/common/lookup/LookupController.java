package com.kosher.iskosher.common.lookup;

import com.kosher.iskosher.common.interfaces.NamedEntity;
import com.kosher.iskosher.common.interfaces.NamedEntityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class LookupController<T extends NamedEntity, D extends NamedEntityDto> {
    protected final LookupService<T, D> service;

    @GetMapping("/")
    public ResponseEntity<List<D>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<D> getByName(@RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping("/get-or-create")
    public ResponseEntity<D> getOrCreate(@RequestParam D dto) {
        return ResponseEntity.ok(service.getOrCreateDto(dto.name()));
    }
}
