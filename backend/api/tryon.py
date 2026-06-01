from uuid import UUID

from fastapi import APIRouter, BackgroundTasks, Depends, status

from backend.api.dependencies import ai_job_service_dependency, try_on_service_dependency
from backend.models.schemas import TryOnJobCreateRequest, TryOnJobResponse
from backend.services.ai_job_service import AiJobService
from backend.services.tryon_service import TryOnService

router = APIRouter(prefix="/api/v1/try-on", tags=["try-on"])


@router.post(
    "/jobs",
    response_model=TryOnJobResponse,
    status_code=status.HTTP_202_ACCEPTED,
)
async def create_try_on_job(
    request: TryOnJobCreateRequest,
    background_tasks: BackgroundTasks,
    try_on_service: TryOnService = Depends(try_on_service_dependency),
    ai_job_service: AiJobService = Depends(ai_job_service_dependency),
) -> TryOnJobResponse:
    job = await try_on_service.create_job(
        user_image_id=request.user_image_id,
        clothing_image_id=request.clothing_image_id,
    )
    background_tasks.add_task(ai_job_service.process_job, job.id)
    return TryOnJobResponse.model_validate(job)


@router.get("/jobs/{job_id}", response_model=TryOnJobResponse)
async def get_try_on_job(
    job_id: UUID,
    try_on_service: TryOnService = Depends(try_on_service_dependency),
) -> TryOnJobResponse:
    job = await try_on_service.get_job(job_id)
    return TryOnJobResponse.model_validate(job)
