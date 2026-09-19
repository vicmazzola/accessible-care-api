# Accessible Care API

Java 21 and Spring Boot backend for the Tech4Change accessible-care MVP.

## Final Architecture

```text
Professional speech -> OCI Speech Live Transcribe -> captions
Deaf user camera -> MediaPipe in the frontend -> 64 x 126 landmark sequence
                  -> Java backend -> OCI Model Deployment
                  -> prediction + DTW distance + top3
```

The final Libras flow does not use FastAPI, WebM uploads, or raw video forwarding.

## Endpoints

### Health

```http
GET /actuator/health
```

### OCI Speech realtime session

```http
POST /api/speech/session
```

No request body. A successful response contains the short-lived OCI session token, session ID, and region:

```json
{
  "success": true,
  "token": "<short-lived-token>",
  "sessionId": "<session-id>",
  "region": "sa-saopaulo-1",
  "error": null
}
```

Do not log or persist the token.

### Libras prediction

```http
POST /api/sign-language/predict
Content-Type: application/json
```

```json
{
  "sequence": [
    [0.0, 0.0],
    [0.0, 0.0]
  ]
}
```

`sequence` must contain exactly 64 frames. Each frame must contain exactly 126 numeric landmarks: indices `0-62` are the left hand and `63-125` are the right hand.

```json
{
  "success": true,
  "prediction": "Aconselhar",
  "distance": 0.0,
  "top3": [
    { "label": "Aconselhar", "distance": 0.0 },
    { "label": "Ajudar", "distance": 5.146977424621582 },
    { "label": "Com medo", "distance": 5.480223178863525 }
  ],
  "error": null
}
```

`distance` is a DTW distance: lower means more similar. It is not a confidence score or percentage.

`POST /api/speech/transcribe` remains a mock-only endpoint; realtime speech uses `/api/speech/session`.

## Environment

| Variable | Required for | Default |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | Profile selection | `mock` |
| `OCI_REGION` | OCI Speech and Model Deployment | `sa-saopaulo-1` |
| `OCI_COMPARTMENT_ID` | OCI Speech session | None |
| `OCI_CONFIG_PROFILE` | OCI Speech API-key profile | `DEFAULT` |
| `OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE` | OCI Model Deployment API-key profile | `DEFAULT` |
| `OCI_MODEL_DEPLOYMENT_ID` | OCI Libras prediction | None |

For the `oci` profile, the OCI SDK reads the API-key configuration from `~/.oci/config`. Keep the config and its private key outside this repository.

## Run locally

Mock profile:

```powershell
$env:SPRING_PROFILES_ACTIVE = "mock"
mvn spring-boot:run
```

OCI profile:

```powershell
$env:SPRING_PROFILES_ACTIVE = "oci"
$env:OCI_REGION = "sa-saopaulo-1"
$env:OCI_COMPARTMENT_ID = "<speech-compartment-ocid>"
$env:OCI_MODEL_DEPLOYMENT_ID = "<model-deployment-ocid>"
$env:OCI_CONFIG_PROFILE = "DEFAULT"
$env:OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE = "SPEAK"
mvn spring-boot:run
```

## Docker

Build:

```powershell
docker build -t accessible-care-api .
```

Run with OCI credentials mounted read-only:

```powershell
docker run --rm -p 8080:8080 `
  -e SPRING_PROFILES_ACTIVE=oci `
  -e OCI_REGION=sa-saopaulo-1 `
  -e OCI_COMPARTMENT_ID=<speech-compartment-ocid> `
  -e OCI_MODEL_DEPLOYMENT_ID=<model-deployment-ocid> `
  -e OCI_CONFIG_PROFILE=DEFAULT `
  -e OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE=SPEAK `
  -v "${env:USERPROFILE}\.oci:/root/.oci:ro" `
  accessible-care-api
```

The mounted OCI config must define both `DEFAULT` and `SPEAK` and use Linux container paths for both keys under `/root/.oci/`. Never copy OCI config files or keys into the image. See [`deploy/OCI_DEPLOYMENT.md`](deploy/OCI_DEPLOYMENT.md) and [`deploy/oci-config.example`](deploy/oci-config.example) for the container-safe layout. Docker checks `GET /actuator/health` every 30 seconds.

## Known Limitation

OCI Speech and real Libras prediction are verified locally. Public deployment still requires OCI credentials to be provisioned as read-only files with Linux paths inside the container.
