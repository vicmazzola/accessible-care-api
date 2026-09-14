package br.com.tech4change.accessible_care_api.controller;

import br.com.tech4change.accessible_care_api.dto.SpeechTranscriptionResponse;
import br.com.tech4change.accessible_care_api.service.SpeechService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    private final SpeechService service;

    public SpeechController(SpeechService service) {
        this.service = service;
    }

    @PostMapping("/transcribe")
    public SpeechTranscriptionResponse transcribe() {
        return service.transcribe();
    }
}