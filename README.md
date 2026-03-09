# 🌸 Cozy Habits

A minimal, aesthetic habit tracker built with Kotlin & Jetpack Compose. Track your daily habits, keep a mood diary, unlock cute pets, and earn badges — all wrapped in a cozy matcha & baby pink design.

---

## ✨ Features

-  **Animated pets** that react to your daily progress — unlock new ones as your streak grows
-  **Habit tracking** with subtasks, progress bars, and swipe-to-delete
-  **Inline editing** of habits and subtasks
-  **Weekly calendar** on the home screen showing your completion history
-  **Mood diary** with emoji moods, saved persistently with Room
-  **Badges & achievements** unlocked by streak milestones
-  **Stats screen** with daily progress and motivational messages
-  **Streak tracking** displayed as a badge on the home screen
-  **Dark mode** toggle
-  **Customizable home shortcuts** — choose which screens appear as quick access buttons
-  **Lottie celebration animations** when you complete a habit

---

## 📸 Screenshots

> Coming soon!

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM |
| Database | Room |
| DI | Hilt |
| Preferences | DataStore |
| Animations | Lottie |
| Navigation | Navigation Compose |
| Async | Coroutines + Flow |

---

## 🗂 Project Structure

```
app/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAOs
│   │   ├── entity/       # Room entities
│   │   └── HabitDatabase.kt
│   ├── preferences/
│   │   ├── PetPreferences.kt   # DataStore
│   │   └── PetConfig.kt        # Pet definitions
│   └── repository/       # Repositories
├── di/
│   └── AppModule.kt      # Hilt module
├── ui/
│   ├── components/       # Reusable composables
│   ├── screens/          # App screens
│   ├── theme/            # Colors, typography, shapes
│   ├── viewmodel/        # ViewModels
│   └── MainApp.kt        # Navigation host
└── MainActivity.kt
```

---

## 🐾 Pets

Pets react to your daily progress and can be unlocked by maintaining streaks:

| Pet | Unlock requirement |
|---|---|
| 🌸 Kawaii | Available from the start |
| 🐶 Doggo | 7 day streak |

More pets coming soon!

---

## 🏆 Badges

| Badge | Requirement |
|---|---|
| 🌱 First Step | 1 day streak |
| ✨ 7 Day Star | 7 day streak |
| 🌸 2 Week Bloom | 14 day streak |
| 🍵 Matcha Master | 21 day streak |
| 🔥 On Fire | 30 day streak |
| 🌿 Nature Lover | 50 day streak |
| 👑 Habit Queen | 100 day streak |

---

## 🚀 Getting Started

### Requirements

- Android Studio Hedgehog or later
- Android SDK 35
- Gradle 8.7
- AGP 8.6.0

### Setup

1. Clone the repo:
```bash
git clone https://github.com/saraardila/habit-tracker-compose.git
```

2. Open in Android Studio

3. Add your Lottie animation files to `app/src/main/res/raw/`:
   - `kawaii_cry.json`
   - `kawaii_hi.json`
   - `kawaii_star.json`
   - `kawaii_love.json`
   - `angry_dog.json`
   - `happy_dog.json`
   - `happyunicorn_dog.json`
   - `astro_dog.json`
   - `celebration.json`

   > You can find free Lottie animations at [lottiefiles.com](https://lottiefiles.com)

4. Build and run on your device or emulator (API 26+)

---

## 🎨 Design

The app uses a cozy aesthetic palette:

| Name | Color |
|---|---|
| Matcha | `#8FAF7E` |
| Matcha Light | `#D4E6C3` |
| Matcha Dark | `#5C7A4E` |
| Baby Pink | `#FFC8D8` |
| Baby Pink Light | `#FFF0F5` |
| Cream White | `#FAF7F2` |

---

## 📋 Roadmap

- [ ] Calendar connected to real completion data
- [ ] Daily notifications / reminders
- [ ] Improved stats with charts
- [ ] Home screen widget
- [ ] More pets and badges
- [ ] Cloud backup

---

## 📄 License

```
MIT License — feel free to use, modify and share 🌸
```

---

<p align="center">Made with 🍵 and 🌸</p>
