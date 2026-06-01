from datetime import datetime
from enum import StrEnum
from typing import Annotated
from uuid import UUID

from pydantic import BaseModel, ConfigDict, Field


class ImageKind(StrEnum):
    USER = "user"
    CLOTHING = "clothing"
    OUTPUT = "output"


class TryOnJobStatus(StrEnum):
    QUEUED = "queued"
    PROCESSING = "processing"
    COMPLETED = "completed"
    FAILED = "failed"


class HealthResponse(BaseModel):
    status: str
    service: str
    environment: str


class ImageAssetResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID
    kind: ImageKind
    original_filename: str
    content_type: str
    size_bytes: int
    width: int
    height: int
    sha256: str
    created_at: datetime


class TryOnJobCreateRequest(BaseModel):
    user_image_id: UUID
    clothing_image_id: UUID


class TryOnJobResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID
    user_image_id: UUID
    clothing_image_id: UUID
    status: TryOnJobStatus
    output_image_id: UUID | None = None
    error_message: str | None = None
    created_at: datetime
    updated_at: datetime


class ErrorResponse(BaseModel):
    detail: Annotated[str, Field(min_length=1)]
