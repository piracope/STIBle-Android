# STIBle / MIVBle

An Android implementation of the [STIBle / MIVBle](https://stible.elitios.net) frontend built with
Jetpack Compose.

## Requirements

- Gradle
- A [Mapbox](https://www.mapbox.com/) account

## Installation

A pre-packaged APK can be
downloaded [here](https://drive.google.com/file/d/1uwbW6sXp5kjGKcNMwhVRMar6aGMVyxFW).
The following instructions relate to manual building.

### Get Mapbox API keys

1. Go to the [access token creation form](https://account.mapbox.com/access-tokens/create) on your
   Mapbox account.
2. Create an access token with the `DOWNLOADS:READ` secret scope.
3. Copy this secret token and paste it to your local Gradle config
    + Location : `~/.gradle/gradle.properties`
    + Format : `MAPBOX_DOWNLOADS_TOKEN=xxxxxxxxxxxxxxxxxxxxxxxx`
4. Copy your default public token to the `app/src/main/res/values/strings.xml`, line 3.

### Setup APK generation (required even if you don't plan to release)

1. Copy `keystore.properties.example` to `keystore.properties`.
2. [Generate an upload key and keystore](https://developer.android.com/studio/publish/app-signing#generate-key)
3. Fill in the values in `keystore.properties`

## Package structure

At the root of the package structure sits the Activity, Application and the main
Composable, `STIBleApp`.

### `ui`

The presentation layer.

At the root of this package sits `STIBleViewModelProvider`, which exposes a factory used by
screens to create their associated ViewModel.

#### `navigation`

Defines the bottom navigation bar as well as the navigation logic.

#### `screens`

The different screens of the application, their necessary composables and associated ViewModels.

#### `theme`

The Material 3 Theme used by this application.

#### `util`

Utility functions that could be used in the UI by any composable.

### `data`

The data layer.

At the root of this package are `AppContainer` (manual dependency injection), and different abstract
repositories implemented in the following packages.

#### `database`

DAOs, Database and Repository implementations using a local Room database.

#### `datastore`

Storing of user preferences locally using a Jetpack Preferences DataStore.

#### `dto`

The different DTOs and Entities.

#### `locale`

The local handling of the Locale, through the in-app locale API provided by Android.

#### `network`

Repository implementations using Retrofit to interact with the game's backend.

#### `util`

Some enum classes that didn't really fit anywhere.

## Backend

The backend's code can be found at [this GitHub repository](https://github.com/piracope/STIBle).

## Authors and acknowledgements

This app was fully developed by Ayoub Moufidi as part of his curriculum at the Haute École
Bruxelles-Brabant, École Supérieure d'Informatique (HE2B-ESI).

Other acknowledgements may be found in the backend's GitHub.