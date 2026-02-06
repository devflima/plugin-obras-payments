package br.com.pluginobras.payments.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JacksonTestConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

