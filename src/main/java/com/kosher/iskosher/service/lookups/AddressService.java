package com.kosher.iskosher.service.lookups;

import com.kosher.iskosher.common.lookup.AbstractLookupService;
import com.kosher.iskosher.dto.AddressDto;
import com.kosher.iskosher.entity.Address;
import com.kosher.iskosher.repository.lookups.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends AbstractLookupService<Address, AddressDto> {
    public AddressService(AddressRepository repository) {
        super(repository, Address.class);
    }

    @Override
    protected Address createEntity(String name) {
        Address newAddress = new Address();
        newAddress.setName(name);
        return newAddress;
    }

    @Override
    protected AddressDto mapToDto(Address entity) {
        return new AddressDto(entity.getId(), entity.getName());
    }
}
