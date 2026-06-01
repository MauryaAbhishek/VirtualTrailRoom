import hashlib
from pathlib import Path
from tempfile import NamedTemporaryFile
from uuid import UUID
from uuid import uuid4

from fastapi import UploadFile
from PIL import Image, UnidentifiedImageError

from backend.app.config import Settings
from backend.models.schemas import ImageKind
from backend.services.metadata_repository import ImageAssetRecord
from backend.utils.errors import bad_request, unsupported_media_type
from backend.utils.time import utc_now


class StorageService:
    def __init__(self, settings: Settings) -> None:
        self._settings = settings

    async def save_image_upload(
        self,
        file: UploadFile,
        kind: ImageKind,
    ) -> ImageAssetRecord:
        content_type = file.content_type
        if content_type not in self._settings.allowed_image_mime_types:
            raise unsupported_media_type("Only JPG, PNG, and WEBP images are supported.")

        asset_id = uuid4()
        extension = self._extension_for_content_type(content_type)
        target_path = self._settings.upload_dir / kind.value / f"{asset_id}.{extension}"
        target_path.parent.mkdir(parents=True, exist_ok=True)

        size_bytes, sha256, temporary_path = await self._write_limited_temp_file(file)
        width, height = self._validate_image(temporary_path)
        temporary_path.replace(target_path)

        return ImageAssetRecord(
            id=asset_id,
            kind=kind,
            original_filename=Path(file.filename or "upload").name,
            content_type=content_type,
            size_bytes=size_bytes,
            width=width,
            height=height,
            sha256=sha256,
            storage_path=target_path,
            created_at=utc_now(),
        )

    def create_output_path(self, job_id: UUID) -> Path:
        self._settings.output_dir.mkdir(parents=True, exist_ok=True)
        return self._settings.output_dir / f"{job_id}.jpg"

    def create_output_record(self, path: Path) -> ImageAssetRecord:
        width, height = self._validate_image(path)
        content = path.read_bytes()
        return ImageAssetRecord(
            id=uuid4(),
            kind=ImageKind.OUTPUT,
            original_filename=path.name,
            content_type="image/jpeg",
            size_bytes=len(content),
            width=width,
            height=height,
            sha256=hashlib.sha256(content).hexdigest(),
            storage_path=path,
            created_at=utc_now(),
        )

    async def _write_limited_temp_file(self, file: UploadFile) -> tuple[int, str, Path]:
        digest = hashlib.sha256()
        size_bytes = 0
        self._settings.upload_dir.mkdir(parents=True, exist_ok=True)

        with NamedTemporaryFile(
            delete=False,
            dir=self._settings.upload_dir,
            prefix="incoming_",
            suffix=".tmp",
        ) as temporary_file:
            temporary_path = Path(temporary_file.name)
            while chunk := await file.read(1024 * 1024):
                size_bytes += len(chunk)
                if size_bytes > self._settings.max_upload_bytes:
                    temporary_path.unlink(missing_ok=True)
                    raise bad_request("Image exceeds maximum upload size.")
                digest.update(chunk)
                temporary_file.write(chunk)

        if size_bytes == 0:
            temporary_path.unlink(missing_ok=True)
            raise bad_request("Uploaded image is empty.")

        return size_bytes, digest.hexdigest(), temporary_path

    @staticmethod
    def _validate_image(path: Path) -> tuple[int, int]:
        try:
            with Image.open(path) as image:
                image.verify()
            with Image.open(path) as image:
                width, height = image.size
        except (UnidentifiedImageError, OSError) as exc:
            path.unlink(missing_ok=True)
            raise bad_request("Uploaded file is not a readable image.") from exc

        if width <= 0 or height <= 0:
            path.unlink(missing_ok=True)
            raise bad_request("Uploaded image has invalid dimensions.")
        return width, height

    @staticmethod
    def _extension_for_content_type(content_type: str) -> str:
        return {
            "image/jpeg": "jpg",
            "image/png": "png",
            "image/webp": "webp",
        }[content_type]
