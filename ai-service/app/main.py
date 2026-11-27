import os
from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel

app = FastAPI(title="Spiritual Knowledge Platform - AI Service", version="0.1.0")


def verify_internal_key(x_ai_service_key: str | None) -> None:
    expected = os.getenv("AI_SERVICE_API_KEY", "")
    if not expected or x_ai_service_key != expected:
        raise HTTPException(status_code=401, detail="Unauthorized internal AI service call")


class HealthResponse(BaseModel):
    status: str
    ai_enabled: bool
    provider: str


@app.get("/internal/v1/health", response_model=HealthResponse)
def health(x_ai_service_key: str | None = Header(default=None)):
    verify_internal_key(x_ai_service_key)
    return HealthResponse(
        status="ok",
        ai_enabled=os.getenv("AI_ENABLED", "true").lower() == "true",
        provider=os.getenv("AI_PROVIDER", "gemini"),
    )

