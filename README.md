# Duski (Android)

🔒 Laatste security check: 2026-08-19 09:05 CEST

Android port of [Duski](https://github.com/EdCafferata/duski), a free sleep-sounds and white-noise app. All sounds are generated procedurally on-device — no recordings, no streaming, no licensing risk — same as the iOS app.

Native Kotlin + Jetpack Compose. The sound generators and safety/age logic are a 1:1 port of the iOS app's `AVAudioEngine`-based generators.

## Status

**Working:**
- Onboarding — pick an age group (Baby/Kind/Tiener/Volwassene/Oudere); content and a safe volume limit adjust accordingly (Baby is hard-capped)
- Sound picker across the same categories as iOS — Ruis (wit/roze/bruin/grijs/blauw), Natuur (regen, golven, wind, kampvuur, beek), Lichaam & baby (hartslag, baarmoeder, ademhaling, sussen, föhn), Overige (klankschaal, ventilator, trein, klok, vliegtuigcabine, autorijden, plus royalty-free classical arrangements — Bach, Pachelbel/Canon, Satie/Gymnopédie, Debussy/Clair de Lune); one sound active at a time, same as iOS
- Sleep timer with a gradual fade-out
- Screensaver picker with animated SwiftUI-equivalent Compose screensavers (including the sheep-counting one)
- Subscription via Google Play Billing (`AbonnementManager.kt`) — the Android equivalent of the iOS app's StoreKit 2 flow, product loading + purchase + entitlement tracking wired up

**Not yet verified end-to-end:** the Play Billing flow needs a real subscription product configured in a Google Play Console listing to test purchases live — no Play Console listing exists yet for this app (same blocker as the other Android ports' tip jars).

## Requirements

- JDK 17+ (project built and tested with Homebrew's `openjdk@21`)
- Android SDK, compileSdk 36, minSdk 26
- Gradle (via the included wrapper)

## Build

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export ANDROID_HOME=/opt/homebrew/share/android-commandlinetools   # or your own SDK path
./gradlew assembleDebug
```

The debug APK lands at `app/build/outputs/apk/debug/app-debug.apk`.

## Licence

GPL-3.0 — see [LICENSE](LICENSE), same as the iOS app.
