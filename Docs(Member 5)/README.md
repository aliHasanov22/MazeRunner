# MazeRunner - Screens, Menus and HUD (Member 5)

This part shows the game to the player: the main menu, pause, map selection, the game screen with the HUD, and the victory / game over screen. It runs the real `GameWorld` from the core gameplay part.

## Files

```text
core/src/main/java/com/mazerunner/
├── MazeRunnerGame.java      Game class, the only place that switches screens
├── GameSession.java         what the game screen needs from a running level
├── WorldSession.java        GameSession built on GameWorld (input, simple drawing)
├── DemoLevel.java           built-in level made with ObjectFactory until maps can be loaded
├── screens/
│   ├── StageScreen.java     base class for menu screens (stage, buttons, input, resize)
│   ├── MenuScreen.java      main menu and pause menu: Continue, Load Map, Exit
│   ├── MapSelectScreen.java lists .properties files in assets/maps, or the demo level
│   ├── MapNames.java        "level-1" -> "Level 1"
│   ├── GameScreen.java      tick, draw, HUD, Esc to pause, victory / game over check
│   └── EndScreen.java       "You escaped!" or "Game Over", score, time, Play again, Main menu
└── hud/
    ├── Hud.java             lives, key, score, time, arrow to the nearest exit
    ├── HudInfo.java         values the HUD reads
    └── HudMath.java         time format and arrow angle
core/src/test/java/com/mazerunner/ScreensSelfTest.java
```

## Game flow

```text
start -> MenuScreen --Load Map--> MapSelectScreen --pick a map--> GameScreen
            ^   |                        |                          |    |
            |   +------Continue / Esc----|------------------------->+    |
            +-----------Esc (pause)------|--------------------------+    |
            +-----------Back / Esc-------+                               |
            +----------Main menu--------- EndScreen <---- won / lost ----+
```

The game screen is kept while the pause menu is open, so Continue returns to the same game.
`GameScreen.hide()` calls `GameWorld.pause()` and `show()` calls `GameWorld.resume()`.
Every other screen is disposed when we leave it.

## Connection to the other parts

| Part | Used from | How |
|------|-----------|-----|
| Member 1, `GameWorld` | `WorldSession` | `tick`, `setMovement`, `pause`, `resume`, `getState` (VICTORY / GAME_OVER), score, elapsed time |
| Member 1, `GridMap.findNearestExit` | `WorldSession.getNearestExit` | target of the HUD arrow |
| Member 1, `ObjectFactory` | `DemoLevel` | builds the demo level from type IDs 0-5 |
| Member 2, map parser | `MazeRunnerGame.createSession` | replace `DemoLevel.create()` with the loaded map |
| Member 2, maps folder scan | `MapSelectScreen.findMaps` | can be replaced by the shared scan method |
| Member 3, enemies and traps | inside `GameWorld` | lost lives show as empty hearts |
| Member 4, renderer | `WorldSession.render` / `resize` | replace the simple ShapeRenderer drawing |
| Member 4, `AudioManager` | `MenuScreen.show`, `GameScreen.show`, `EndScreen` | menu / pause / gameplay music, victory / game over sound |

## HUD

- Own `Stage` and `ScreenViewport`, so zooming the game camera does not move it.
- Size follows the window height: scale = height / 720, kept between 0.75 and 2.5.
- Hearts and arrow are drawn with `Pixmap`, so no extra image files are needed.
- Arrow angle = `atan2(dy, dx)` from the player to `findNearestExit`.

## Controls

| Key | Action |
|-----|--------|
| Arrows / WASD | move |
| Shift | sprint |
| Esc | pause menu, press again to continue |
| Enter / Esc | on the end screen: main menu |

## Test

```text
./gradlew screensSmokeTest
```

Checks time formatting, arrow angles, map names and that the demo level starts correctly.
