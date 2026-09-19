package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SignLanguagePredictionRequest;
import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;

public interface SignLanguageModelClient {

    SignLanguageResponse predict(SignLanguagePredictionRequest request);
}
