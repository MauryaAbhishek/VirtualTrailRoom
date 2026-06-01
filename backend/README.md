# Virtual Trial Room Backend

FastAPI backend for image uploads and try-on job orchestration.

## Run Locally

```bash
cd backend
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cd ..
uvicorn backend.app.main:app --reload
```

## Test

```bash
cd ..
backend/.venv/bin/python -m unittest discover backend/tests
```

## API

- `GET /health`
- `POST /api/v1/uploads/images?kind=user`
- `POST /api/v1/uploads/images?kind=clothing`
- `GET /api/v1/uploads/images/{image_id}`
- `POST /api/v1/try-on/jobs`
- `GET /api/v1/try-on/jobs/{job_id}`

## Storage

Uploaded files are stored in `backend/uploads`.
Generated try-on outputs are stored in `backend/outputs`.
Metadata and job records are stored in SQLite at `backend/virtual_trial_room.sqlite3`.

## AI Processing

Creating a try-on job schedules asynchronous background processing. The default local engine is CPU-based and writes a generated output image. The engine boundary is isolated behind `backend.ai.engine.TryOnEngine` so a GPU HR-VITON or RunPod implementation can replace the local engine without changing API routes.

## Docker

```bash
docker build -t virtual-trial-room-backend:local .
docker run --rm -p 8000:8000 -v virtual-trial-room-data:/data virtual-trial-room-backend:local
```

The container stores uploads, outputs, and SQLite metadata under `/data`.
