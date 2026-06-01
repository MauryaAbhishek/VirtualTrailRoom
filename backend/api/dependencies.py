from functools import lru_cache

from backend.app.config import Settings, get_settings
from backend.ai.engine import TryOnEngine
from backend.ai.local_engine import LocalTryOnEngine
from backend.ai.runpod_engine import RunPodTryOnEngine
from backend.ai.runpod_qwen_engine import RunPodQwenImageEditEngine
from backend.services.metadata_repository import MetadataRepository
from backend.services.ai_job_service import AiJobService
from backend.services.storage_service import StorageService
from backend.services.tryon_service import TryOnService


@lru_cache
def settings_dependency() -> Settings:
    return get_settings()


@lru_cache
def repository_dependency() -> MetadataRepository:
    return MetadataRepository(settings_dependency().database_path)


def storage_service_dependency() -> StorageService:
    return StorageService(settings_dependency())


def try_on_service_dependency() -> TryOnService:
    return TryOnService(repository_dependency())


@lru_cache
def try_on_engine_dependency() -> TryOnEngine:
    settings = settings_dependency()
    if settings.ai_provider == "runpod_qwen":
        return RunPodQwenImageEditEngine(settings)
    if settings.ai_provider == "runpod":
        return RunPodTryOnEngine(settings)
    if settings.ai_provider == "local":
        return LocalTryOnEngine()
    raise ValueError(f"Unsupported AI provider: {settings.ai_provider}")


def ai_job_service_dependency() -> AiJobService:
    return AiJobService(
        repository=repository_dependency(),
        storage_service=storage_service_dependency(),
        try_on_engine=try_on_engine_dependency(),
    )
