package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SignLanguagePredictionCandidateResponse;
import br.com.tech4change.accessible_care_api.dto.SignLanguagePredictionRequest;
import br.com.tech4change.accessible_care_api.dto.SignLanguageResponse;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.modeldeployment.ModelDeploymentClient;
import com.oracle.bmc.modeldeployment.requests.PredictRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Component
@Profile("oci")
public class OciSignLanguageModelClient implements SignLanguageModelClient {

    private final String region;
    private final String configProfile;
    private final String modelDeploymentId;
    private final JsonMapper jsonMapper;
    private final String modelDeploymentEndpoint;

    public OciSignLanguageModelClient(
            @Value("${oci.region}") String region,
            @Value("${oci.config.profile}") String configProfile,
            @Value("${oci.model-deployment-id}") String modelDeploymentId,
            @Value("${oci.model-deployment-endpoint}") String modelDeploymentEndpoint,
            JsonMapper jsonMapper
    ) {
        this.region = region;
        this.configProfile = configProfile;
        this.modelDeploymentId = modelDeploymentId;
        this.modelDeploymentEndpoint = modelDeploymentEndpoint;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public SignLanguageResponse predict(SignLanguagePredictionRequest request) {
        try {
            var configFile = ConfigFileReader.parseDefault(configProfile);
            var provider = new ConfigFileAuthenticationDetailsProvider(configFile);

            try (var client = ModelDeploymentClient.builder().build(provider)) {

                client.setEndpoint(
                        "https://modeldeployment." + region + ".oci.customer-oci.com"
                );

                var response = client.predict(
                        PredictRequest.builder()
                                .modelDeploymentId(modelDeploymentId)
                                .requestBody(jsonMapper.writeValueAsString(request))
                                .build()
                );

                var prediction = jsonMapper.readValue(
                        response.getValue(),
                        ModelDeploymentPrediction.class
                );

                return new SignLanguageResponse(
                        true,
                        prediction.prediction(),
                        prediction.distance(),
                        prediction.top3(),
                        null
                );
            }
        } catch (Exception ex) {
            return new SignLanguageResponse(
                    false,
                    null,
                    null,
                    List.of(),
                    ex.getMessage()
            );
        }
    }

    private record ModelDeploymentPrediction(
            String prediction,
            double distance,
            List<SignLanguagePredictionCandidateResponse> top3
    ) {
    }
}