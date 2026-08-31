<div align="center">

# MAHVA GALLERY
### Gold Valuation & Atelier Pricing Engine

<p align="center">
  <i>A bespoke precision calculation suite designed for fine jewelry ateliers and gold galleries.</i>
</p>

---

</div>

## Overview

**Mahva Gallery Gold Calc** is a dual-platform valuation suite crafted to compute real-time gold pricing, artisanal labor fees (*Ojrat*), profit margins, and statutory taxation with instant mathematical precision.

Available both as a native Android experience and an offline-first Progressive Web Application.

---

## Platforms & Architecture

| Platform | Technology | Design Characteristics |
| :--- | :--- | :--- |
| **Android** | Kotlin · Jetpack Compose | Native 120Hz rendering, RTL layout, Persian digit normalization, SharedPreferences persistence |
| **PWA** | Vanilla HTML5 / ES6 / CSS3 | Zero-dependency standalone web app, embedded Vazirmatn typography, LocalStorage state |
| **CI / CD** | GitHub Actions | Automated headless APK compilation without requiring local Android Studio installations |

---

## Directory Structure

```
mahva-gallery/
├── android/            # Native Jetpack Compose application
│   ├── app/            # Source code, theme tokens, and unit tests
│   └── gradle/         # Version catalog and Gradle wrapper
├── pwa/                # Standalone PWA web client & brand assets
└── .github/
    └── workflows/      # Automated cloud APK build pipeline
```

---

## Valuation Mathematics

All transactions follow standardized Iranian gold trade pricing standards:

$$\begin{aligned}
\text{Raw Gold Value } (C) &= \text{Price per Gram } (A) \times \text{Weight in Grams } (B) \\[6pt]
\text{Artisanal Labor / Ojrat } (E) &= \frac{\text{Labor \% } (D) \times C}{100} \\[6pt]
\text{Trade Profit } (G) &= \frac{\text{Profit \% } (F) \times E}{100} \\[6pt]
\text{Statutory Tax } (I) &= \frac{(E + G) \times \text{Tax \% } (H)}{100} \\[6pt]
\text{Final Price } (K) &= C + E + G + I \\[6pt]
\text{Effective Markup } (J) &= \left(\frac{K}{C} - 1\right) \times 100 \quad [\%]
\end{aligned}$$

---

## Getting Started

### Cloud Build (Recommended)
This repository compiles `.apk` binaries automatically via GitHub Actions:
1. Push any commit to the `main` branch.
2. Navigate to **Actions** $\rightarrow$ **Build Android APK**.
3. Download the signed artifact `mahva-gallery-debug-apk`.

### Local Compilation
To compile the Android package locally using the Gradle wrapper:

```bash
cd android
./gradlew assembleDebug
```

The output binary will be generated at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

---

<div align="center">
  <sub>Mahva Gallery · Atelier Precision & Craftsmanship</sub>
</div>
