# AfriBooks — Student Textbook Marketplace
### MADA372 SS3 — Mobile Application Development

---

## Project Overview

**AfriBooks** is an Android MVP textbook marketplace application built for South African university students. It enables students to buy and sell second-hand textbooks in a culturally resonant, mobile-first experience inspired by African aesthetics.

---

## Screens & Features

| Screen | Features |
|---|---|
| **Splash** | Branded launch screen with AfriBooks logo |
| **Login** | Email/password validation, demo-mode login |
| **Register** | Username, email, password + confirm, full validation |
| **Home** | Search shortcut, Buy/Sell CTAs, Recent Listings RecyclerView |
| **Buy Books** | Live search, condition filter chips, full book listing |
| **Sell Book** | Form with title, course, price, condition chips, validation |
| **Book Detail** | Full book info, price, condition, seller, Save + Enquiry |
| **Enquiry / Contact** | Name, email, message form with validation |
| **Profile** | User info, menu items, logout with confirmation dialog |

---

## Tech Stack

- **Language:** Kotlin
- **UI:** XML layouts + Material Components 1.11.0
- **Architecture:** Fragment-based navigation with ViewBinding
- **Build System:** Gradle 8.2 / AGP 8.2.2
- **Min SDK:** 24 (Android 7.0) | Target SDK: 34 (Android 14)

---

## Key Android Concepts Demonstrated

- `AppCompatActivity` + `Fragment` lifecycle management
- `RecyclerView` with `ListAdapter` + `DiffUtil`
- `ViewBinding` throughout all activities and fragments
- `ConstraintLayout` + `LinearLayout` combined layouts
- `MaterialComponents` — `TextInputLayout`, `MaterialButton`, `CardView`, `BottomNavigationView`, `ChipGroup`, `Snackbar`, `AppBarLayout`
- Input validation with error messages on `TextInputLayout`
- `DiffUtil.ItemCallback` for efficient list updates
- `Serializable` data passing between activities via `Intent`
- `AlertDialog` for destructive action confirmation
- Custom drawable resources (vectors, shapes, selectors, layer-lists)
- Color state lists for BottomNav and Chips
- African-inspired colour palette (terracotta, gold, earth tones)

---

## African Cultural Design

The UI draws on African visual culture:
- **Terracotta & earth tones** inspired by West African clay architecture
- **Gold accents** (kente-strip bars) referencing Ghanaian kente cloth
- **Savanna cream** backgrounds echoing the African landscape
- App name **"Funda Africa"** — *funda* means "learn/read" in isiZulu/Xhosa

---

## Project Structure

```
app/src/main/
├── java/com/afribooks/app/
│   ├── adapter/         BookAdapter.kt
│   ├── data/
│   │   ├── model/       Book.kt, User.kt, BookCondition.kt
│   │   └── repository/  BookRepository.kt, UserRepository.kt
│   ├── ui/
│   │   ├── auth/        SplashActivity, LoginActivity, RegisterActivity
│   │   ├── home/        MainActivity, HomeFragment
│   │   ├── buy/         BuyFragment
│   │   ├── sell/        SellFragment
│   │   ├── profile/     ProfileFragment
│   │   ├── detail/      BookDetailActivity
│   │   └── enquiry/     EnquiryActivity
│   └── utils/           ValidationUtils.kt, Extensions.kt
└── res/
    ├── layout/          All XML layouts
    ├── values/          colors, strings, themes, dimens
    ├── drawable/        Vector icons, backgrounds, shapes
    ├── menu/            bottom_nav_menu.xml
    └── color/           Color state list selectors
```

---

## How to Run

1. Open in **Android Studio Hedgehog** (2023.1.1) or later
2. Wait for Gradle sync to complete
3. Run on emulator or device with **API 24+**
4. Use any email + password (6+ chars) to log in

---

## References

- Cheng, L.C. (2016). The mobile app usability inspection (MAUi) framework as a guide for minimal viable product (MVP) testing. *Proceedings of the 2nd International Conference in HCI and UX Indonesia 2016*, pp. 1–11.
- Lenarduzzi, V., et al. (2016). MVP explained: A systematic mapping study on the definitions of minimal viable product. *IEEE SEAA 2016*. DOI: 10.1109/SEAA.2016.56
- Android Developers. (2024). *Develop Android apps with Kotlin*. https://developer.android.com/kotlin
- Material Design. (2024). *Material Components for Android*. https://material.io/develop/android
