package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.KosherTypeDto;
import com.kosher.iskosher.entity.KosherType;
import com.kosher.iskosher.repository.lookups.KosherTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class KosherTypeService extends AbstractLookupService<KosherType, KosherTypeDto> {

    public KosherTypeService(KosherTypeRepository repository) {
        super(repository, KosherType.class);
    }

    @Override
    protected KosherType createEntity(String name) {
        KosherType newKosherType = new KosherType();
        newKosherType.setName(name);
        return newKosherType;
    }

    @Override
    protected KosherTypeDto mapToDto(KosherType entity) {
        return new KosherTypeDto(entity.getId(), entity.getName(), entity.getKosherIconUrl());
    }

}
