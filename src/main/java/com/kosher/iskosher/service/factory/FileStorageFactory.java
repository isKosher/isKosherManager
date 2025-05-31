package com.kosher.iskosher.service.factory;

import com.kosher.iskosher.common.enums.StorageProvider;
import com.kosher.iskosher.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileStorageFactory {
    private final Map<StorageProvider, FileStorageService> storageServices;

    public FileStorageService getStorageService(StorageProvider provider) {
        FileStorageService service = storageServices.get(provider);
        if (service == null) {
            throw new UnsupportedOperationException("Storage provider not supported: " + provider);
        }
        if (!service.isAvailable()) {
            throw new IllegalStateException("Storage service is not available: " + provider);
        }
        return service;
    }
}