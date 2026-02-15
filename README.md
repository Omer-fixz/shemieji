# Shimeji-style Android App

A minimal Android app that displays a draggable character overlay on top of other apps.

## Features

- **Overlay Permission**: Requests `SYSTEM_ALERT_WINDOW` permission
- **Foreground Service**: Keeps the character alive in the background
- **Draggable Character**: Touch and drag the character anywhere on screen
- **Sprite Animation**: 4-frame animation loop using a sprite sheet
- **Random Walk**: Character occasionally moves randomly to simulate wandering

## Setup

1. Open the project in Android Studio
2. Sync Gradle files
3. Run the app on a device (API 26+)
4. Grant overlay permission when prompted
5. Tap "Start Character" to launch the overlay

## Project Structure

- `MainActivity.kt` - Entry point with permission handling
- `OverlayService.kt` - Foreground service managing the overlay
- `OverlayCharacter.kt` - Compose UI with sprite animation and drag logic
- `AndroidManifest.xml` - Permissions and service configuration

## Customization

Replace `createDummySpriteSheet()` in `OverlayCharacter.kt` with your own sprite sheet bitmap loaded from resources.
