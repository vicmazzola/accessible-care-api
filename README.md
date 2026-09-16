# Accessible Care API

Backend for the **Tech4Change 2026** project, focused on assistive communication for mental health care between hearing professionals and Deaf users.

## Goal

The MVP focuses on two main communication flows:

- **Speech → Text** using Oracle OCI Speech
- **Libras → Text** using a Machine Learning model exposed through FastAPI

## Technologies

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Boot Actuator
- Oracle OCI Java SDK
- Maven

## Endpoints

### Health Check

```http
GET /actuator/health
```

### Sign Language Prediction

```http
POST /api/sign-language/predict
```

Current mock response:

```json
{
  "success": true,
  "prediction": {
    "class": "ansiedade",
    "confidence": 0.92
  },
  "error": null
}
```

### Speech-to-Text

```http
POST /api/speech/transcribe
```

### OCI Speech Session

```http
POST /api/speech/session
```

## How to Run

```powershell
.\mvnw.cmd spring-boot:run
```

Application available at:

```text
http://localhost:8080
```

## Current Status

- Backend created
- Main APIs structured
- Sign language mock working
- Speech mock working
- OCI integration structure prepared
- `mock` and `oci` profiles configured

## Next Steps

- Integrate real OCI Speech
- Integrate the FastAPI sign language model
- Integrate the frontend
- Prepare backend deployment
