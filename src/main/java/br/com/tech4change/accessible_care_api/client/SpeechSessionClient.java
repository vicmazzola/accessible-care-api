package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SpeechSessionResponse;

public interface SpeechSessionClient {

    SpeechSessionResponse createSession();
}