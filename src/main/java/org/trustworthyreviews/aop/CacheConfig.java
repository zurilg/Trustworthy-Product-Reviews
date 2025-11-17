package org.trustworthyreviews.aop;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for the caffeine cache.
 * Caffeine is a java in-memory caching library which will be used
 * in this program to cache product reviews, as part of the AOP
 * implementations to the program.
 *
 * @version 11-17-2025
 */
@Configuration
@EnableCaching
public class CacheConfig {
    /**
     * Creates a cache builder to be assigned later.
     *
     * @return a Caffeine builder with an hour duration.
     */
    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
    }

    /**
     * Creates a Spring caffeine manager.
     * The cache manager dictates a cache's behaviour using
     * annotations over desired methods.
     *
     * @param caffeine the cache to be managed
     * @return the manager for the given cache.
     */
    @Bean
    public CaffeineCacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
