import base64
from pathlib import Path
from typing import Any

import httpx

from backend.ai.engine import TryOnEngine
from backend.ai.prompt import PHOTOREAL_TRY_ON_PROMPT
from backend.app.config import Settings


class RunPodQwenImageEditEngine(TryOnEngine):
    ENDPOINT_ID = "qwen-image-edit-2511"

    def __init__(self, settings: Settings) -> None:
        if not settings.runpod_api_key:
            raise ValueError("VTR_RUNPOD_API_KEY is required when VTR_AI_PROVIDER=runpod_qwen.")
        self._api_key = settings.runpod_api_key
        self._timeout_seconds = settings.runpod_timeout_seconds

    async def generate(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
        user_image_url: str | None = None,
        clothing_image_url: str | None = None,
    ) -> None:
        person_image = user_image_url or self._data_url(user_image_path)
        garment_image = clothing_image_url or self._data_url(clothing_image_path)
        payload = {
            "input": {
                "prompt": PHOTOREAL_TRY_ON_PROMPT,
                "images": [person_image, garment_image],
                "size": "1024*1280",
                "output_format": "jpg",
                "negative_prompt": (
                    "cartoon, illustration, fake overlay, duplicate person, changed face, "
                    "changed hairstyle, distorted body, blurry, low quality"
                ),
            }
        }

        async with httpx.AsyncClient(timeout=self._timeout_seconds) as client:
            response = await client.post(
                f"https://api.runpod.ai/v2/{self.ENDPOINT_ID}/runsync",
                headers=self._headers(),
                json=payload,
            )
            response.raise_for_status()
            response_payload = response.json()

        image_base64 = self._extract_output_image(response_payload)
        output_image_path.parent.mkdir(parents=True, exist_ok=True)
        output_image_path.write_bytes(base64.b64decode(image_base64))

    def _headers(self) -> dict[str, str]:
        return {
            "Authorization": f"Bearer {self._api_key}",
            "Content-Type": "application/json",
        }

    @staticmethod
    def _data_url(path: Path) -> str:
        suffix = path.suffix.lower()
        media_type = {
            ".jpg": "image/jpeg",
            ".jpeg": "image/jpeg",
            ".png": "image/png",
            ".webp": "image/webp",
        }.get(suffix, "image/jpeg")
        encoded = base64.b64encode(path.read_bytes()).decode("ascii")
        return f"data:{media_type};base64,{encoded}"

    @staticmethod
    def _extract_output_image(payload: dict[str, Any]) -> str:
        output = payload.get("output")
        candidates: list[Any] = [
            output,
            output.get("image") if isinstance(output, dict) else None,
            output.get("image_base64") if isinstance(output, dict) else None,
            output.get("output_image_base64") if isinstance(output, dict) else None,
            output.get("result") if isinstance(output, dict) else None,
            output[0] if isinstance(output, list) and output else None,
            payload.get("image_base64"),
        ]
        for candidate in candidates:
            if isinstance(candidate, str) and candidate:
                return candidate.removeprefix("data:image/jpeg;base64,").removeprefix(
                    "data:image/png;base64,"
                )
            if isinstance(candidate, dict):
                nested = candidate.get("image") or candidate.get("image_base64")
                if isinstance(nested, str) and nested:
                    return nested.removeprefix("data:image/jpeg;base64,").removeprefix(
                        "data:image/png;base64,"
                    )
        raise ValueError(f"RunPod Qwen response did not include an output image: {payload}")
