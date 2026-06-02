import base64
import io
import os
from typing import Any

import runpod
from PIL import Image, ImageOps


def _decode_image(value: str) -> Image.Image:
    if "," in value and value.startswith("data:image/"):
        value = value.split(",", 1)[1]
    image_bytes = base64.b64decode(value, validate=True)
    image = Image.open(io.BytesIO(image_bytes))
    return ImageOps.exif_transpose(image).convert("RGB")


def _encode_image(image: Image.Image) -> str:
    output = io.BytesIO()
    image.save(output, format="JPEG", quality=94, optimize=True)
    return base64.b64encode(output.getvalue()).decode("ascii")


def _center_fit(image: Image.Image, size: tuple[int, int]) -> Image.Image:
    canvas = Image.new("RGB", size, (244, 246, 250))
    fitted = ImageOps.contain(image, size)
    x = (size[0] - fitted.width) // 2
    y = (size[1] - fitted.height) // 2
    canvas.paste(fitted, (x, y))
    return canvas


def _demo_try_on(person_image: Image.Image, garment_image: Image.Image) -> Image.Image:
    person = _center_fit(person_image, (1024, 1280)).convert("RGBA")
    garment = ImageOps.contain(garment_image.convert("RGBA"), (560, 620))
    garment_alpha = garment.split()[-1]
    garment_alpha = garment_alpha.point(lambda pixel: int(pixel * 0.88))
    garment.putalpha(garment_alpha)

    x = (person.width - garment.width) // 2
    y = int(person.height * 0.34)
    person.alpha_composite(garment, (x, y))
    return person.convert("RGB")


def handler(event: dict[str, Any]) -> dict[str, Any]:
    payload = event.get("input") or {}
    person_image_base64 = payload.get("person_image_base64")
    garment_image_base64 = payload.get("garment_image_base64")

    if not isinstance(person_image_base64, str) or not person_image_base64:
        return {"error": "person_image_base64 is required"}
    if not isinstance(garment_image_base64, str) or not garment_image_base64:
        return {"error": "garment_image_base64 is required"}

    provider_mode = os.getenv("VTR_RUNPOD_WORKER_MODE", "demo")
    if provider_mode != "demo":
        return {
            "error": (
                "VTR_RUNPOD_WORKER_MODE must be configured with a production VTON model "
                "before this worker can generate photoreal garment-preserving results."
            )
        }

    person_image = _decode_image(person_image_base64)
    garment_image = _decode_image(garment_image_base64)
    result_image = _demo_try_on(person_image, garment_image)
    return {"image_base64": _encode_image(result_image)}


runpod.serverless.start({"handler": handler})
