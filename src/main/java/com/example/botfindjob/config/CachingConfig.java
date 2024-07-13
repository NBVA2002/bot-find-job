package com.example.botfindjob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class CachingConfig {
  @Autowired
  private RedisConnectionFactory redisConnectionFactory;

  @Bean
  public CacheManager cacheManager() {
    return RedisCacheManager.builder(redisConnectionFactory).build();
  }
}