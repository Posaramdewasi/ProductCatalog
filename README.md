# ProductCatalog Android App

A production-ready Android application showcasing a product catalog using the [Fake Store API](https://fakestoreapi.com/). Built with modern Android development practices, focusing on Clean Architecture, MVVM, and offline-first functionality.

## 🚀 Features

- **Product Listing**: Browse a wide variety of products with high-quality images.
- **Lazy Loading**: Client-side pagination for smooth scrolling and optimized performance.
- **Product Details**: Detailed view for each product, including category badges and ratings.
- **Offline Support**: Robust caching mechanism using Room Database to view previously loaded data without an internet connection.
- **Error Handling**: Graceful handling of network timeouts, no-connectivity states, and API errors with easy retry options.
- **Modern UI**: Material Design 3 components with a clean, responsive layout.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: XML with [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)

## 🏗 Architecture & Project Structure

The project follows **Clean Architecture** principles to ensure maintainability, testability, and scalability.

```
com.example.productcatalog
├── data
│   ├── api          # Retrofit API interfaces
│   ├── db           # Room Database, Entities, and DAOs
│   ├── mapper       # Mappers between DTOs and Entities
│   ├── model        # Data Transfer Objects (DTOs)
│   └── repository   # Repository implementations
├── di               # Hilt Dependency Injection modules
├── domain
│   └── repository   # Repository interfaces (Domain boundary)
├── ui
│   ├── adapter      # RecyclerView adapters
│   ├── detail       # Product Detail UI and ViewModel
│   └── home         # Home Screen UI and ViewModel
└── utils            # Generic helpers (Resource wrapper)
```

## 📶 Offline Support Strategy

The app implements an **Offline-First** approach:
1. **Request**: ViewModel requests data from the Repository.
2. **Network Try**: Repository attempts to fetch fresh data from the API.
3. **Cache Update**: On a successful network response, the local Room database is cleared and updated with the new data.
4. **Fallback**: If the network fetch fails (No internet, Timeout), the Repository automatically queries the local database and returns the cached results.
5. **UI State**: The user is only shown an error state if *both* the network and the local cache are unavailable.

## ⚙️ Setup Instructions

1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Ensure you have **JDK 21** configured in Gradle settings.
4. Sync the project with Gradle files.
5. Run the app on an emulator or physical device.

## 🤖 AI Usage Summary

This project utilized AI assistance for boilerplate generation, architectural scaffolding, and troubleshooting Gradle configuration issues related to AGP 9.x. All AI-generated code was reviewed, refactored, and improved by a Senior Engineer to ensure production standards.

## 🔄 Continuous Integration

This project uses **GitHub Actions** for automated CI/CD.
- **Automatic Build**: Every push or pull request triggers a full project build to ensure compilation success.
- **Automatic Unit Tests**: The entire test suite is executed automatically to prevent regressions.
- **Gradle Caching**: Optimized workflow execution time using GitHub's runner cache.

## ⚠️ Known Limitations & Future Improvements

- **API Pagination**: The Fake Store API does not support server-side pagination; lazy loading is implemented client-side.
- **Search & Filter**: Future versions could include product searching and category filtering.
- **Unit Testing**: Adding comprehensive unit tests for ViewModels and Repositories.
- **UI Components**: Implementing a dedicated "Favorite" feature to save products locally.
