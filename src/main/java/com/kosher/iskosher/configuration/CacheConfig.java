package com.kosher.iskosher.configuration;

import com.kosher.iskosher.types.CacheDefinition;
import com.kosher.iskosher.types.TravelInfo;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.default-size}")
    private int defaultCacheSize;

    @Value("${cache.travel-info.ttl-minutes}")
    private int travelInfoTtlMinutes;

    @Value("${cache.lookup.ttl-hours}")
    private int lookupTtlHours;

    @Value("${cache.business-previews.ttl-minutes}")
    private int businessPreviewsTtlMinutes;

    @Value("${cache.business-count.ttl-minutes}")
    private int businessCountTtlMinutes;

    private static final Map<String, CacheDefinition<?, ?>> CACHE_DEFINITIONS = new HashMap<>();


    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        log.info("Initializing CacheManager...");

        // Initialize cache definitions
        initializeCacheDefinitions();

        // Get cache provider and manager
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        log.info("CacheManager initialized with provider: {}", cachingProvider.getClass().getName());

        // Create all defined caches
        CACHE_DEFINITIONS.values().forEach(definition ->
                createCache(cacheManager, definition)
        );

        return new JCacheCacheManager(cacheManager);
    }

    private void initializeCacheDefinitions() {
        CACHE_DEFINITIONS.put("travelInfoCache",
                new CacheDefinition<>("travelInfoCache", String.class, TravelInfo.class,
                        Duration.ofMinutes(travelInfoTtlMinutes), defaultCacheSize));

        CACHE_DEFINITIONS.put("lookupCache",
                new CacheDefinition<>("lookupCache", String.class, Object.class,
                        Duration.ofHours(lookupTtlHours), defaultCacheSize));

        CACHE_DEFINITIONS.put("businessPreviewsCache",
                new CacheDefinition<>("businessPreviewsCache", String.class, Page.class,
                        Duration.ofMinutes(businessPreviewsTtlMinutes), defaultCacheSize));

        CACHE_DEFINITIONS.put("businessCountCache",
                new CacheDefinition<>("businessCountCache", String.class, Long.class,
                        Duration.ofMinutes(businessCountTtlMinutes), 1));
    }

    private <K, V> void createCache(CacheManager cacheManager, CacheDefinition<K, V> definition) {
        log.debug("Creating cache: {}, TTL: {}, Size: {}",
                definition.name(), definition.ttl(), definition.size());

        cacheManager.createCache(definition.name(),
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(
                                        definition.keyType(),
                                        definition.valueType(),
                                        ResourcePoolsBuilder.heap(definition.size())
                                )
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(definition.ttl()))
                                .build()
                )
        );
    }
}