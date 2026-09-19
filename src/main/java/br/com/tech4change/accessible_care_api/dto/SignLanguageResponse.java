package br.com.tech4change.accessible_care_api.dto;

import java.util.List;

public record SignLanguageResponse(
        boolean success,
        String prediction,
        Double distance,
        List<SignLanguagePredictionCandidateResponse> top3,
        String error
) {}
