from uuid import UUID

from backend.ai.engine import TryOnEngine
from backend.models.schemas import TryOnJobStatus
from backend.services.metadata_repository import MetadataRepository
from backend.services.storage_service import StorageService
from backend.utils.time import utc_now


class AiJobService:
    def __init__(
        self,
        repository: MetadataRepository,
        storage_service: StorageService,
        try_on_engine: TryOnEngine,
    ) -> None:
        self._repository = repository
        self._storage_service = storage_service
        self._try_on_engine = try_on_engine

    async def process_job(self, job_id: UUID) -> None:
        job = await self._repository.get_try_on_job(job_id)
        if job is None:
            return

        await self._repository.update_try_on_job(
            job_id=job.id,
            status=TryOnJobStatus.PROCESSING,
            updated_at=utc_now(),
        )

        try:
            user_image = await self._repository.get_image(job.user_image_id)
            clothing_image = await self._repository.get_image(job.clothing_image_id)
            if user_image is None or clothing_image is None:
                raise ValueError("Try-on job references missing image assets.")

            output_path = self._storage_service.create_output_path(job.id)
            await self._try_on_engine.generate(
                user_image_path=user_image.storage_path,
                clothing_image_path=clothing_image.storage_path,
                output_image_path=output_path,
            )
            output_record = self._storage_service.create_output_record(output_path)
            await self._repository.insert_image(output_record)
            await self._repository.update_try_on_job(
                job_id=job.id,
                status=TryOnJobStatus.COMPLETED,
                output_image_id=output_record.id,
                updated_at=utc_now(),
            )
        except Exception as exc:
            await self._repository.update_try_on_job(
                job_id=job.id,
                status=TryOnJobStatus.FAILED,
                error_message=str(exc) or exc.__class__.__name__,
                updated_at=utc_now(),
            )
