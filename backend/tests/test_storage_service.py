import asyncio
from io import BytesIO
import tempfile
import unittest
from pathlib import Path

from fastapi import UploadFile
from PIL import Image

from backend.app.config import Settings
from backend.models.schemas import ImageKind
from backend.services.storage_service import StorageService


class StorageServiceTest(unittest.TestCase):
    def test_save_image_upload_persists_valid_metadata(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            settings = self._settings(Path(temp_dir))
            service = StorageService(settings)
            upload = self._upload_file("user.jpg", "image/jpeg")

            record = asyncio.run(service.save_image_upload(upload, ImageKind.USER))

            self.assertEqual(record.kind, ImageKind.USER)
            self.assertEqual(record.content_type, "image/jpeg")
            self.assertEqual(record.width, 64)
            self.assertEqual(record.height, 96)
            self.assertTrue(record.storage_path.exists())
            self.assertGreater(record.size_bytes, 0)
            self.assertEqual(len(record.sha256), 64)

    def test_save_image_upload_rejects_unsupported_type(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            settings = self._settings(Path(temp_dir))
            service = StorageService(settings)
            upload = self._upload_file("user.gif", "image/gif")

            with self.assertRaises(Exception):
                asyncio.run(service.save_image_upload(upload, ImageKind.USER))

    @staticmethod
    def _upload_file(filename: str, content_type: str) -> UploadFile:
        image = Image.new("RGB", (64, 96), color=(30, 80, 160))
        buffer = BytesIO()
        image.save(buffer, format="JPEG")
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
        )


if __name__ == "__main__":
    unittest.main()

