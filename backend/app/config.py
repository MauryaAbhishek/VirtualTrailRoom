from dataclasses import dataclass
import os
from pathlib import Path


@dataclass(frozen=True)
class Settings:
    app_name: str
    environment: str
    backend_dir: Path
    upload_dir: Path
    output_dir: Path
    database_path: Path
    max_upload_bytes: int
    cors_allowed_origins: tuple[str, ...]
    allowed_image_mime_types: frozenset[str]
    ai_provider: str
    public_base_url: str | None
    runpod_endpoint_id: str | None
    runpod_api_key: str | None
    runpod_timeout_seconds: float


def get_settings() -> Settings:
    backend_dir = Path(__file__).resolve().parents[1]
    upload_dir = Path(os.getenv("VTR_UPLOAD_DIR", backend_dir / "uploads"))
    output_dir = Path(os.getenv("VTR_OUTPUT_DIR", backend_dir / "outputs"))
    database_path = Path(os.getenv("VTR_DATABASE_PATH", backend_dir / "virtual_trial_room.sqlite3"))
    max_upload_mb = int(os.getenv("VTR_MAX_UPLOAD_MB", "12"))
    cors_allowed_origins = tuple(
        origin.strip()
        for origin in os.getenv("VTR_CORS_ALLOWED_ORIGINS", "*").split(",")
        if origin.strip()
    )

    return Settings(
        app_name=os.getenv("VTR_APP_NAME", "Virtual Trial Room API"),
        environment=os.getenv("VTR_ENVIRONMENT", "development"),
        backend_dir=backend_dir,
        upload_dir=upload_dir,
        output_dir=output_dir,
        database_path=database_path,
        max_upload_bytes=max_upload_mb * 1024 * 1024,
        cors_allowed_origins=cors_allowed_origins,
        allowed_image_mime_types=frozenset({"image/jpeg", "image/png", "image/webp"}),
        ai_provider=os.getenv("VTR_AI_PROVIDER", "local").strip().lower(),
        public_base_url=os.getenv("VTR_PUBLIC_BASE_URL"),
        runpod_endpoint_id=os.getenv("VTR_RUNPOD_ENDPOINT_ID"),
        runpod_api_key=os.getenv("VTR_RUNPOD_API_KEY"),
        runpod_timeout_seconds=float(os.getenv("VTR_RUNPOD_TIMEOUT_SECONDS", "180")),
    )
