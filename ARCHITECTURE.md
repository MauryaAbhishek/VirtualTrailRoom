# Virtual Trial Room Architecture

This repository is organized as a production-oriented AI-powered virtual trial room platform.

## Top-Level Modules

- `android-app/`: Kotlin Android client using Jetpack Compose, MVVM, Clean Architecture, Hilt, Navigation Compose, CameraX, Retrofit, Coil, Coroutines, and StateFlow.
- `backend/`: Python FastAPI service exposing async APIs, request orchestration, storage integration, and AI job management.
- `ai-engine/`: AI inference assets and pipeline code for preprocessing, model inference, and postprocessing.

## Android Package Strategy

The Android package name will be:

```text
com.virtualtrialroom.app
```

Recommended package mapping:

```text
com.virtualtrialroom.app.data
com.virtualtrialroom.app.data.local
com.virtualtrialroom.app.data.remote
com.virtualtrialroom.app.data.repository
com.virtualtrialroom.app.domain
com.virtualtrialroom.app.domain.model
com.virtualtrialroom.app.domain.repository
com.virtualtrialroom.app.domain.usecase
com.virtualtrialroom.app.ui
com.virtualtrialroom.app.ui.components
com.virtualtrialroom.app.ui.screen
com.virtualtrialroom.app.ui.theme
com.virtualtrialroom.app.viewmodel
com.virtualtrialroom.app.navigation
com.virtualtrialroom.app.di
com.virtualtrialroom.app.util
```

## Backend Package Strategy

The backend will expose application code through the `backend` Python package and split concerns by responsibility:

```text
backend.app
backend.api
backend.services
backend.ai
backend.models
backend.utils
```

## Separation Of Concerns

- Domain contains business rules and interfaces.
- Data contains concrete implementations for network, cache, and persistence.
- UI contains only Compose rendering and user interaction wiring.
- ViewModels expose immutable StateFlow UI state and coordinate use cases.
- Backend APIs validate requests and delegate to services.
- Backend services coordinate storage, authentication, AI execution, and persistence.
- AI engine code remains isolated from HTTP concerns so it can run locally, in containers, or on GPU workers.
