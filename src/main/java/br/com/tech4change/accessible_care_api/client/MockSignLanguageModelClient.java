package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.PredictionResponse;
import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import org.springframework.stereotype.Component;

@Component
public class MockSignLanguageModelClient implements SignLanguageModelClient {

    @Override
    public SignLanguageResponse predict() {
        return new SignLanguageResponse(
                true,
                new PredictionResponse("ansiedade", 0.92),
                null
        );
    }
}