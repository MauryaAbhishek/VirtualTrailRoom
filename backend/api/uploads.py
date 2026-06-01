from uuid import UUID

from fastapi import APIRouter, Depends, File, UploadFile, status
from fastapi.responses import FileResponse

from backend.api.dependencies import repository_dependency, storage_service_dependency
from backend.models.schemas import ImageAssetResponse, ImageKind
from backend.services.metadata_repository import MetadataRepository
from backend.services.storage_service import StorageService
from backend.utils.errors import not_found
from backend.utils.errors import bad_request

router = APIRouter(prefix="/api/v1/uploads", tags=["uploads"])


@router.post(
    "/images",
    response_model=ImageAssetResponse,
    status_code=status.HTTP_201_CREATED,
)
async def upload_image(
    kind: ImageKind,
    file: UploadFile = File(...),
    storage_service: StorageService = Depends(storage_service_dependency),
    repository: MetadataRepository = Depends(repository_dependency),
) -> ImageAssetResponse:
    if kind == ImageKind.OUTPUT:
        raise bad_request("Output images are created by the try-on pipeline.")
    image = await storage_service.save_image_upload(file=file, kind=kind)
    await repository.insert_image(image)
    return ImageAssetResponse.model_validate(image)


@router.get("/images/{image_id}", response_model=ImageAssetResponse)
async def get_image(
    image_id: UUID,
    repository: MetadataRepository = Depends(repository_dependency),
) -> ImageAssetResponse:
    image = await repository.get_image(image_id)
    if image is None:
        raise not_found("Image upload was not found.")
    return ImageAssetResponse.model_validate(image)


@router.get("/images/{image_id}/content")
async def get_image_content(
    image_id: UUID,
    repository: MetadataRepository = Depends(repository_dependency),
) -> FileResponse:
    image = await repository.get_image(image_id)
    if image is None or not image.storage_path.exists():
        raise not_found("Image content was not found.")
    return FileResponse(
        path=image.storage_path,
        media_type=image.content_type,
        filename=image.original_filename,
    )
