package br.com.tech4change.accessible_care_api.client;

import br.com.tech4change.accessible_care_api.dto.SpeechSessionResponse;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.aispeech.AIServiceSpeechClient;
import com.oracle.bmc.aispeech.model.CreateRealtimeSessionTokenDetails;
import com.oracle.bmc.aispeech.requests.CreateRealtimeSessionTokenRequest;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("oci")
public class OciSpeechSessionClient implements SpeechSessionClient {

    private final String compartmentId;
    private final String region;

    public OciSpeechSessionClient(
            @Value("${oci.compartment-id}") String compartmentId,
            @Value("${oci.region}") String region
    ) {
        this.compartmentId = compartmentId;
        this.region = region;
    }

    @Override
    public SpeechSessionResponse createSession() {
        try {
            var configFile = ConfigFileReader.parseDefault();

            var provider =
                    new ConfigFileAuthenticationDetailsProvider(configFile);

            try (var client = AIServiceSpeechClient.builder()
                    .build(provider)) {

                client.setRegion(Region.fromRegionId(region));

                var details = CreateRealtimeSessionTokenDetails.builder()
                        .compartmentId(compartmentId)
                        .build();

                var request = CreateRealtimeSessionTokenRequest.builder()
                        .createRealtimeSessionTokenDetails(details)
                        .build();

                var response = client.createRealtimeSessionToken(request);
                var token = response.getRealtimeSessionToken();

                return new SpeechSessionResponse(
                        true,
                        token.getToken(),
                        token.getSessionId(),
                        region,
                        null
                );
            }

        } catch (Exception ex) {
            return new SpeechSessionResponse(
                    false,
                    null,
                    null,
                    region,
                    ex.getMessage()
            );
        }
    }
}