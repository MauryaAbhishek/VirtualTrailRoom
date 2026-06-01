from pathlib import Path
from typing import Protocol


class TryOnEngine(Protocol):
    async def generate(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
        user_image_url: str | None = None,
        clothing_image_url: str | None = None,
    ) -> None:
        """Generate a try-on output image."""
