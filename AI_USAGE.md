# AI Usage Report

## 1. AI Tools Used
- **Google Gemini (Android Studio AI Agent)**: Primary assistant for project scaffolding, Gradle troubleshooting, and specific Android implementation guidance.
- **ChatGPT (OpenAI)**: Utilized for high-level architecture discussions, documentation drafting, repository review, debugging guidance, interview preparation, and comprehensive code review suggestions.

## 2. Where AI Was Used
- **Project Scaffolding**: Creating the initial folder structure and package hierarchy.
- **Hilt Configuration**: Generating the `NetworkModule`, `DatabaseModule`, and `RepositoryModule` templates.
- **Room Setup**: Generating Entity and DAO boilerplate.
- **Gradle Troubleshooting**: Resolving specific `kapt` vs `AGP 9.x` compatibility issues and duplicate plugin block errors.
- **UI Layouts**: Initial drafting of XML layouts for Home and Detail screens.

## 3. AI Suggestions Accepted
- Use of `SavedStateHandle` in `ProductDetailViewModel` to survive process death.
- Implementation of a `Resource` sealed class for wrapping UI states.
- Extension function approach for Mappers between DTOs and Entities.

## 4. AI Suggestions Modified
- **Lazy Loading**: AI initially suggested a simple Paging 3 setup, but modified it to a custom client-side lazy loading implementation since the Fake Store API lacks server-side pagination.
- **Error Handling**: Enhanced the AI-generated error messages to be more user-friendly and specific to connection types.

## 5. AI Suggestions Rejected
- **Navigation Component**: The AI suggested using Jetpack Navigation, but was rejected in favor of explicit Intent-based navigation to keep the assignment's dependency footprint minimal and consistent with the specific requirements.
- **KSP**: AI suggested using KSP for Room/Hilt, but was rejected in favor of `kapt` to align with the environment's current stability for this specific project version.

## 6. Developer Judgment Improvement
**Scenario: Gradle Sync Failure**
During Step 1, the project encountered a severe conflict between the new AGP 9.x built-in Kotlin support and the `kapt` plugin. The AI initially suggested downgrading AGP. Instead, I exercised developer judgment to apply specific compatibility flags (`android.builtInKotlin=false`) in `gradle.properties`, allowing the project to use the latest Gradle version while maintaining `kapt` functionality required by the existing Hilt/Room setup. This preserved the modern build environment without compromising the requested architecture.
