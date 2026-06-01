FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ENV VTR_ENVIRONMENT=production
ENV VTR_UPLOAD_DIR=/data/uploads
ENV VTR_OUTPUT_DIR=/data/outputs
ENV VTR_DATABASE_PATH=/data/virtual_trial_room.sqlite3

WORKDIR /srv/virtual-trial-room

COPY requirements.txt ./requirements.txt
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt \
    && addgroup --system app \
    && adduser --system --ingroup app app \
    && mkdir -p /data/uploads /data/outputs \
    && chown -R app:app /data /srv/virtual-trial-room

COPY . ./backend
RUN chown -R app:app /srv/virtual-trial-room/backend

USER app

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD python -c "import json,urllib.request; response=urllib.request.urlopen('http://127.0.0.1:8000/health', timeout=3); data=json.load(response); raise SystemExit(0 if data.get('status') == 'ok' else 1)"

CMD ["uvicorn", "backend.app.main:app", "--host", "0.0.0.0", "--port", "8000"]
