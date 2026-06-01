from fastapi import APIRouter, Depends

from backend.api.dependencies import settings_dependency
from backend.app.config import Settings
from backend.models.schemas import HealthResponse

router = APIRouter(tags=["health"])


@router.get("/health", response_model=HealthResponse)
async def health(settings: Settings = Depends(settings_dependency)) -> HealthResponse:
    return HealthResponse(
        status="ok",
        service=settings.app_name,
        environment=settings.environment,
    )

