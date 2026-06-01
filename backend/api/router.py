from fastapi import APIRouter

from backend.api import health, tryon, uploads

api_router = APIRouter()
api_router.include_router(health.router)
api_router.include_router(uploads.router)
api_router.include_router(tryon.router)

