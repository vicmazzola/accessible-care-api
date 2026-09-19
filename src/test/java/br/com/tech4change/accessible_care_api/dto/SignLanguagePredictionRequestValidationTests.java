package br.com.tech4change.accessible_care_api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignLanguagePredictionRequestValidationTests {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void acceptsExactlySixtyFourFramesWithOneHundredTwentySixValues() {
        var request = new SignLanguagePredictionRequest(sequence(64, 126));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsInvalidFrameDimensions() {
        var invalidFrameCount =
                new SignLanguagePredictionRequest(sequence(63, 126));

        var invalidLandmarkCount =
                new SignLanguagePredictionRequest(sequence(64, 125));

        assertFalse(validator.validate(invalidFrameCount).isEmpty());
        assertFalse(validator.validate(invalidLandmarkCount).isEmpty());
    }

    @Test
    void serializesTheConfirmedSequenceField() throws Exception {
        var request = new SignLanguagePredictionRequest(sequence(64, 126));
        var json = new ObjectMapper().writeValueAsString(request);

        assertTrue(json.contains("\"sequence\""));
        assertFalse(json.contains("\"frames\""));
    }

    private List<List<Double>> sequence(int frameCount, int landmarkCount) {
        return IntStream.range(0, frameCount)
                .mapToObj(index -> Collections.nCopies(landmarkCount, 0.0))
                .toList();
    }
}
