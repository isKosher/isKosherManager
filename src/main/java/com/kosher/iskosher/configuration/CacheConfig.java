package com.kosher.iskosher.configuration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
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
    //TODO: Use this cache for create business
    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        EhcacheCachingProvider ehcacheProvider = (EhcacheCachingProvider) cachingProvider;

        CacheManager cacheManager = ehcacheProvider.getCacheManager(
                ehcacheProvider.getDefaultURI(),
                ehcacheProvider.getDefaultClassLoader()
        );

        cacheManager.createCache("travelInfoCache",
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                        String.class, Object.class,
                                        ResourcePoolsBuilder.heap(1000)
                                )
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(15)))
                                .build()
                )
        );

        return new JCacheCacheManager(cacheManager);
    }
}
