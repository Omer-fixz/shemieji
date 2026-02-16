# Requirements Document

## Introduction

This specification defines the requirements for migrating the Shimeji Android application from XML-based theming to a fully Jetpack Compose-based theming system. The current application has XML theme references that cause build errors due to missing Material3 theme definitions. The migration will eliminate XML themes, establish a proper Compose Material3 theme with light/dark mode support, and ensure the application builds and runs successfully.

## Glossary

- **Compose**: Jetpack Compose, Android's modern declarative UI toolkit
- **Material3**: Material Design 3, the latest version of Google's design system
- **Theme**: A collection of colors, typography, and shapes that define the visual appearance of an application
- **Color_Scheme**: A set of colors (primary, secondary, background, etc.) used throughout the application
- **XML_Theme**: Legacy Android theme defined in XML resource files
- **Compose_Theme**: Theme defined using Jetpack Compose's MaterialTheme composable

## Requirements

### Requirement 1: Remove XML Theme Dependencies

**User Story:** As a developer, I want to remove all XML-based theme definitions, so that the application no longer depends on legacy theming systems and the build error is resolved.

#### Acceptance Criteria

1. WHEN the application is built THEN the system SHALL NOT reference Theme.Material3.DayNight.NoActionBar or any Material3 XML themes
2. WHEN the application is built THEN the system SHALL NOT reference Theme.AppCompat themes in the AndroidManifest
3. THE Theme_System SHALL remove or replace all XML theme definitions in res/values/themes.xml
4. THE AndroidManifest SHALL use a Compose-compatible theme or no theme attribute

### Requirement 2: Configure Jetpack Compose Dependencies

**User Story:** As a developer, I want proper Jetpack Compose dependencies configured, so that the application can use Compose Material3 components and theming.

#### Acceptance Criteria

1. THE Build_Configuration SHALL include androidx.compose.material3:material3 dependency
2. THE Build_Configuration SHALL include androidx.compose.ui:ui dependency
3. THE Build_Configuration SHALL include androidx.compose.ui:ui-tooling-preview dependency
4. THE Build_Configuration SHALL include androidx.activity:activity-compose dependency
5. THE Build_Configuration SHALL set compileSdk to at least 33
6. THE Build_Configuration SHALL set targetSdk to at least 33
7. THE Build_Configuration SHALL enable Compose build features
8. THE Build_Configuration SHALL set appropriate Kotlin compiler extension version for Compose

### Requirement 3: Implement Compose Material3 Theme

**User Story:** As a developer, I want a Compose-based Material3 theme with light and dark color schemes, so that the application has a modern, consistent visual appearance that adapts to system theme preferences.

#### Acceptance Criteria

1. THE Theme_System SHALL define a light color scheme with primary, secondary, tertiary, background, surface, and other Material3 colors
2. THE Theme_System SHALL define a dark color scheme with primary, secondary, tertiary, background, surface, and other Material3 colors
3. WHEN the system is in light mode THEN the Theme_System SHALL apply the light color scheme
4. WHEN the system is in dark mode THEN the Theme_System SHALL apply the dark color scheme
5. THE Theme_System SHALL provide a composable function that wraps content with MaterialTheme
6. THE Theme_System SHALL be defined in Kotlin code, not XML

### Requirement 4: Update MainActivity to Use Compose Theme

**User Story:** As a developer, I want MainActivity to use the new Compose theme, so that all UI components render with consistent Material3 styling.

#### Acceptance Criteria

1. WHEN MainActivity is created THEN the system SHALL call setContent with the Compose theme wrapper
2. THE MainActivity SHALL apply the custom Compose theme to all content
3. THE MainActivity SHALL NOT reference any XML themes or styles
4. WHEN the application launches THEN the UI SHALL display with proper Material3 theming

### Requirement 5: Ensure Build Success

**User Story:** As a developer, I want the application to build successfully without theme-related errors, so that I can compile and run the application.

#### Acceptance Criteria

1. WHEN the application is built THEN the build SHALL complete without "style/Theme.Material3.DayNight.NoActionBar not found" errors
2. WHEN the application is built THEN the build SHALL complete without any XML theme resolution errors
3. WHEN the application is built THEN all Compose dependencies SHALL resolve correctly
4. THE Application SHALL compile successfully with the new Compose theme configuration

### Requirement 6: Maintain Existing Functionality

**User Story:** As a user, I want the application to maintain its existing overlay functionality, so that the migration to Compose theming does not break any features.

#### Acceptance Criteria

1. WHEN the application launches THEN the main screen SHALL display with permission request and start service buttons
2. WHEN the user interacts with buttons THEN the overlay permission and service functionality SHALL work as before
3. THE Migration SHALL NOT modify the core overlay service logic
4. THE Migration SHALL only affect theming and UI presentation layer
