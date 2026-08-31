# 𓋞 gold-calc 𓋞
### *mahva gallery · precision jewelry & gold valuation engine*

```txt
  ┌──────────────────────────────────────────────────────────┐
  │  𓊪  𓋹  𓋞  𓎛  𓏏   [ 𓍯 MAHVA · GOLD · VALUATION ]   𓊵  𓋞  𓋹  │
  └──────────────────────────────────────────────────────────┘
```

> **Alchemist's ledger built for precision gold calculation.**  
> Zero bloat. Native 120Hz Compose runtime + standalone offline PWA.

---

### ⌬ architecture

```
├── android/    ⌁  Pure Kotlin · Jetpack Compose · Material 3 · RTL
├── pwa/        ⌁  Single-file offline web app (Vazirmatn + LocalStorage)
└── .github/    ⌁  Headless CI/CD APK forge (Zero-Studio cloud compiler)
```

---

### ⚗ the alchemy (formulas)

$$\begin{aligned}
\text{Raw Price } (C) &= A \times B \\
\text{Ojrat } (E) &= \frac{D \cdot C}{100} \\
\text{Profit } (G) &= \frac{F \cdot E}{100} \\
\text{Tax } (I) &= \frac{(E + G) \cdot H}{100} \\
\text{Total } (K) &= C + E + G + I \\
\text{Net Markup } (J) &= \left(\frac{K}{C} - 1\right) \times 100 \quad [\%]
\end{aligned}$$

---

### 𓏲 features

- **𓋞 Native Android (Kotlin / Compose)** — Edge-to-edge RTL layout with Persian digit normalization (`۰-۹` ⇄ `0-9`) & live comma currency splitting.
- **𓋹 State Memory** — Percentage defaults (`D`, `F`, `H`) persist across cold boots in `SharedPreferences`.
- **𓊝 Cloud APK Forge** — Push to `main` → GitHub Actions spits out ready-to-install `.apk` in ~60s. No Android Studio monster needed on disk.

---

### ⟁ forge

```bash
# cloud build
git push origin main
# ↳ grab 'mahva-gallery-debug-apk' from GitHub Actions artifacts

# local cli (if JDK 17 is on path)
cd android && ./gradlew assembleDebug
```

---

<div align="center">
𓁹 <i>Crafted for Mahva Gallery · 𓋞 nebu (gold) precision</i> 𓁹
</div>
