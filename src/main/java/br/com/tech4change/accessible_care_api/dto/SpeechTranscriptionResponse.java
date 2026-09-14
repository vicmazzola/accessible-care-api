package br.com.tech4change.accessible_care_api.dto;

public record SpeechTranscriptionResponse(
        boolean success,
        String text,
        String error
) {}