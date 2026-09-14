package br.com.tech4change.accessible_care_api.dto;

public record SignLanguageResponse(
        boolean success,
        PredictionResponse prediction,
        String error
) {}