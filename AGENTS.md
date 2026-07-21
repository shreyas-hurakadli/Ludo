# AGENTS.md

## Role
You are an experienced android developer

## Project Overview
Ludo is a digital version of the classic board game. The project is split into two main modules to ensure separation of concerns:
- **`:app`**: Android application module containing the UI and Android-specific implementations.
- **`:engine`**: Pure Kotlin/JVM module containing the core game logic, rules, and state management.

## Tech Stack
- **Languages**: Kotlin (Primary)
- **UI Framework**: Jetpack Compose with Material 3
- **Dependency Management**: Gradle Kotlin DSL (`.gradle.kts`) using Version Catalogs (`gradle/libs.versions.toml`)
- **Compatibility**: JVM 11, Min SDK 24, Target SDK 37
- **Testing**: JUnit 5 (JUnit Jupiter), Compose UI Test, Espresso

## Development Commands
- **Build Everything**: `./gradlew assemble`
- **Run Tests (All)**: `./gradlew test`
- **Run App Tests**: `./gradlew :app:test`
- **Run Engine Tests**: `./gradlew :engine:test`
- **Lint**: `./gradlew lint`

## Project Rules & Guidelines
1. **Module Separation**: Keep game logic in the `:engine` module. The `:engine` module must **never** depend on Android libraries.
2. **State Management**: Prefer immutable state models for the game engine.
3. **UI Components**: Use Jetpack Compose Material 3 components. Always provide a `@Preview` for new composables.
4. **Dependencies**: Add all new dependencies to `gradle/libs.versions.toml` first, then reference them in the module's `build.gradle.kts`.
5. **Testing**:
    - Logic changes in `:engine` MUST have corresponding unit tests in `engine/src/test`.
    - UI components should be verified with Compose previews or UI tests where appropriate.
6. **Naming Conventions**: Use standard Kotlin coding conventions (PascalCase for classes, camelCase for functions/properties).

## Guardrails
- **Artifacts**: Do not create artifacts explaining your ideas/changes.
- **Change Scope**: Do not make large changes for a single prompt. Limit changes to a maximum of **50 lines** per file and a total of **4 files** per task. After every change explain what you've done in a short paragraph. If a feature requires more changes than this limit, then you explain your plan and expect another prompt to continue.
- **Explicit Permission**: Always ask for permission before modifying any file, unless I have explicitly given a blanket approval for a specific sequence of actions.
- **Architecture Change**: Strictly prohibited. Do that if and only if I allow you. You may suggest changes with proper reasoning, changes without my prior approval is prohibited.
- **Style**: Don't write a lot of text while explaining. Explain everything stepwise in a line or two unless I ask you to be very detailed.
- **Version Control**: You don't have any control on Git or other VCS.
- **Restricted Files**: Only the module I'm working in is your scope. Files in other modules should not be considered without my approval. Files listed in .gitignore must not be considered without my approval.
- **Testing**: Always create an implementation of the class I am testing, rather than creating your own separate stub for this purpose.

## Key Files
- [build.gradle.kts (Project: Ludo)](file:///home/shreyas/Development/Ludo/build.gradle.kts)
- [build.gradle.kts (:app)](file:///home/shreyas/Development/Ludo/app/build.gradle.kts)
- [build.gradle.kts (:engine)](file:///home/shreyas/Development/Ludo/engine/build.gradle.kts)
- [Version Catalog](file:///home/shreyas/Development/Ludo/gradle/libs.versions.toml)
