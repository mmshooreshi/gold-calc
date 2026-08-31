<div align="center">

<img src="pwa/logo-dark.png" alt="Mahva Gallery" width="96" />

# MAHVA GALLERY

**Precision Gold Valuation & Pricing Engine**

<p align="center">
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-172051?style=flat&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Web-Progressive%20App-172051?style=flat&logo=pwa&logoColor=white" alt="PWA" />
  <img src="https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-172051?style=flat&logo=githubactions&logoColor=white" alt="CI" />
  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat&logo=kotlin&logoColor=white" alt="Kotlin" />
</p>

---

</div>

## Overview

A dedicated valuation suite engineered for jewelry ateliers and bullion trading. Built with a 120Hz native Android (Jetpack Compose) application alongside an offline-first Progressive Web App.

---

## Valuation Matrix

| Symbol | Parameter | Computation | Unit |
| :---: | :--- | :--- | :---: |
| **$A$** | Raw Gold Price | *Input (Per Gram)* | Toman |
| **$B$** | Gold Weight | *Input* | Grams |
| **$C$** | Raw Gold Value | $$C = A \times B$$ | Toman |
| **$D$** | Making Charge Rate (*Ojrat*) | *Default / Input* | $\%$ |
| **$E$** | Making Charge Amount | $$E = \frac{D \times C}{100}$$ | Toman |
| **$F$** | Profit Margin Rate | *Default / Input* | $\%$ |
| **$G$** | Profit Amount | $$G = \frac{F \times E}{100}$$ | Toman |
| **$H$** | Tax Rate | *Default / Input* | $\%$ |
| **$I$** | Tax Amount | $$I = \frac{(E + G) \times H}{100}$$ | Toman |
| **$K$** | Total Final Price | $$K = C + E + G + I$$ | Toman |
| **$J$** | Effective Markup | $$J = \left(\frac{K}{C} - 1\right) \times 100$$ | $\%$ |

---

## Build & Installation

### Cloud APK (Zero Local Setup)
Push to GitHub to automatically trigger the build pipeline:
```bash
git push origin main
```
Download the compiled `app-debug.apk` directly from **GitHub Actions** $\rightarrow$ **Artifacts**.

### Local Build
```bash
cd android
./gradlew assembleDebug
```

---

<div align="center">
  <sub>Mahva Gallery · Fine Jewelry Valuation</sub>
</div>
