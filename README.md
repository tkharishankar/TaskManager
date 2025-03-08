# Task Manager

A modern Android task management application built with Jetpack Compose.

## Features

- **Task Management**: Create, organize, and track your tasks
- **Intuitive UI**: Material 3 design
- **Filters & Sorting**: View tasks by status (All, Pending, Completed)
- **Gestures**:
  - Swipe-to-delete with undo functionality
  - Long-press to reorder tasks
- **Dark Mode**: Full support for light and dark themes
- **Custom Theming**: Customizable primary color

## Setup Instructions

### Prerequisites
- Android Studio Ladybug Feature Drop | 2024.2.2 or newer
- Kotlin 2.1.10 or newer
- Gradle 8.10.X or newer
- Android SDK 35
- JDK 17

### Installation

1. Clone the repository
   ```
   git clone https://github.com/tkharishankar/TaskManager.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the application on an emulator or physical device

### Project Configuration

#### Build.gradle (app)

```gradle
dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Lifecycle components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Koin for DI
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    // Room for database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
```

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture with a clean separation of concerns:

### Data Layer
- **Room Database**: Stores task data locally
- **Repository Pattern**: Abstracts data operations from the rest of the app

### Domain Layer
- **Models**: Data classes representing app entities
- **Use Cases**: Business logic for task operations

### Presentation Layer
- **ViewModels**: Manages UI state and business logic
- **Composables**: Declarative UI components
- **State Management**: Unidirectional data flow with StateFlow

### Dependency Injection
- Koin for dependency injection, making components testable and decoupled

## Project Structure

```
app/
├── data/
│   ├── local/
│   │   ├── TaskDao.kt
│   │   └── AppDatabase.kt
│   └── repository/
│       ├── TaskRepository.kt
│       └── SettingsRepository.kt
├── di/
│   ├── DatabaseModule.kt
│   ├── TaskModule.kt
│   └── SettingsModule.kt
├── domain/
│   └── model/
│       └── Task.kt
├── ui/
│   ├── component/
│   │   ├── DismissBackground.kt
│   │   ├── ModifierExt.kt
│   │   ├── TaskAppBar.kt
│   │   ├── TaskBottomNavigation.kt
│   │   └── TaskNavHost.kt
│   ├── screen/
│   │   ├── creation/
│   │   │   ├── Component
│   │   │   ├── CreateTask.kt
│   │   │   └── TaskCreationViewModel.kt
│   │   ├── dashboard/
│   │   │   ├── TaskCompletionProgress.kt
│   │   │   └── TaskDashboard.kt
│   │   ├── settings/
│   │   │   ├── Settings.kt
│   │   │   └── SettingsViewModel.kt
│   │   ├── taskdetail/
│   │   │   ├── Component
│   │   │   ├── TaskDetail.kt
│   │   │   └── TaskDetailViewModel.kt
│   │   └── tasklist/
│   │       ├── Component
│   │       ├── TaskList.kt
│   │       └── TaskListViewModel.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── TaskManagerApp.kt
├── MainActivity.kt
└── TaskMangerApplication.kt
```