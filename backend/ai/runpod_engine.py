import asyncio
import base64
from pathlib import Path
from typing import Any

import httpx

from backend.ai.engine import TryOnEngine
from backend.ai.prompt import PHOTOREAL_TRY_ON_PROMPT
from backend.app.config import Settings


class RunPodTryOnEngine(TryOnEngine):
    def __init__(self, settings: Settings) -> None:
        if not settings.runpod_endpoint_id:
            raise ValueError("VTR_RUNPOD_ENDPOINT_ID is required when VTR_AI_PROVIDER=runpod.")
        if not settings.runpod_api_key:
            raise ValueError("VTR_RUNPOD_API_KEY is required when VTR_AI_PROVIDER=runpod.")
        self._endpoint_id = settings.runpod_endpoint_id
        self._api_key = settings.runpod_api_key
        self._timeout_seconds = settings.runpod_timeout_seconds

    async def generate(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
    ) -> None:
        payload = {
            "input": {
                "person_image_base64": self._encode_image(user_image_path),
                "garment_image_base64": self._encode_image(clothing_image_path),
                "prompt": PHOTOREAL_TRY_ON_PROMPT,
                "output_format": "jpeg",
            }
        }

        async with httpx.AsyncClient(timeout=self._timeout_seconds) as client:
            run_response = await client.post(
                self._url("run"),
                headers=self._headers(),
                json=payload,
            )
            run_response.raise_for_status()
            run_payload = run_response.json()
            job_id = run_payload.get("id")
            if not isinstance(job_id, str) or not job_id:
                raise ValueError("RunPod response did not include a job id.")

            output_payload = await self._wait_for_output(client, job_id)

        image_base64 = self._extract_output_image(output_payload)
        image_bytes = base64.b64decode(image_base64)
        output_image_path.parent.mkdir(parents=True, exist_ok=True)
        output_image_path.write_bytes(image_bytes)

    async def _wait_for_output(self, client: httpx.AsyncClient, job_id: str) -> dict[str, Any]:
        deadline = asyncio.get_running_loop().time() + self._timeout_seconds
        while asyncio.get_running_loop().time() < deadline:
            response = await client.get(
                self._url(f"status/{job_id}"),
                headers=self._headers(),
            )
            response.raise_for_status()
            payload = response.json()
            status = payload.get("status")
            if status == "COMPLETED":
                return payload
            if status in {"FAILED", "CANCELLED", "TIMED_OUT"}:
                error = payload.get("error") or payload.get("output") or status
                raise ValueError(f"RunPod try-on job failed: {error}")
            await asyncio.sleep(2)

        raise TimeoutError("RunPod try-on job timed out.")

    def _url(self, path: str) -> str:
        return f"https://api.runpod.ai/v2/{self._endpoint_id}/{path}"

    def _headers(self) -> dict[str, str]:
        return {
            "Authorization": f"Bearer {self._api_key}",
            "Content-Type": "application/json",
        }

    @staticmethod
    def _encode_image(path: Path) -> str:
        return base64.b64encode(path.read_bytes()).decode("ascii")

    @staticmethod
    def _extract_output_image(payload: dict[str, Any]) -> str:
        output = payload.get("output")
        candidates: list[Any] = [
            output,
            output.get("image_base64") if isinstance(output, dict) else None,
            output.get("output_image_base64") if isinstance(output, dict) else None,
            output.get("result_image_base64") if isinstance(output, dict) else None,
            payload.get("image_base64"),
        ]
        for candidate in candidates:
            if isinstance(candidate, str) and candidate:
                return candidate.removeprefix("data:image/jpeg;base64,").removeprefix(
                    "data:image/png;base64,"
                )
        raise ValueError("RunPod response did not include a base64 output image.")
