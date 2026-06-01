# Go Live Today

## 1. Deploy Backend On Render

1. Push this repo to GitHub.
2. Open Render.
3. Choose **New +** → **Blueprint**.
4. Select this repository.
5. Render will read `render.yaml`.
6. Deploy the service.
7. Copy the public backend URL, for example:

```text
https://virtual-trial-room-backend.onrender.com/
```

8. Verify:

```bash
curl https://YOUR_RENDER_URL/health
```

Expected:

```json
{"status":"ok","service":"Virtual Trial Room API","environment":"production"}
```

## 2. Point Android Release To Backend

In `gradle.properties`, replace:

```properties
vtr.releaseApiBaseUrl=https://your-production-api.example/
```

with the Render URL:

```properties
vtr.releaseApiBaseUrl=https://YOUR_RENDER_URL/
```

The URL must end with `/`.

## 3. Build Android

Debug build:

```bash
./gradlew assembleDebug
```

Release build:

```bash
./gradlew assembleRelease
```

## 4. Test Full Flow

1. Open app.
2. Capture or import image.
3. Select clothing.
4. Tap **Generate Try-On**.
5. Wait for processing.
6. Result image should load from backend.

## 5. Before Public Launch

- Use a paid Render instance or RunPod for better cold-start behavior.
- Replace `VTR_CORS_ALLOWED_ORIGINS=*` with your trusted app/web origins.
- Add Firebase Auth before storing real user images at scale.
- Replace `LocalTryOnEngine` with HR-VITON/RunPod GPU inference for realistic results.
