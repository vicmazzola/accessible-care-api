package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SpeechSessionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mock")
public class MockSpeechSessionClient implements SpeechSessionClient {

    @Override
    public SpeechSessionResponse createSession() {
        return new SpeechSessionResponse(
                true,
                "mock-jwt-token",
                "mock-session-id",
                "sa-saopaulo-1",
                null
        );
    }
}