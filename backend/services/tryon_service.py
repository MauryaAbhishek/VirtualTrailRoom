from uuid import UUID, uuid4

from backend.models.schemas import ImageKind, TryOnJobStatus
from backend.services.metadata_repository import MetadataRepository, TryOnJobRecord
from backend.utils.errors import bad_request, not_found
from backend.utils.time import utc_now


class TryOnService:
    def __init__(self, repository: MetadataRepository) -> None:
        self._repository = repository

    async def create_job(
        self,
        user_image_id: UUID,
        clothing_image_id: UUID,
    ) -> TryOnJobRecord:
        user_image = await self._repository.get_image(user_image_id)
        if user_image is None:
            raise not_found("User image was not found.")
        if user_image.kind != ImageKind.USER:
            raise bad_request("user_image_id must reference a user image upload.")

        clothing_image = await self._repository.get_image(clothing_image_id)
        if clothing_image is None:
            raise not_found("Clothing image was not found.")
        if clothing_image.kind != ImageKind.CLOTHING:
            raise bad_request("clothing_image_id must reference a clothing image upload.")

        now = utc_now()
        job = TryOnJobRecord(
            id=uuid4(),
            user_image_id=user_image_id,
            clothing_image_id=clothing_image_id,
            status=TryOnJobStatus.QUEUED,
            output_image_id=None,
            error_message=None,
            created_at=now,
            updated_at=now,
        )
        await self._repository.insert_try_on_job(job)
        return job

    async def get_job(self, job_id: UUID) -> TryOnJobRecord:
        job = await self._repository.get_try_on_job(job_id)
        if job is None:
            raise not_found("Try-on job was not found.")
        return job

