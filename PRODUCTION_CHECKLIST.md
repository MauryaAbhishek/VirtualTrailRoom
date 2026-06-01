# Production Readiness Checklist

## Android

- Use a real Firebase project and add `google-services.json` before enabling Firebase plugins.
- Replace local placeholder wardrobe categories with backend catalog data.
- Add release signing config outside source control.
- Test CameraX on at least one physical low-end device and one modern device.
- Add instrumentation tests for capture, gallery import, and navigation flows.

## Backend

- Replace SQLite with Postgres before high-concurrency production traffic.
- Put uploads and outputs on persistent object storage when deploying beyond a single node.
- Restrict `VTR_CORS_ALLOWED_ORIGINS` to trusted app/web origins.
- Add authentication middleware before exposing user data endpoints publicly.
- Put Uvicorn behind a managed TLS ingress or reverse proxy.
- Add structured logs and request IDs.

## AI

- Replace `LocalTryOnEngine` with an HR-VITON/RunPod implementation behind `TryOnEngine`.
- Add GPU health checks and model checkpoint validation on startup.
- Store model checkpoints outside the application image when possible.
- Track latency, failure rate, GPU memory, and queue depth.

## Operations

- Build and scan Docker images in CI.
- Run backend tests before deploy.
- Keep `/data` or object storage backed up.
- Add retention policies for uploaded user images and generated outputs.
