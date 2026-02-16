# Design Document: Compose Theme Migration

## Overview

This design describes the migration of the Shimeji Android application from XML-based theming to Jetpack Compose Material3 theming. The migration involves removing XML theme files, configuring Compose dependencies, creating a Kotlin-based theme system with light and dark color schemes, and updating the application entry point to use the new theme.

The design follows Android's modern UI development best practices using Jetpack Compose and Material Design 3. The theme system will automatically adapt to the device's light/dark mode preference, providing a consistent and modern user experience.

## Architecture

The architecture follows a layered approach:

1. **Build Configuration Layer**: Gradle build files configure Compose dependencies and compiler options
2. **Theme Definition Layer**: Kotlin files define color schemes and theme composables
3. **Application Layer**: MainActivity applies the theme to all UI content
4. **UI Component Layer**: Existing Compose UI components consume theme values

### Component Interaction Flow

```
MainActivity.onCreate()
    └─> setContent { }
        └─> ShimejiTheme { }  // Custom theme wrapper
            └─> MaterialTheme { }  // Material3 theme with color schemes
                └─> Surface { }
                    └─> MainScreen { }  // Existing UI
```

## Components and Interfaces

### 1. Build Configuration (build.gradle.kts)

**Purpose**: Configure Compose dependencies and build options

**Configuration Requirements**:
- `compileSdk`: 34 (already set)
- `targetSdk`: 34 (already set)
- `buildFeatures.compose`: true (already set)
- `composeOptions.kotlinCompilerExtensionVersion`: "1.5.3" (already set)

**Dependencies** (most already present):
```kotlin
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
implementation("androidx.activity:activity-compose:1.8.0")
implementation(platform("androidx.compose:compose-bom:2023.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
```

### 2. Color Schemes (ui/theme/Color.kt)

**Purpose**: Define color palettes for light and dark themes

**Light Color Scheme**:
- Primary: Purple-based color for primary actions
- Secondary: Teal-based color for secondary elements
- Tertiary: Pink-based accent color
- Background: Light neutral background
- Surface: Light surface color for cards/containers
- Error: Red for error states
- OnPrimary/OnSecondary/etc.: Contrasting text colors

**Dark Color Scheme**:
- Primary: Lighter purple for dark mode
- Secondary: Lighter teal for dark mode
- Tertiary: Lighter pink for dark mode
- Background: Dark neutral background
- Surface: Dark surface color
- Error: Light red for dark mode
- OnPrimary/OnSecondary/etc.: Contrasting text colors for dark backgrounds

**Interface**:
```kotlin
val LightColorScheme: ColorScheme
val DarkColorScheme: ColorScheme
```

### 3. Theme Composable (ui/theme/Theme.kt)

**Purpose**: Provide a composable function that applies Material3 theme based on system settings

**Functionality**:
- Detect system dark mode preference using `isSystemInDarkTheme()`
- Select appropriate color scheme (light or dark)
- Apply color scheme to MaterialTheme
- Wrap provided content with themed context

**Interface**:
```kotlin
@Composable
fun ShimejiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
```

**Implementation Logic**:
1. Determine if dark theme should be used (parameter or system preference)
2. Select color scheme: `if (darkTheme) DarkColorScheme else LightColorScheme`
3. Apply to MaterialTheme with selected colors
4. Render content within themed context

### 4. MainActivity Updates

**Purpose**: Apply Compose theme to application content

**Changes Required**:
- Wrap existing `setContent` block with `ShimejiTheme { }`
- Remove any XML theme references
- Maintain existing UI structure (Surface, MainScreen)

**Updated Structure**:
```kotlin
setContent {
    ShimejiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(...)
        }
    }
}
```

### 5. XML Theme Removal

**Purpose**: Eliminate XML theme files that cause build errors

**Files to Modify**:
- `res/values/themes.xml`: Remove or empty the file
- `AndroidManifest.xml`: Remove theme attributes from application and activity tags

**AndroidManifest Changes**:
- Remove `android:theme="@style/Theme.AppCompat.Light"` from `<application>`
- Remove `android:theme="@style/Theme.AppCompat.Light"` from `<activity>`

## Data Models

No new data models are required for this migration. The theme system uses Compose's built-in types:

- `ColorScheme`: Material3 color scheme containing all theme colors
- `Typography`: Material3 typography scale (using defaults)
- `Shapes`: Material3 shape scale (using defaults)


## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

For this migration, most correctness criteria are configuration checks and structural validations that verify the migration was completed correctly. These are best validated through example-based tests that check specific files and configurations.

### Property 1: Required Compose Dependencies Present

*For any* build configuration after migration, all required Compose dependencies (material3, ui, ui-tooling-preview, activity-compose) should be present in the dependencies block.

**Validates: Requirements 2.1, 2.2, 2.3, 2.4**

### Property 2: XML Theme References Removed

*For any* XML resource file or AndroidManifest after migration, no references to Material3 or AppCompat XML themes should exist.

**Validates: Requirements 1.1, 1.2, 1.3, 1.4**

### Property 3: Build Compilation Success

*For any* build execution after migration, the build should complete successfully without theme-related errors.

**Validates: Requirements 5.1, 5.2, 5.3, 5.4**

### Example-Based Tests

The following criteria are best validated through specific example tests:

**Build Configuration Validation**:
- Verify compileSdk >= 33 (Requirement 2.5)
- Verify targetSdk >= 33 (Requirement 2.6)
- Verify buildFeatures.compose = true (Requirement 2.7)
- Verify kotlinCompilerExtensionVersion is set (Requirement 2.8)

**Theme System Validation**:
- Verify LightColorScheme has all required colors (Requirement 3.1)
- Verify DarkColorScheme has all required colors (Requirement 3.2)
- Verify ShimejiTheme uses LightColorScheme when darkTheme=false (Requirement 3.3)
- Verify ShimejiTheme uses DarkColorScheme when darkTheme=true (Requirement 3.4)
- Verify ShimejiTheme composable exists with correct signature (Requirement 3.5)
- Verify theme is defined in .kt files (Requirement 3.6)

**MainActivity Validation**:
- Verify MainActivity.onCreate calls setContent with ShimejiTheme (Requirement 4.1, 4.2)
- Verify MainActivity has no XML theme references (Requirement 4.3)

**Service Logic Preservation**:
- Verify OverlayService.kt and OverlayCharacter.kt are unchanged (Requirement 6.3)

## Error Handling

### Build Errors

**Missing Dependencies**:
- If Compose dependencies are missing, the build will fail with "Unresolved reference" errors
- Solution: Ensure all dependencies are added to build.gradle.kts

**Theme Resolution Errors**:
- If XML themes are still referenced, build fails with "style/Theme.X not found"
- Solution: Remove all XML theme references from themes.xml and AndroidManifest.xml

**Compose Compiler Mismatch**:
- If Kotlin version and Compose compiler version are incompatible, build fails
- Solution: Use compatible versions (Kotlin 1.9.10 with Compose compiler 1.5.3)

### Runtime Errors

**Theme Not Applied**:
- If ShimejiTheme is not used in setContent, UI may not have proper theming
- Solution: Wrap all content in setContent with ShimejiTheme { }

**Color Scheme Issues**:
- If color schemes are incomplete, some UI elements may have default colors
- Solution: Ensure all Material3 color roles are defined in both light and dark schemes

## Testing Strategy

### Unit Tests

Unit tests will validate specific configuration and structural requirements:

1. **Build Configuration Tests**:
   - Parse build.gradle.kts and verify all required dependencies are present
   - Verify SDK versions meet minimum requirements
   - Verify Compose build features are enabled

2. **XML Theme Removal Tests**:
   - Parse themes.xml and verify no Material3/AppCompat themes are referenced
   - Parse AndroidManifest.xml and verify no theme attributes reference XML themes

3. **Theme Definition Tests**:
   - Verify LightColorScheme and DarkColorScheme objects exist
   - Verify all required color properties are defined
   - Verify ShimejiTheme composable exists with correct signature

4. **MainActivity Structure Tests**:
   - Parse MainActivity.kt and verify setContent contains ShimejiTheme wrapper
   - Verify no XML theme references in MainActivity code

5. **Service Preservation Tests**:
   - Verify OverlayService.kt and OverlayCharacter.kt are unchanged from baseline

### Property-Based Tests

Property-based tests will validate universal properties across the migration:

1. **Dependency Completeness Property** (Property 1):
   - Generate variations of build.gradle.kts content
   - Verify all required Compose dependencies are present
   - Minimum 100 iterations

2. **XML Theme Absence Property** (Property 2):
   - Parse all XML files in the project
   - Verify none contain Material3 or AppCompat theme references
   - Minimum 100 iterations

3. **Build Success Property** (Property 3):
   - Execute build with the new configuration
   - Verify build completes without theme-related errors
   - Minimum 100 iterations

### Integration Tests

Integration tests will verify the complete migration:

1. **Build and Run Test**:
   - Execute full Gradle build
   - Verify no compilation errors
   - Verify APK is generated successfully

2. **Theme Application Test**:
   - Launch application in test environment
   - Verify UI renders with Material3 theming
   - Verify light/dark mode switching works

3. **Functionality Preservation Test**:
   - Launch application
   - Verify permission request button works
   - Verify start service button works
   - Verify overlay service can be started

### Testing Framework

- **Unit Testing**: JUnit 4 for Kotlin/Android
- **Property-Based Testing**: Kotest Property Testing for Kotlin
- **Build Testing**: Gradle test tasks
- **Integration Testing**: Android Instrumentation Tests (optional)

### Test Configuration

All property-based tests should be configured with:
- Minimum 100 iterations per test
- Tag format: **Feature: compose-theme-migration, Property {number}: {property_text}**
- Each test should reference its corresponding design property
