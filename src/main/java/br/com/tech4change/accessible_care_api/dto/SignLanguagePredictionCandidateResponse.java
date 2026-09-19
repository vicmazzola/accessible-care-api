package br.com.tech4change.accessible_care_api.dto;

public record SignLanguagePredictionCandidateResponse(
        String label,
        double distance
) {}
