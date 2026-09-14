package br.com.tech4change.accessible_care_api.service;

import br.com.tech4change.accessible_care_api.client.SpeechClient;
import br.com.tech4change.accessible_care_api.dto.SpeechTranscriptionResponse;
import org.springframework.stereotype.Service;

@Service
public class SpeechService {

    private final SpeechClient speechClient;

    public SpeechService(SpeechClient speechClient) {
        this.speechClient = speechClient;
    }

    public SpeechTranscriptionResponse transcribe() {
        return speechClient.transcribe();
    }
}