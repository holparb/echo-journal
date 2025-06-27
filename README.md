# EchoJournal

<p>
  An audio journaling app designed to help users quickly log their thoughts and mood throughout the day with voice memos.
</p>

<p>
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue.svg" alt="Min SDK">
  <img src="https://img.shields.io/badge/Target%20SDK-35-brightgreen.svg" alt="Target SDK">
  <img src="https://img.shields.io/badge/Kotlin-2.1.20-purple.svg" alt="Kotlin Version">
  <img src="https://img.shields.io/badge/Jetpack%20ComposeBom-2025.04.00-orange.svg" alt="Jetpack Compose Version">
</p>

## Overview

**EchoJournal** is an Android application that allows users to:

- Record quick voice memos
- Tag them with moods and topics
- Organize logs in a filterable journal
- Optionally enable extended features like AI transcription

## Features

### Splash Screen
- Simple branding screen displayed on app launch.

### Journal History
- Displays a list of audio entries grouped by day (Today, Yesterday, or date headers).
- Filter audio logs by mood and topic.
- Supports inline audio playback with visual audio-level indicators.

### Quick Voice Recording
- Start recording via a floating action button.
- Pause/resume functionality.
- Cancel ongoing recordings.

### Create Record Screen
- Add or edit details like mood, topic(s), title, and optional description.
- Save the new log entry to the journal.

### Settings Screen
- Set default mood or topics for new log entries.

### Home Screen Widget
- Quickly open the app to create a new recording from the user’s home screen.

## Screenshots

<table>
  <tr>
    <td align="center">
      <strong>1. Splash-screen</strong><br/>
      <img src="screenshots/1- Splash-screen.png" width="240" alt="Splash-screen" />
    </td>
    <td align="center">
      <strong>2. Entries - Empty</strong><br/>
      <img src="screenshots/2- Entries - Empty.png" width="240" alt="Entries - Empty" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>3. Entries</strong><br/>
      <img src="screenshots/3- Entries.png" width="240" alt="Entries" />
    </td>
    <td align="center">
      <strong>4. Entries - Mood Filter Active</strong><br/>
      <img src="screenshots/4- Entries - Mood Filter Active.png" width="240" alt="Entries - Mood Filter Active" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>5. Entries - Topic Filter Active</strong><br/>
      <img src="screenshots/5- Entries - Topic Filter Active.png" width="240" alt="Entries - Topic Filter Active" />
    </td>
    <td align="center">
      <strong>6. Entries - Recording</strong><br/>
      <img src="screenshots/6- Entries - Recording.png" width="240" alt="Entries - Record in Progress" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>7. Entries - Quick Recording (Empty fields)</strong><br/>
      <img src="screenshots/7- Entries - Quick Recording.png" width="240" alt="Create Record - Empty fields" />
    </td>
    <td align="center">
      <strong>8. Creating New Entry - Mood Selector</strong><br/>
      <img src="screenshots/8- Creating New Entry - Mood Selector.png" width="240" alt="Create Record - Additional Details" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>9. Creating New Entry - Topic Selector</strong><br/>
      <img src="screenshots/9- Creating New Entry - Topic Selector.png" width="240" alt="Create Record - Additional Details" />
    </td>
    <td align="center">
      <strong>10. Creating New Entry - Audio Playback</strong><br/>
      <img src="screenshots/10- Creating New Entry - Audio Playback.png" width="240" alt="Create Record - Additional Details" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>11. Settings</strong><br/>
      <img src="screenshots/11- Settings.png" width="240" alt="Create Record - Additional Details" />
    </td>
    <td align="center">
      <strong>12. Home Screen Widget</strong><br/>
      <img src="screenshots/12- Home Screen Widget.png" width="240" alt="Create Record - Additional Details" />
    </td>
  </tr>
</table>

## Tech Stack & Libraries

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Koin](https://insert-koin.io/) for dependency injection
- Reactive Programming with [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
    
- Room for local database
- Architecture Components
    - Lifecycle components
    - Navigation Compose
- **Audio Recording & Playback**:
    - Native [MediaRecorder](https://developer.android.com/reference/android/media/MediaRecorder) for capturing audio
    - Native [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer) for playback
- [Timber](https://github.com/JakeWharton/timber) for logging

## Architecture

EchoJournal is currently built as **single-module** for the following reasons:
- Simpler Gradle configuration and fewer module boundaries.
- Faster to set up for smaller teams or single developers.
- Ideal for rapidly iterating on the MVP.

## Potential Future Updates:
- **Multi-Module Refactor**: As EchoJournal expands (e.g., larger teams, more complex AI features), break down the single module into features with their own layers.
- **Dark Theme**: Create a dark theme.
- **Network Synchronization**: Add cloud sync or user authentication if needed for multi-device usage.

## Acknowledgment

This project was built as part of the **[Pl Mobile Dev Campus](https://pl-coding.com/campus/)** community challenge.

