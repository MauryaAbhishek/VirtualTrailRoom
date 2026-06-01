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

## HR-VITON Integration Point

Add the GPU implementation as a new class implementing:

```python
backend.ai.engine.TryOnEngine
```

Then update `backend/api/dependencies.py` to return that engine instead of `LocalTryOnEngine`.

## Persistent Files

Use a persistent RunPod volume mounted at `/data` so these paths survive restarts:

```text
/data/uploads
/data/outputs
/data/virtual_trial_room.sqlite3
```

