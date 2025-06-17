package com.kosher.iskosher.configuration;

import com.kosher.iskosher.common.enums.StorageProvider;
import com.kosher.iskosher.service.FileStorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean
    @ConditionalOnProperty(name = "storage.google-drive.enabled", havingValue = "true")
    public Map<StorageProvider, FileStorageService> storageServices(
            @Qualifier("googleDriveService") FileStorageService googleDriveService,
            @Qualifier("supabaseService") FileStorageService supabaseService) {

        Map<StorageProvider, FileStorageService> services = new HashMap<>();

        if (googleDriveService != null) {
            services.put(StorageProvider.GOOGLE_DRIVE, googleDriveService);
        }

        if (supabaseService != null) {
            services.put(StorageProvider.SUPABASE, supabaseService);
        }

        return services;
    }
}