package org.example.springcakemanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${cache.name.cakes:cakesCache}")
    private String cakeCacheName;

    /**
     * Bean to handle JSON serialization and deserialization.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


    /**
     * Cache manager using Caffeine implementation.
     */
    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager(cakeCacheName);
    }

}
