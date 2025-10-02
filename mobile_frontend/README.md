# Ocean Professional Tic Tac Toe (Android, Kotlin)

A modern Tic Tac Toe app styled with the Ocean Professional theme (blue & amber accents, minimalist UI, rounded corners, subtle gradients, and smooth feedback). Implemented with traditional Android Views and the Declarative Gradle DSL.

Features:
- Centered 3x3 board with tap-to-move tiles
- Current game status: "X's Turn", "O Wins!", "Draw!", etc.
- Restart button to reset board (scores persist)
- Score display for X and O at the top
- Subtle gradients, shadows, and rounded corners

Tech:
- Kotlin
- Android Views (Material Components)
- Declarative Gradle DSL

Build:
- ./gradlew build

Run on a connected device/emulator:
- ./gradlew :app:installDebug
- Launch "Tic Tac Toe" from the launcher

Notes:
- Min SDK 30, Compile SDK 34 (set in settings.gradle.dcl defaults)
- Uses Material Components for buttons