package com.example.botfindjob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class GeminiConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
