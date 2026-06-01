# Virtual Trial Room Deployment

## Local Docker Compose

```bash
docker compose up --build
```

Backend:

```text
http://127.0.0.1:8000
```

API docs:

```text
http://127.0.0.1:8000/docs
```

## Backend Image

Build:

```bash
docker build -t virtual-trial-room-backend:local ./backend
```

Run:

```bash
docker run --rm -p 8000:8000 -v virtual-trial-room-data:/data virtual-trial-room-backend:local
```

Health check:

```bash
curl http://127.0.0.1:8000/health
```

## Production Environment

Use these environment variables:

```text
VTR_APP_NAME=Virtual Trial Room API
VTR_ENVIRONMENT=production
VTR_UPLOAD_DIR=/data/uploads
VTR_OUTPUT_DIR=/data/outputs
VTR_DATABASE_PATH=/data/virtual_trial_room.sqlite3
VTR_MAX_UPLOAD_MB=12
VTR_CORS_ALLOWED_ORIGINS=https://your-app-domain.example
```

## Fastest Live Backend

Use [LIVE_TODAY.md](/Users/abhishekmaurya/Documents/Virtual%20Trial%20Room/LIVE_TODAY.md) with `render.yaml`.

## RunPod

Use [deploy/runpod/README.md](/Users/abhishekmaurya/Documents/Virtual%20Trial%20Room/deploy/runpod/README.md) and [deploy/runpod/pod-template.json](/Users/abhishekmaurya/Documents/Virtual%20Trial%20Room/deploy/runpod/pod-template.json).
