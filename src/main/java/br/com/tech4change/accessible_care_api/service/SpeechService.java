package br.com.tech4change.accessible_care_api.service;

import br.com.tech4change.accessible_care_api.dto.SpeechTranscriptionResponse;
import org.springframework.stereotype.Service;

@Service
public class SpeechService {

    public SpeechTranscriptionResponse transcribe() {
        return new SpeechTranscriptionResponse(
                true,
                "Como você está se sentindo hoje?",
                null
        );
    }
}