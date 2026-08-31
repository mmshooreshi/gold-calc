<div align="center">

<img src="pwa/logo.png" alt="Mahva Gallery" width="90" />

# MAHVA GALLERY
### 𓋞 Gold Valuation Engine 𓋞

<p align="center">
  <img src="https://img.shields.io/badge/Android-Jetpack%20Compose-172051?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Web-PWA-64748B?style=flat-square&logo=pwa&logoColor=white" alt="PWA" />
  <img src="https://img.shields.io/badge/CI-Cloud%20Build-172051?style=flat-square&logo=githubactions&logoColor=white" alt="CI" />
</p>

---

</div>

## ⟡ Platforms

| Target | Stack | Highlights |
| :--- | :--- | :--- |
| **Android** | Kotlin · Jetpack Compose | Native 120Hz RTL · Persian digit normalization · Auto-saving defaults |
| **PWA** | HTML5 · ES6 · CSS3 | Zero-dependency standalone web client · Embedded Vazirmatn typography |

---

## ⟡ Valuation Formulas

| Parameter | Term | Formula | Unit |
| :--- | :--- | :--- | :---: |
| **Raw Gold Price ($A$)** | قیمت طلای خام | *Input* | تومان |
| **Weight ($B$)** | وزن | *Input* | گرم |
| **Raw Price ($C$)** | قیمت خام | $$C = A \times B$$ | تومان |
| **Ojrat Rate ($D$)** | درصد اجرت | *Input* | $\%$ |
| **Ojrat Amount ($E$)** | مبلغ اجرت | $$E = \frac{D \times C}{100}$$ | تومان |
| **Profit Rate ($F$)** | درصد سود | *Input* | $\%$ |
| **Profit Amount ($G$)** | مبلغ سود | $$G = \frac{F \times E}{100}$$ | تومان |
| **Tax Rate ($H$)** | درصد مالیات | *Input* | $\%$ |
| **Tax Amount ($I$)** | مبلغ مالیات | $$I = \frac{(E + G) \times H}{100}$$ | تومان |
| **Total Price ($K$)** | قیمت کل | $$K = C + E + G + I$$ | تومان |
| **Final Markup ($J$)** | درصد نهایی | $$J = \left(\frac{K}{C} - 1\right) \times 100$$ | $\%$ |

---

## ⟡ Build

```bash
# Cloud APK (No Android Studio required)
git push origin main
# ↳ Download 'mahva-gallery-debug-apk' from GitHub Actions artifacts

# Local Build
cd android && ./gradlew assembleDebug
```

---

<div align="center">
  <sub>𓋞 &nbsp; Mahva Gallery &nbsp; 𓋞</sub>
</div>
