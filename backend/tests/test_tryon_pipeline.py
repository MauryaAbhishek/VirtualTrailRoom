import asyncio
from io import BytesIO
import tempfile
import unittest
from pathlib import Path

from fastapi import UploadFile
from PIL import Image, ImageDraw

from backend.ai.local_engine import LocalTryOnEngine
from backend.app.config import Settings
from backend.models.schemas import ImageKind, TryOnJobStatus
from backend.services.ai_job_service import AiJobService
from backend.services.metadata_repository import MetadataRepository
from backend.services.storage_service import StorageService
from backend.services.tryon_service import TryOnService


class TryOnPipelineTest(unittest.TestCase):
    def test_try_on_job_completes_with_output_asset(self) -> None:
        asyncio.run(self._run_pipeline())

    async def _run_pipeline(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            root = Path(temp_dir)
            settings = self._settings(root)
            repository = MetadataRepository(settings.database_path)
            storage = StorageService(settings)
            try_on = TryOnService(repository)
            ai_jobs = AiJobService(repository, storage, LocalTryOnEngine())

            await repository.initialize()
            user = await storage.save_image_upload(
                self._user_upload(),
                ImageKind.USER,
            )
            clothing = await storage.save_image_upload(
                self._clothing_upload(),
                ImageKind.CLOTHING,
            )
            await repository.insert_image(user)
            await repository.insert_image(clothing)

            job = await try_on.create_job(user.id, clothing.id)
            await ai_jobs.process_job(job.id)
            completed = await try_on.get_job(job.id)

            self.assertEqual(completed.status, TryOnJobStatus.COMPLETED)
            self.assertIsNotNone(completed.output_image_id)
            output = await repository.get_image(completed.output_image_id)
            self.assertIsNotNone(output)
            self.assertTrue(output.storage_path.exists())
            self.assertEqual(output.width, 320)
            self.assertEqual(output.height, 480)

    @staticmethod
    def _user_upload() -> UploadFile:
        image = Image.new("RGB", (320, 480), color=(210, 220, 230))
        draw = ImageDraw.Draw(image)
        draw.ellipse((126, 48, 194, 116), fill=(128, 92, 72))
        draw.rectangle((102, 118, 218, 340), fill=(170, 145, 125))
        return TryOnPipelineTest._to_upload(image, "user.jpg", "image/jpeg", "JPEG")

    @staticmethod
    def _clothing_upload() -> UploadFile:
        image = Image.new("RGBA", (240, 240), color=(255, 255, 255, 0))
        draw = ImageDraw.Draw(image)
        draw.polygon(
            [(60, 42), (180, 42), (222, 112), (188, 138), (174, 214), (66, 214), (52, 138), (18, 112)],
            fill=(20, 108, 220, 235),
        )
        return TryOnPipelineTest._to_upload(image, "shirt.png", "image/png", "PNG")

    @staticmethod
    def _to_upload(
        image: Image.Image,
        filename: str,
        content_type: str,
        image_format: str,
    ) -> UploadFile:
        buffer = BytesIO()
        image.save(buffer, format=image_format)
        buffer.seek(0)
        return UploadFile(filename=filename, file=buffer, headers={"content-type": content_type})

    @staticmethod
    def _settings(root: Path) -> Settings:
        return Settings(
            app_name="Test API",
            environment="test",
            backend_dir=root,
            upload_dir=root / "uploads",
            output_dir=root / "outputs",
            database_path=root / "test.sqlite3",
            max_upload_bytes=12 * 1024 * 1024,
            cors_allowed_origins=("*",),
            allowed_image_mime_types=frozenset({"image/jpeg", "image/png", "image/webp"}),
            ai_provider="local",
            public_base_url=None,
            runpod_endpoint_id=None,
            runpod_api_key=None,
            runpod_timeout_seconds=180,
            fashn_api_key=None,
            fashn_timeout_seconds=180,
        )


if __name__ == "__main__":
    unittest.main()
