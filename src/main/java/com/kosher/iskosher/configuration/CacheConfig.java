package com.kosher.iskosher.configuration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final String TRAVEL_INFO_CACHE = "travelInfoCache";
    private static final int CACHE_SIZE = 1000;  // Max 1000 entries
    private static final Duration CACHE_TTL = Duration.ofMinutes(15); // Expiration time

    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        cacheManager.createCache(TRAVEL_INFO_CACHE,
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                        String.class, Object.class,
                                        ResourcePoolsBuilder.heap(CACHE_SIZE)
                                )
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(CACHE_TTL))
                                .build()
                )
        );

        return new JCacheCacheManager(cacheManager);
    }
}
