package br.com.tech4change.accessible_care_api.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        boolean success,
        String error,
        String path,
        LocalDateTime timestamp
) {}