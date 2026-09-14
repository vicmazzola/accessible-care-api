package br.com.tech4change.accessible_care_api.controller;

import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import br.com.tech4change.accessible_care_api.service.SignLanguageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign-language")
public class SignLanguageController {

    private final SignLanguageService service;

    public SignLanguageController(SignLanguageService service) {
        this.service = service;
    }

    @PostMapping("/predict")
    public SignLanguageResponse predict() {
        return service.predict();
    }
}