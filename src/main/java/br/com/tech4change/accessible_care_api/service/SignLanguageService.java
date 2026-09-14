package br.com.tech4change.accessible_care_api.service;

import br.com.tech4change.accessible_care_api.client.SignLanguageModelClient;
import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import org.springframework.stereotype.Service;

@Service
public class SignLanguageService {

    private final SignLanguageModelClient modelClient;

    public SignLanguageService(SignLanguageModelClient modelClient) {
        this.modelClient = modelClient;
    }

    public SignLanguageResponse predict() {
        return modelClient.predict();
    }
}