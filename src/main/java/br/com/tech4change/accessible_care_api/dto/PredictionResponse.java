package br.com.tech4change.accessible_care_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionResponse(
        @JsonProperty("class")
        String className,
        double confidence
) {}