package br.com.tech4change.accessible_care_api.dto;

public record SpeechSessionResponse(
        boolean success,
        String token,
        String sessionId,
        String region,
        String error
) {}