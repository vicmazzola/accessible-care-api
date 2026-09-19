# OCI Docker Deployment

## Runtime requirements

- Run the repository `Dockerfile` image on port `8080`.
- Terminate HTTPS at the Oracle Cloud load balancer, ingress, or reverse proxy; the container serves HTTP on port `8080`.
- Provide outbound HTTPS access to OCI Speech and OCI Model Deployment.
- Provision OCI files from the deployment secret mechanism. Do not put them in the image, repository, environment variables, or chat.

## OCI credential files

Mount these files read-only at the following Linux container paths:

```text
/root/.oci/config
/root/.oci/default_api_key.pem
/root/.oci/speak_api_key.pem
```

Use `oci-config.example` as the template for the mounted `config`. Replace placeholders only in the secret-managed runtime copy. Its `key_file` values must remain Linux paths shown above; Windows paths do not work inside the container.

`DEFAULT` is used by OCI Speech. `SPEAK` is used only by OCI Model Deployment.

## Required environment variables

```text
SPRING_PROFILES_ACTIVE=oci
OCI_REGION=sa-saopaulo-1
OCI_COMPARTMENT_ID=<speech-compartment-ocid>
OCI_MODEL_DEPLOYMENT_ID=<model-deployment-ocid>
OCI_CONFIG_PROFILE=DEFAULT
OCI_MODEL_DEPLOYMENT_CONFIG_PROFILE=SPEAK
```

## Container run shape

The hosting platform should route HTTPS traffic to container port `8080` and mount the OCI directory read-only at `/root/.oci`.

```text
container port: 8080
OCI mount: /root/.oci (read-only)
health check: GET /actuator/health
```

## Post-deploy validation

1. Confirm `GET /actuator/health` returns HTTP 200.
2. From `https://speak-front-rho.vercel.app`, confirm browser CORS permits `POST /api/speech/session`.
3. Confirm the Speech session response is successful.
4. Send a MediaPipe `sequence` containing exactly 64 frames with 126 numeric values each to `POST /api/sign-language/predict`.
5. Confirm the Libras response has `success: true`, `prediction`, DTW `distance`, and `top3`.
6. Treat lower DTW distance as a closer match; it is not confidence or probability.
