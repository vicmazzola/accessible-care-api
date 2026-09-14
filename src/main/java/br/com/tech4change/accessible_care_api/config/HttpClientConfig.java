package br.com.tech4change.accessible_care_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public RestClient signLanguageRestClient(
            @Value("${model.api.url}") String modelApiUrl
    ) {
        return RestClient.builder()
                .baseUrl(modelApiUrl)
                .build();
    }
}