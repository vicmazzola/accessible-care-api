package br.com.tech4change.accessible_care_api.service;

import br.com.tech4change.accessible_care_api.client.SpeechSessionClient;
import br.com.tech4change.accessible_care_api.dto.SpeechSessionResponse;
import org.springframework.stereotype.Service;

@Service
public class SpeechSessionService {

    private final SpeechSessionClient speechSessionClient;

    public SpeechSessionService(SpeechSessionClient speechSessionClient) {
        this.speechSessionClient = speechSessionClient;
    }

    public SpeechSessionResponse createSession() {
        return speechSessionClient.createSession();
    }
}