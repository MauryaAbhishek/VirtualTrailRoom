import asyncio
from pathlib import Path

from PIL import Image, ImageChops, ImageFilter, ImageOps

from backend.ai.engine import TryOnEngine


class LocalTryOnEngine(TryOnEngine):
    async def generate(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
        user_image_url: str | None = None,
        clothing_image_url: str | None = None,
    ) -> None:
        await asyncio.to_thread(
            self._generate_sync,
            user_image_path,
            clothing_image_path,
            output_image_path,
        )

    def _generate_sync(
        self,
        user_image_path: Path,
        clothing_image_path: Path,
        output_image_path: Path,
    ) -> None:
        with Image.open(user_image_path) as user_source:
            user_image = ImageOps.exif_transpose(user_source).convert("RGB")
        with Image.open(clothing_image_path) as clothing_source:
            clothing_image = ImageOps.exif_transpose(clothing_source).convert("RGBA")

        garment = self._extract_garment(clothing_image)
        fitted_garment = self._fit_garment_to_body(garment, user_image.size)

        output = user_image.convert("RGBA")
        output.alpha_composite(
            fitted_garment,
            dest=self._garment_anchor(user_image.size, fitted_garment.size),
        )

        output_image_path.parent.mkdir(parents=True, exist_ok=True)
        output.convert("RGB").save(output_image_path, format="JPEG", quality=94, optimize=True)

    @staticmethod
    def _extract_garment(image: Image.Image) -> Image.Image:
        rgba = image.convert("RGBA")
        alpha = rgba.getchannel("A")

        if alpha.getextrema() == (255, 255):
            background = Image.new("RGBA", rgba.size, rgba.getpixel((0, 0)))
            difference = ImageChops.difference(rgba, background).convert("L")
            alpha = difference.point(lambda value: 255 if value > 24 else 0)

        alpha = alpha.filter(ImageFilter.GaussianBlur(radius=1.4))
        rgba.putalpha(alpha)
        bbox = rgba.getbbox()
        if bbox is None:
            return rgba
        return rgba.crop(bbox)

    @staticmethod
    def _fit_garment_to_body(garment: Image.Image, user_size: tuple[int, int]) -> Image.Image:
        user_width, user_height = user_size
        garment_ratio = garment.height / max(1, garment.width)
        is_full_body_garment = garment_ratio >= 1.12
        if is_full_body_garment:
            target_width = max(1, int(user_width * 0.86))
            target_height = max(1, int(user_height * 0.82))
        else:
            target_width = max(1, int(user_width * 0.58))
            target_height = max(1, int(user_height * 0.46))

        garment.thumbnail((target_width, target_height), Image.Resampling.LANCZOS)
        canvas = Image.new("RGBA", (target_width, target_height), (0, 0, 0, 0))
        x = (target_width - garment.width) // 2
        y = (target_height - garment.height) // 2
        canvas.alpha_composite(garment, (x, y))
        return canvas

    @staticmethod
    def _garment_anchor(
        user_size: tuple[int, int],
        garment_size: tuple[int, int],
    ) -> tuple[int, int]:
        user_width, user_height = user_size
        garment_width, garment_height = garment_size
        x = (user_width - garment_width) // 2
        garment_ratio = garment_height / max(1, garment_width)
        y = int(user_height * (0.14 if garment_ratio >= 1.12 else 0.28))
        return x, min(y, user_height - garment_height)
