package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SignLanguagePredictionRequest;
import br.com.tech4change.accessible_care_api.dto.SignLanguagePredictionCandidateResponse;
import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("mock")
public class MockSignLanguageModelClient implements SignLanguageModelClient {

    @Override
    public SignLanguageResponse predict(SignLanguagePredictionRequest request) {
        return new SignLanguageResponse(
                true,
                "ansiedade",
                0.0,
                List.of(
                        new SignLanguagePredictionCandidateResponse("ansiedade", 0.0),
                        new SignLanguagePredictionCandidateResponse("tristeza", 1.0),
                        new SignLanguagePredictionCandidateResponse("alegria", 2.0)
                ),
                null
        );
    }
}
