# S.P.E.A.K.

S.P.E.A.K. is a Tech4Change 2026 assistive-technology MVP for bidirectional accessibility in consultations. It supports Libras-to-text and speech-to-text. It does not diagnose, replace psychologists, or replace professional Libras interpreters.

## 1. Project overview

The project combines a Next.js frontend, Java 21 / Spring Boot backend, MediaPipe landmark extraction, OCI Speech, and OCI Model Deployment. The backend API and runtime contracts documented here are the project source of truth.

## 2. Problem and solution

Communication barriers can limit participation by deaf people in consultations. S.P.E.A.K. demonstrates two complementary MVP flows:

- Libras signs captured in the browser are recognized as text.
- Spoken language is transcribed into captions.

The MVP is assistive technology, not a complete translation or clinical system.

## 3. MVP architecture

```text
Professional speech -> OCI Speech Live Transcribe -> captions
Deaf user camera -> MediaPipe in the frontend -> 64 x 126 landmark sequence
                  -> Java backend -> OCI Model Deployment
                  -> prediction + DTW distance + top3
```

The final Libras flow does not use FastAPI, WebM uploads, or raw-video forwarding.

## 4. Libras -> Text architecture

```text
Person signing -> webcam -> MediaPipe -> hand landmarks -> temporal sequence
-> Java / Spring Boot backend -> OCI Model Deployment -> prediction -> text
```

MediaPipe runs in the browser. The frontend sends landmarks only: exactly 64 frames with exactly 126 numeric values per frame. Values `0-62` represent the left hand and `63-125` the right hand.

## 5. OCI Data Science / Model Deployment

OCI is the official environment for the Libras AI layer. OCI Data Science is used for pipeline development, experimentation, data preparation, model testing, and artifact generation. The OCI project is `tech4change-libras`.

The MVP model is served through OCI Model Deployment; the application does not load or execute the model from GitHub. The Java backend calls the deployment and returns its prediction, DTW distance, and ranked top three results. Real prediction through the backend has returned HTTP 200 from the active deployment.

The official OCI Data Science pipeline remains the reference for landmark ordering, normalization, missing-hand handling, and adjustment to the required 64-frame sequence.

## 6. Libras model limitations

This is a proof of concept for a limited vocabulary of supported, isolated signs. Its results can be affected by framing, capture conditions, dataset size, signer diversity, and generalization to new users. It is not continuous Libras translation and must not be treated as a replacement for a professional Libras interpreter.

## 7. Speech -> Text / OCI Speech

```text
Hearing person's speech -> OCI Speech -> transcription -> caption for the deaf user
```

Realtime session generation through OCI Speech is verified. The frontend requests a session token from the backend and uses it for realtime transcription. Portuguese transcription is supported by the MVP configuration.

## 8. Backend API

| Endpoint | Purpose |
| --- | --- |
| `GET /actuator/health` | Hosting and application health check. |
| `POST /api/speech/session` | Creates an OCI Speech realtime session. No request body. |
| `POST /api/sign-language/predict` | Sends one MediaPipe landmark sequence for Libras prediction. |

`POST /api/speech/transcribe` remains mock-only. Realtime speech uses `POST /api/speech/session`.

## 9. Exact API contracts

### `POST /api/speech/session`

No request body.

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

### `POST /api/sign-language/predict`

Content type: `application/json`.

Request shape notation — `number[64][126]` must be sent as JSON arrays, not as a string:

```text
{
  "sequence": number[64][126]
}
```

This means `sequence` contains exactly 64 frame arrays, and every frame contains exactly 126 numeric values. A request with fewer or additional frames or values is rejected by backend validation.

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

`distance` is DTW distance: lower values indicate a closer match. It is not confidence, probability, or a percentage. `top3` is a ranking of model matches by that distance.

## 10. Technologies

- Frontend: Next.js, TypeScript, MediaPipe, Vercel.
- Backend: Java 21, Spring Boot, Maven, OCI Java SDK.
- AI and cloud: OCI Data Science, OCI Model Deployment, OCI Speech.
- Packaging: Docker.

## 11. Environment variables

| Variable | Required for | Default |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | Profile selection | `mock` |
| `OCI_REGION` | OCI Speech and Model Deployment | `sa-saopaulo-1` |
| `OCI_COMPARTMENT_ID` | OCI Speech session | None |
| `OCI_CONFIG_PROFILE` | OCI Speech API-key profile | `DEFAULT` |
| `OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE` | OCI Model Deployment API-key profile | `DEFAULT` |
| `OCI_MODEL_DEPLOYMENT_ID` | OCI Libras prediction | None |

For the `oci` profile, Speech uses `OCI_CONFIG_PROFILE=DEFAULT`; Model Deployment uses `OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE=SPEAK`. The OCI SDK reads both profiles from `~/.oci/config`.

## 12. Local execution

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

`mvn clean test` passes for the current backend.

## 13. Docker/deployment

Build:

```powershell
docker build -t accessible-care-api .
```

The container exposes port `8080` and checks `GET /actuator/health`. HTTPS should terminate at the hosting platform, load balancer, ingress, or reverse proxy; the application serves HTTP inside the container.

For OCI execution, mount OCI configuration and both private keys as read-only files under `/root/.oci/`. The mounted `config` must define `DEFAULT` and `SPEAK` and reference Linux container paths, not Windows paths. Never copy credential files into the image. See [`deploy/OCI_DEPLOYMENT.md`](deploy/OCI_DEPLOYMENT.md) and [`deploy/oci-config.example`](deploy/oci-config.example) for the deployment layout.

The Docker build passes locally.

## 14. Security

Never commit or publish OCI private keys, API keys, tokens, passwords, `.pem` files, `.key` files, `.env` files, `.oci` directories, credential-bearing configuration files, or temporary signed URLs. Secrets must be provided through the deployment environment's secret mechanism and mounted read-only when file-based OCI authentication is used.

## 15. Known limitations

- Libras recognition is limited to supported isolated signs, not continuous translation.
- Real-world robustness requires broader data and testing with diverse signers.
- Public deployment still requires post-deploy verification of health, CORS, Speech, and Libras prediction.

## 16. Future improvements

- Expand the supported sign vocabulary and dataset diversity.
- Improve spatial and temporal normalization, missing-hand handling, and unknown-sign handling in the official ML pipeline.
- Improve DTW-distance calibration and ranking evaluation for the model's supported signs.
- Evaluate continuous sign recognition and test with real users.
- Strengthen privacy and production security controls.

## 17. Team and contributions

FIAP Tech4Change 2026 — Group 01.

- **Victor Silva Mazzola RM370764 — Backend**
    - Java 21 / Spring Boot backend
    - REST APIs and validation
    - OCI Speech session integration
    - OCI Model Deployment integration
    - CORS, environment configuration, Docker and backend deployment
    - API and backend documentation

- **Patrick Nascimento Andrade RM369393 — Frontend**
    - Next.js / TypeScript frontend
    - Consultation interface
    - Camera and microphone integration
    - MediaPipe landmark capture
    - Backend API consumption
    - Libras prediction and caption display
    - Frontend deployment on Vercel

- **Fabiana Luizon Martins Campos RM370325 — AI / OCI**
    - Libras dataset and preprocessing pipeline
    - Model experimentation and validation
    - OCI Data Science
    - OCI Model Deployment
    - OCI Speech configuration
    - ML architecture, limitations and AI documentation
