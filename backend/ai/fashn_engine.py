import asyncio
from pathlib import Path
from typing import Any

import httpx

from backend.ai.engine import TryOnEngine
from backend.ai.prompt import PHOTOREAL_TRY_ON_PROMPT
from backend.app.config import Settings


class FashnTryOnEngine(TryOnEngine):
    BASE_URL = "https://api.fashn.ai/v1"

    def __init__(self, settings: Settings) -> None:
        if not settings.fashn_api_key:
            raise ValueError("VTR_FASHN_API_KEY is required when VTR_AI_PROVIDER=fashn.")
        self._api_key = settings.fashn_api_key
        self._timeout_seconds = settings.fashn_timeout_seconds

    async def generate(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
        user_image_url: str | None = None,
        clothing_image_url: str | None = None,
    ) -> None:
        if not user_image_url or not clothing_image_url:
            raise ValueError("VTR_PUBLIC_BASE_URL is required when VTR_AI_PROVIDER=fashn.")

        payload = {
            "model_name": "tryon-max",
            "inputs": {
                "model_image": user_image_url,
                "product_image": clothing_image_url,
                "prompt": PHOTOREAL_TRY_ON_PROMPT,
                "resolution": "1k",
                "generation_mode": "balanced",
                "num_images": 1,
                "output_format": "jpeg",
            },
        }

        async with httpx.AsyncClient(timeout=self._timeout_seconds) as client:
            run_response = await client.post(
                f"{self.BASE_URL}/run",
                headers=self._headers(),
                json=payload,
            )
            run_response.raise_for_status()
            prediction_id = self._extract_prediction_id(run_response.json())
            completed_payload = await self._wait_for_completion(client, prediction_id)
            output_url = self._extract_output_url(completed_payload)
            image_response = await client.get(output_url)
            image_response.raise_for_status()

        output_image_path.parent.mkdir(parents=True, exist_ok=True)
        output_image_path.write_bytes(image_response.content)

    def _headers(self) -> dict[str, str]:
        return {
            "Authorization": f"Bearer {self._api_key}",
            "Content-Type": "application/json",
        }

    async def _wait_for_completion(
        self,
        client: httpx.AsyncClient,
        prediction_id: str,
    ) -> dict[str, Any]:
        deadline = asyncio.get_running_loop().time() + self._timeout_seconds
        while asyncio.get_running_loop().time() < deadline:
            response = await client.get(
                f"{self.BASE_URL}/status/{prediction_id}",
                headers=self._headers(),
            )
            response.raise_for_status()
            payload = response.json()
            status = payload.get("status")
            if status == "completed":
                return payload
            if status == "failed":
                raise ValueError(f"FASHN try-on failed: {payload.get('error')}")
            await asyncio.sleep(2)
        raise TimeoutError("FASHN try-on job timed out.")

    @staticmethod
    def _extract_prediction_id(payload: dict[str, Any]) -> str:
        prediction_id = payload.get("id")
        if not isinstance(prediction_id, str) or not prediction_id:
            raise ValueError(f"FASHN response did not include a prediction id: {payload}")
        return prediction_id

    @staticmethod
    def _extract_output_url(payload: dict[str, Any]) -> str:
        output = payload.get("output")
        if isinstance(output, list) and output and isinstance(output[0], str):
            return output[0]
        if isinstance(output, str) and output:
            return output
        raise ValueError(f"FASHN response did not include an output image URL: {payload}")
