<div align="center">

<img src="pwa/logo-dark.png" alt="Mahva Gallery Emblem" width="110" />

# MAHVA GALLERY
### 𓋞 Gold Valuation & Atelier Pricing Engine 𓋞

<p align="center">
  <i>Bespoke precision calculation suite designed for fine jewelry ateliers and bullion trade.</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-172051?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Web-Progressive%20App-64748B?style=for-the-badge&logo=pwa&logoColor=white" alt="PWA" />
  <img src="https://img.shields.io/badge/CI%2FCD-Cloud%20Forge-172051?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions" />
  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
</p>

---

</div>

## ⟡ Overview

**Mahva Gallery Gold Calc** is a dual-platform valuation suite crafted to compute real-time gold pricing, artisanal labor fees (*Ojrat*), profit margins, and statutory taxation with instant mathematical precision.

Built with a native 120Hz Android Jetpack Compose engine alongside an offline-first Progressive Web Application.

---

## ⟡ Platform Architecture

| Platform | Technology Stack | Core Capabilities |
| :--- | :--- | :--- |
| **Android** | Kotlin · Jetpack Compose · Material 3 | 120Hz native rendering · Persian digit normalization (`۰-۹` ⇄ `0-9`) · Live currency comma grouping · SharedPreferences persistence |
| **PWA** | HTML5 · Modern ES6 · Vanilla CSS | Zero external runtime dependencies · Embedded Vazirmatn typography · LocalStorage defaults |
| **CI / CD** | GitHub Actions Workflow | Automated cloud APK compilation — generates ready-to-install `.apk` binaries without local Android Studio |

---

## ⟡ Directory Layout

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

## ⟡ Valuation Mathematics

Standardized pricing formulas conforming to Persian bullion and jewelry trade regulations:

| Symbol | Parameter | Persian Term | Formula | Unit |
| :---: | :--- | :--- | :--- | :---: |
| **$A$** | Raw Gold Price | قیمت طلای خام | *User Input* | تومان |
| **$B$** | Gold Weight | وزن | *User Input* | گرم |
| **$C$** | Raw Price | قیمت خام | $$C = A \times B$$ | تومان |
| **$D$** | Artisanal Labor Fee | درصد اجرت | *User Input* | $\%$ |
| **$E$** | Labor Amount | مبلغ اجرت | $$E = \frac{D \times C}{100}$$ | تومان |
| **$F$** | Trade Profit Rate | درصد سود | *User Input* | $\%$ |
| **$G$** | Profit Amount | مبلغ سود | $$G = \frac{F \times E}{100}$$ | تومان |
| **$H$** | Statutory Tax Rate | درصد مالیات | *User Input* | $\%$ |
| **$I$** | Tax Amount | مبلغ مالیات | $$I = \frac{(E + G) \times H}{100}$$ | تومان |
| **$K$** | Total Acquisition Price | قیمت کل | $$K = C + E + G + I$$ | تومان |
| **$J$** | Effective Markup Rate | درصد نهایی | $$J = \left(\frac{K}{C} - 1\right) \times 100$$ | $\%$ |

---

## ⟡ Getting Started

### 1. Cloud Build (Zero Local Setup)
This repository compiles `.apk` binaries automatically via GitHub Actions:
1. Push any commit to the `main` branch.
2. Navigate to the repository's **Actions** tab $\rightarrow$ select **Build Android APK**.
3. Download the signed artifact **`mahva-gallery-debug-apk`**.

### 2. Local Compilation
To compile the Android package locally via the Gradle wrapper:

```bash
cd android
./gradlew assembleDebug
```

The compiled binary will be located at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

---

<div align="center">
  <sub>𓋞 &nbsp; Mahva Gallery · Precision Gold Valuation & Atelier Engineering &nbsp; 𓋞</sub>
</div>
