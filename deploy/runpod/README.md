# RunPod Deployment

This folder documents the GPU deployment path for the Virtual Trial Room backend.

## Current Step 11 Target

The backend container is deployment-ready and runs the local CPU `TryOnEngine` implementation. In Step 10, the engine boundary was isolated behind `backend.ai.engine.TryOnEngine`, so HR-VITON can replace the local engine without changing API routes.

## Pod Deployment

1. Build and push the backend image:

```bash
docker build -t YOUR_REGISTRY/virtual-trial-room-backend:latest ./backend
docker push YOUR_REGISTRY/virtual-trial-room-backend:latest
```

2. Create a RunPod GPU Pod with:

```text
Container Image: YOUR_REGISTRY/virtual-trial-room-backend:latest
HTTP Port: 8000
Volume Mount: /data
```

3. Environment variables:

```text
VTR_ENVIRONMENT=production
VTR_UPLOAD_DIR=/data/uploads
VTR_OUTPUT_DIR=/data/outputs
VTR_DATABASE_PATH=/data/virtual_trial_room.sqlite3
VTR_MAX_UPLOAD_MB=12
VTR_CORS_ALLOWED_ORIGINS=https://your-app-domain.example
```

4. Health endpoint:

```text
GET /health
```

## Serverless Photoreal Try-On Integration

The production backend can call a RunPod Serverless endpoint by setting:

```text
VTR_AI_PROVIDER=runpod
VTR_RUNPOD_ENDPOINT_ID=your_runpod_endpoint_id
VTR_RUNPOD_API_KEY=your_runpod_api_key
VTR_RUNPOD_TIMEOUT_SECONDS=180
```

The RunPod worker must accept this JSON payload:

```json
{
  "input": {
    "person_image_base64": "base64 encoded full-body user photo",
    "garment_image_base64": "base64 encoded saree/clothing reference photo",
    "prompt": "photoreal virtual try-on instruction",
    "output_format": "jpeg"
  }
}
```

The worker should return one of these output shapes:

```json
{
  "output": {
    "image_base64": "base64 encoded generated try-on image"
  }
}
```

or:

```json
{
  "output": "base64 encoded generated try-on image"
}
```

The prompt sent by the backend is:

```text
Using the provided woman's full-body photo and the provided saree or clothing reference image, generate a realistic virtual try-on image. The woman must be wearing the exact garment from the reference image. Preserve her face, hairstyle, body shape, pose, skin tone, and all facial details. Ensure the garment draping looks natural and realistic, with accurate fabric texture, folds, lighting, and shadows. The final output must appear like a real photograph.
```

## HR-VITON Integration Point

For a custom in-process GPU service, add an implementation of:

```python
backend.ai.engine.TryOnEngine
```

Then set `VTR_AI_PROVIDER` to route to that engine in `backend/api/dependencies.py`.

## Persistent Files

Use a persistent RunPod volume mounted at `/data` so these paths survive restarts:

```text
/data/uploads
/data/outputs
/data/virtual_trial_room.sqlite3
```
