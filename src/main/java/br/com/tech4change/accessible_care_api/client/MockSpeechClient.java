package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SpeechTranscriptionResponse;
import org.springframework.stereotype.Component;

@Component
public class MockSpeechClient implements SpeechClient {

    @Override
    public SpeechTranscriptionResponse transcribe() {
        return new SpeechTranscriptionResponse(
                true,
                "Como você está se sentindo hoje?",
                null
        );
    }
}