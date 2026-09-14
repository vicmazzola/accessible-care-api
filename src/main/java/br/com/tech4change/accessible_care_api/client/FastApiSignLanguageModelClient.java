package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import org.springframework.web.client.RestClient;

public class FastApiSignLanguageModelClient implements SignLanguageModelClient {

    private final RestClient restClient;

    public FastApiSignLanguageModelClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public SignLanguageResponse predict() {
        return restClient.post()
                .uri("/api/sign-language/predict")
                .retrieve()
                .body(SignLanguageResponse.class);
    }
}