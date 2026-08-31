<div align="center">

<img src="pwa/logo-dark.png" alt="Mahva Gallery" width="105" />

# 𓋞 GOLD · CALC 𓋞
### *the alchemist's gold valuation & ledger engine*

```txt
  ┌─────────────────────────────────────────────────────────────┐
  │  𓊪  𓋹  𓋞  𓎛  𓏏   [ 𓍯 MAHVA · PRECISION · ALCHEMY ]   𓊵  𓋞  𓋹  │
  └─────────────────────────────────────────────────────────────┘
```

<p align="center">
  <img src="https://img.shields.io/badge/Android-120Hz%20Compose-172051?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Web-Offline%20PWA-64748B?style=flat-square&logo=pwa&logoColor=white" alt="PWA" />
  <img src="https://img.shields.io/badge/Forge-Cloud%20CI-172051?style=flat-square&logo=githubactions&logoColor=white" alt="CI" />
  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
</p>

> Built for gold appraisers, boutique jewellers, and bullion traders.  
> Zero bloated frameworks. Pure math, instant keystroke reactions, and cold-boot state persistence.

---

</div>

### ⌬ Architecture

```
├── android/    ⌁  Pure Kotlin · Jetpack Compose · Material 3 · 120Hz native runtime
├── pwa/        ⌁  Zero-dependency standalone web client with LocalStorage state
└── .github/    ⌁  Headless CI/CD APK forge (compiles APK in ~45s on GitHub)
```

---

### ⚗ The Alchemy (Valuation Formulas)

| Symbol | Parameter | Formula | Unit |
| :---: | :--- | :--- | :---: |
| **$A$** | Raw Gold Price (Per Gram) | *Keypad Input* | Toman |
| **$B$** | Gold Weight | *Keypad Input* | Grams |
| **$C$** | Raw Gold Value | $$C = A \times B$$ | Toman |
| **$D$** | Making Charge / Labor Rate | *Default / Input* | $\%$ |
| **$E$** | Making Charge Amount | $$E = \frac{D \times C}{100}$$ | Toman |
| **$F$** | Profit Margin Rate | *Default / Input* | $\%$ |
| **$G$** | Profit Amount | $$G = \frac{F \times E}{100}$$ | Toman |
| **$H$** | Tax Rate | *Default / Input* | $\%$ |
| **$I$** | Tax Amount | $$I = \frac{(E + G) \times H}{100}$$ | Toman |
| **$K$** | Total Final Price | $$K = C + E + G + I$$ | Toman |
| **$J$** | Net Effective Markup | $$J = \left(\frac{K}{C} - 1\right) \times 100$$ | $\%$ |

---

### 𓏲 Key Perks

- **𓋞 120Hz Compose Engine** — Silky smooth scrolling, native Android ripple effects, and live comma grouping while typing (`3,500,000`).
- **𓋹 Smart Memory** — Your favorite default percentages (`D`, `F`, `H`) automatically stick in `SharedPreferences` across cold boots.
- **𓊝 Zero-Studio Cloud Forge** — Push code $\rightarrow$ GitHub Actions spits out a ready-to-install `app-debug.apk` directly to your phone.

---

### ⟁ Forge / Run

```bash
# 1. Cloud Build (Zero local disk footprint)
git push origin main
# ↳ Grab 'mahva-gallery-debug-apk' from GitHub Actions artifacts tab

# 2. Local Build (If you have JDK 17 on path)
cd android && ./gradlew assembleDebug
```

---

<div align="center">
𓁹 <i>Crafted for Mahva Gallery · 𓋞 nebu (gold) precision</i> 𓁹
</div>
