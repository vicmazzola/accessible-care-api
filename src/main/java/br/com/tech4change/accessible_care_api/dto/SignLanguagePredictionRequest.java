package br.com.tech4change.accessible_care_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SignLanguagePredictionRequest(
        @NotNull
        @Size(min = 64, max = 64)
        List<@NotNull @Size(min = 126, max = 126) List<@NotNull Double>> sequence
) {}
