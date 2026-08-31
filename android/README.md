# Mahva Gallery (مهوا گالری) - Android App

Native Android application for the Mahva Gallery Gold Calculator built with **Kotlin** and **Jetpack Compose**.

---

## ✨ Features

- **100% Native Jetpack Compose**: 120Hz smooth scrolling, zero webview latency, native Android ripple effects, and seamless keyboard transitions.
- **RTL & Persian Support**: Built for Right-to-Left layout with full Persian/Arabic numeral normalization and Persian currency formatting (`تومان`, `گرم`, `درصد`, `%`).
- **Live Currency Formatter**: Auto-formats numbers with commas (e.g. `3,500,000`) live as typed.
- **Formulas & Parity**:
  - Raw Price: $C = A \times B$
  - Ojrat Amount: $E = \frac{D \times C}{100}$
  - Profit Amount: $G = \frac{F \times E}{100}$
  - Tax Amount: $I = \frac{(E + G) \times H}{100}$
  - Total Price: $K = C + E + G + I$
  - Final Percentage: $J = \left(\frac{K}{C} - 1\right) \times 100$
- **Smart Persistence**: Ojrat ($D$), Profit ($F$), and Tax ($H$) default percentages are automatically saved in `SharedPreferences` and restored on app launch.
- **Recalculate Action**: "محاسبه مجدد" clears raw price and weight while retaining default percentages.
- **Branded Launcher Icon**: Custom app icon generated from `logo.png`.

---

## 🚀 How to Build APK without Android Studio (Zero Local Setup)

This repository includes a pre-configured **GitHub Actions workflow** (`.github/workflows/build-apk.yml`).

### Step 1: Push to GitHub
Push your repository to GitHub:
```bash
git add .
git commit -m "Add native Android app with Jetpack Compose"
git push origin main
```

### Step 2: Download the APK
1. Open your repository on GitHub in your browser or phone.
2. Go to the **Actions** tab.
3. Click the latest **Build Android APK** run.
4. Under the **Artifacts** section at the bottom, click `mahva-gallery-debug-apk` to download `app-debug.apk`.
5. Install `app-debug.apk` directly on your Android phone!

---

## 🛠 Local Build (Optional)

If you have JDK 17 installed:
```bash
cd android
./gradlew assembleDebug
```
The compiled APK will be located at:
`android/app/build/outputs/apk/debug/app-debug.apk`
