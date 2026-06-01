import asyncio
from dataclasses import dataclass
from datetime import datetime
import sqlite3
from pathlib import Path
from uuid import UUID

from backend.models.schemas import ImageKind, TryOnJobStatus
from backend.utils.time import from_iso, to_iso


@dataclass(frozen=True)
class ImageAssetRecord:
    id: UUID
    kind: ImageKind
    original_filename: str
    content_type: str
    size_bytes: int
    width: int
    height: int
    sha256: str
    storage_path: Path
    created_at: datetime


@dataclass(frozen=True)
class TryOnJobRecord:
    id: UUID
    user_image_id: UUID
    clothing_image_id: UUID
    status: TryOnJobStatus
    output_image_id: UUID | None
    error_message: str | None
    created_at: datetime
    updated_at: datetime


class MetadataRepository:
    def __init__(self, database_path: Path) -> None:
        self._database_path = database_path

    async def initialize(self) -> None:
        await asyncio.to_thread(self._initialize_sync)

    async def insert_image(self, image: ImageAssetRecord) -> None:
        await asyncio.to_thread(self._insert_image_sync, image)

    async def get_image(self, image_id: UUID) -> ImageAssetRecord | None:
        return await asyncio.to_thread(self._get_image_sync, image_id)

    async def insert_try_on_job(self, job: TryOnJobRecord) -> None:
        await asyncio.to_thread(self._insert_try_on_job_sync, job)

    async def get_try_on_job(self, job_id: UUID) -> TryOnJobRecord | None:
        return await asyncio.to_thread(self._get_try_on_job_sync, job_id)

    async def update_try_on_job(
        self,
        job_id: UUID,
        status: TryOnJobStatus,
        updated_at: datetime,
        output_image_id: UUID | None = None,
        error_message: str | None = None,
    ) -> None:
        await asyncio.to_thread(
            self._update_try_on_job_sync,
            job_id,
            status,
            updated_at,
            output_image_id,
            error_message,
        )

    def _connect(self) -> sqlite3.Connection:
        self._database_path.parent.mkdir(parents=True, exist_ok=True)
        connection = sqlite3.connect(self._database_path)
        connection.row_factory = sqlite3.Row
        connection.execute("PRAGMA foreign_keys=ON")
        connection.execute("PRAGMA busy_timeout=5000")
        return connection

    def _initialize_sync(self) -> None:
        with self._connect() as connection:
            connection.execute("PRAGMA journal_mode=WAL")
            connection.execute("PRAGMA synchronous=NORMAL")
            connection.execute(
                """
                CREATE TABLE IF NOT EXISTS image_assets (
                    id TEXT PRIMARY KEY,
                    kind TEXT NOT NULL,
                    original_filename TEXT NOT NULL,
                    content_type TEXT NOT NULL,
                    size_bytes INTEGER NOT NULL,
                    width INTEGER NOT NULL,
                    height INTEGER NOT NULL,
                    sha256 TEXT NOT NULL,
                    storage_path TEXT NOT NULL,
                    created_at TEXT NOT NULL
                )
                """
            )
            connection.execute(
                """
                CREATE TABLE IF NOT EXISTS try_on_jobs (
                    id TEXT PRIMARY KEY,
                    user_image_id TEXT NOT NULL,
                    clothing_image_id TEXT NOT NULL,
                    status TEXT NOT NULL,
                    output_image_id TEXT,
                    error_message TEXT,
                    created_at TEXT NOT NULL,
                    updated_at TEXT NOT NULL,
                    FOREIGN KEY(user_image_id) REFERENCES image_assets(id),
                    FOREIGN KEY(clothing_image_id) REFERENCES image_assets(id),
                    FOREIGN KEY(output_image_id) REFERENCES image_assets(id)
                )
                """
            )
            connection.execute(
                "CREATE INDEX IF NOT EXISTS idx_image_assets_kind ON image_assets(kind)"
            )
            connection.execute(
                "CREATE INDEX IF NOT EXISTS idx_try_on_jobs_status ON try_on_jobs(status)"
            )
            connection.execute(
                """
                CREATE INDEX IF NOT EXISTS idx_try_on_jobs_created_at
                ON try_on_jobs(created_at)
                """
            )

    def _insert_image_sync(self, image: ImageAssetRecord) -> None:
        with self._connect() as connection:
            connection.execute(
                """
                INSERT INTO image_assets (
                    id, kind, original_filename, content_type, size_bytes,
                    width, height, sha256, storage_path, created_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                (
                    str(image.id),
                    image.kind.value,
                    image.original_filename,
                    image.content_type,
                    image.size_bytes,
                    image.width,
                    image.height,
                    image.sha256,
                    str(image.storage_path),
                    to_iso(image.created_at),
                ),
            )

    def _get_image_sync(self, image_id: UUID) -> ImageAssetRecord | None:
        with self._connect() as connection:
            row = connection.execute(
                "SELECT * FROM image_assets WHERE id = ?",
                (str(image_id),),
            ).fetchone()
        return self._row_to_image(row) if row is not None else None

    def _insert_try_on_job_sync(self, job: TryOnJobRecord) -> None:
        with self._connect() as connection:
            connection.execute(
                """
                INSERT INTO try_on_jobs (
                    id, user_image_id, clothing_image_id, status, output_image_id,
                    error_message, created_at, updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                (
                    str(job.id),
                    str(job.user_image_id),
                    str(job.clothing_image_id),
                    job.status.value,
                    str(job.output_image_id) if job.output_image_id else None,
                    job.error_message,
                    to_iso(job.created_at),
                    to_iso(job.updated_at),
                ),
            )

    def _get_try_on_job_sync(self, job_id: UUID) -> TryOnJobRecord | None:
        with self._connect() as connection:
            row = connection.execute(
                "SELECT * FROM try_on_jobs WHERE id = ?",
                (str(job_id),),
            ).fetchone()
        return self._row_to_job(row) if row is not None else None

    def _update_try_on_job_sync(
        self,
        job_id: UUID,
        status: TryOnJobStatus,
        updated_at: datetime,
        output_image_id: UUID | None,
        error_message: str | None,
    ) -> None:
        with self._connect() as connection:
            cursor = connection.execute(
                """
                UPDATE try_on_jobs
                SET status = ?,
                    output_image_id = ?,
                    error_message = ?,
                    updated_at = ?
                WHERE id = ?
                """,
                (
                    status.value,
                    str(output_image_id) if output_image_id else None,
                    error_message,
                    to_iso(updated_at),
                    str(job_id),
                ),
            )
            if cursor.rowcount == 0:
                raise ValueError(f"Try-on job not found: {job_id}")

    @staticmethod
    def _row_to_image(row: sqlite3.Row) -> ImageAssetRecord:
        return ImageAssetRecord(
            id=UUID(row["id"]),
            kind=ImageKind(row["kind"]),
            original_filename=row["original_filename"],
            content_type=row["content_type"],
            size_bytes=row["size_bytes"],
            width=row["width"],
            height=row["height"],
            sha256=row["sha256"],
            storage_path=Path(row["storage_path"]),
            created_at=from_iso(row["created_at"]),
        )

    @staticmethod
    def _row_to_job(row: sqlite3.Row) -> TryOnJobRecord:
        output_image_id = row["output_image_id"]
        return TryOnJobRecord(
            id=UUID(row["id"]),
            user_image_id=UUID(row["user_image_id"]),
            clothing_image_id=UUID(row["clothing_image_id"]),
            status=TryOnJobStatus(row["status"]),
            output_image_id=UUID(output_image_id) if output_image_id else None,
            error_message=row["error_message"],
            created_at=from_iso(row["created_at"]),
            updated_at=from_iso(row["updated_at"]),
        )
