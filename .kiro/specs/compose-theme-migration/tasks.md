# Implementation Plan: Compose Theme Migration

## Overview

This implementation plan converts the design into discrete coding tasks that migrate the Shimeji Android application from XML-based theming to Jetpack Compose Material3 theming. Tasks are ordered to build incrementally, starting with theme definition, then configuration updates, and finally integration.

## Tasks

- [x] 1. Create Compose theme color schemes
  - Create `app/src/main/java/com/example/shimeji/ui/theme/Color.kt`
  - Define `LightColorScheme` with Material3 colors (primary, secondary, tertiary, background, surface, error, and on-colors)
  - Define `DarkColorScheme` with Material3 colors for dark mode
  - Use `lightColorScheme()` and `darkColorScheme()` builder functions from Compose Material3
  - _Requirements: 3.1, 3.2_

- [ ]* 1.1 Write unit tests for color scheme definitions
  - Verify LightColorScheme has all required color properties
  - Verify DarkColorScheme has all required color properties
  - _Requirements: 3.1, 3.2_

- [x] 2. Create Compose theme wrapper composable
  - Create `app/src/main/java/com/example/shimeji/ui/theme/Theme.kt`
  - Define `ShimejiTheme` composable function with `darkTheme` parameter and `content` lambda
  - Use `isSystemInDarkTheme()` to detect system theme preference
  - Select color scheme based on dark theme parameter
  - Wrap content with `MaterialTheme` using selected color scheme
  - _Requirements: 3.3, 3.4, 3.5, 3.6_

- [ ]* 2.1 Write unit tests for theme composable
  - Verify ShimejiTheme uses LightColorScheme when darkTheme=false
  - Verify ShimejiTheme uses DarkColorScheme when darkTheme=true
  - _Requirements: 3.3, 3.4_

- [x] 3. Remove XML theme references
  - [x] 3.1 Update or remove `app/src/main/res/values/themes.xml`
    - Remove the `Theme.Shimeji` style that references `Theme.Material3.DayNight.NoActionBar`
    - Either delete the file or leave it empty with just the `<resources>` tag
    - _Requirements: 1.1, 1.3_

  - [x] 3.2 Update `app/src/main/AndroidManifest.xml`
    - Remove `android:theme="@style/Theme.AppCompat.Light"` from `<application>` tag
    - Remove `android:theme="@style/Theme.AppCompat.Light"` from `<activity>` tag
    - _Requirements: 1.2, 1.4_

- [ ]* 3.3 Write tests for XML theme removal
  - Parse themes.xml and verify no Material3/AppCompat theme references
  - Parse AndroidManifest.xml and verify no XML theme attributes
  - **Property 2: XML Theme References Removed**
  - **Validates: Requirements 1.1, 1.2, 1.3, 1.4**

- [x] 4. Update MainActivity to use Compose theme
  - Modify `app/src/main/java/com/example/shimeji/MainActivity.kt`
  - Wrap existing `setContent` block content with `ShimejiTheme { }`
  - Import the new theme composable
  - Maintain existing Surface and MainScreen structure
  - _Requirements: 4.1, 4.2, 4.3_

- [ ]* 4.1 Write tests for MainActivity structure
  - Verify MainActivity.onCreate calls setContent with ShimejiTheme wrapper
  - Verify MainActivity has no XML theme references in code
  - _Requirements: 4.1, 4.2, 4.3_

- [x] 5. Verify and update build configuration
  - Review `app/build.gradle.kts` to ensure all required dependencies are present
  - Add `androidx.compose.ui:ui-tooling-preview` if missing
  - Verify `compileSdk` and `targetSdk` are at least 33 (currently 34, so no change needed)
  - Verify `buildFeatures.compose = true` is set (already set)
  - Verify `composeOptions.kotlinCompilerExtensionVersion` is set (already set)
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8_

- [ ]* 5.1 Write property test for build configuration
  - **Property 1: Required Compose Dependencies Present**
  - Parse build.gradle.kts and verify all required Compose dependencies exist
  - Verify SDK versions meet minimum requirements
  - **Validates: Requirements 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8**

- [x] 6. Checkpoint - Build and verify
  - Execute Gradle build: `./gradlew build` or `gradlew.bat build`
  - Verify build completes without "style/Theme.Material3.DayNight.NoActionBar not found" errors
  - Verify no XML theme resolution errors
  - If build fails, review error messages and fix issues
  - Ensure all tests pass, ask the user if questions arise
  - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [ ]* 6.1 Write property test for build success
  - **Property 3: Build Compilation Success**
  - Execute build and verify it completes without theme-related errors
  - **Validates: Requirements 5.1, 5.2, 5.3, 5.4**

- [ ]* 7. Write integration tests for functionality preservation
  - Verify OverlayService.kt and OverlayCharacter.kt are unchanged
  - Test that permission request functionality still works
  - Test that start service functionality still works
  - _Requirements: 6.1, 6.2, 6.3_

- [ ] 8. Final checkpoint - Complete migration verification
  - Run full test suite
  - Build and install APK on test device or emulator
  - Verify app launches successfully
  - Verify UI displays with Material3 theming
  - Verify light/dark mode switching works
  - Verify overlay functionality still works
  - Ensure all tests pass, ask the user if questions arise

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- The migration preserves all existing overlay functionality (OverlayService, OverlayCharacter)
- Most Compose dependencies are already present in the project
- The main changes are: creating theme files, removing XML themes, and wrapping MainActivity content
- Build configuration is already mostly correct (compileSdk 34, Compose enabled)
- Property-based tests use Kotest Property Testing framework for Kotlin
