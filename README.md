# ProductCatalog Android App

A production-ready Android application showcasing a product catalog using the [Fake Store API](https://fakestoreapi.com/). Built with modern Android development practices, focusing on Clean Architecture, MVVM, and offline-first functionality.

## 🚀 Features

- **Product Listing**: Browse a wide variety of products with high-quality images.
- **Lazy Loading**: Client-side pagination for smooth scrolling and optimized performance.
- **Product Details**: Detailed view for each product, including category badges and ratings.
- **Offline Support**: Robust caching mechanism using Room Database to view previously loaded data without an internet connection.
- **Error Handling**: Graceful handling of network timeouts, no-connectivity states, and API errors with easy retry options.
- **Modern UI**: Material Design 3 components with a clean, responsive layout.
- **Unit Tested**: Core logic in Repositories and ViewModels is covered by unit tests.
- **CI/CD**: Integrated GitHub Actions for automated build and test validation.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: XML with [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)

## 🏗 Architecture & Flow

The project follows **Clean Architecture** principles to ensure maintainability, testability, and scalability.

### Data Flow
`UI (Activity/Fragment)` ↔ `ViewModel` ↔ `Repository` ↔ `Retrofit (Remote)` OR `Room (Local Cache)`

1. **Presentation Layer**: UI reacts to state changes exposed via `StateFlow` from the ViewModel.
2. **Domain Layer**: Defines the business contract via Repository interfaces.
3. **Data Layer**: Orchestrates data between the Fake Store API and the Room database. It implements the "Offline-First" strategy by attempting a network fetch and falling back to the local cache on failure.

## 📶 Offline Support Strategy

The app implements an **Offline-First** approach:
1. **Request**: ViewModel requests data from the Repository.
2. **Network Try**: Repository attempts to fetch fresh data from the API.
3. **Cache Update**: On a successful network response, the local Room database is cleared and updated with the new data.
4. **Fallback**: If the network fetch fails (No internet, Timeout), the Repository automatically queries the local database and returns the cached results.
5. **UI State**: The user is only shown an error state if *both* the network and the local cache are unavailable.

## 🌐 API

This project consumes the **Fake Store API**.
- **Base URL**: `https://fakestoreapi.com/`
- **Endpoints Used**:
    - `GET /products`: Fetch all products.
    - `GET /products/{id}`: Fetch details for a specific product.
- **Documentation**: [https://fakestoreapi.com/docs](https://fakestoreapi.com/docs)

## ⚙️ Setup Instructions

- **Android Studio**: Ladybug (2024.2.1) or newer.
- **JDK**: 21 (Ensure this is configured in File > Settings > Build, Execution, Deployment > Build Tools > Gradle).
- **Minimum SDK**: 24
- **Compile SDK**: 37

1. Clone the repository: `git clone https://github.com/[username]/ProductCatalog.git`
2. Open the project in Android Studio.
3. Perform a **Gradle Sync**.
4. Run the `app` module on an emulator or physical device.

## 🔄 Continuous Integration

This project uses **GitHub Actions** for automated CI/CD.
- **Automatic Build**: Every push or pull request triggers a full project build to ensure compilation success.
- **Automatic Unit Tests**: The entire test suite is executed automatically to prevent regressions.

## 🤖 AI Usage Summary

This project utilized AI assistance for boilerplate generation, architectural scaffolding, and troubleshooting Gradle configuration issues. The developer reviewed, understood, modified where necessary, and validated all AI-generated suggestions before including them to ensure they met production standards and specific project requirements.

## 📸 Screenshots

*Screenshots will be available in the `screenshots/` directory.*
- Home Screen Placeholder
- Product Detail Screen Placeholder

## ⚠️ Future Improvements

- **Search**: Real-time product search functionality.
- **Category Filter**: Filtering products by category using API endpoints.
- **Favorites**: Ability to bookmark products locally.
- **Dark Mode**: Complete Material 3 dark theme implementation.
- **KSP Migration**: Migrating from `kapt` to `ksp` for faster processing.
