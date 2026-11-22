# `GameState.java`

- **Package:** `com.mazerunner.core`
- **File:** `core/src/main/java/com/mazerunner/core/GameState.java`
- **Type:** `public enum GameState`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GameState` defines the 5 lifecycle states of a MazeRunner game session. `GameWorld` checks this enum on every frame to decide whether the simulation runs or stays frozen, and screens/audio check `world.getState()` to decide what to draw and play.

## Enum Values

| Value | Meaning |
|---|---|
| `RUNNING` | Active gameplay — timer, player movement, enemies, and collisions all update every tick. |
| `PAUSED` | Gameplay is paused (e.g., `Esc` pressed). Timers and movement are frozen and held input is cleared. |
| `VICTORY` | Player collected a key and reached an `Exit` tile. |
| `GAME_OVER` | Player's lives dropped to `0`. |
| `MENU` | Returned to the main menu from pause, victory, or game over. |

## State Transitions (Managed by `GameWorld`)

- `new GameWorld(map)` -> starts in `RUNNING`.
- `world.pause()` -> `RUNNING` to `PAUSED`.
- `world.resume()` -> `PAUSED` to `RUNNING`.
- `world.finishVictory()` -> `RUNNING` to `VICTORY`.
- `world.damagePlayer(...)` (when `lives <= 0`) -> `RUNNING` to `GAME_OVER`.
- `world.returnToMenu()` -> any state except `RUNNING` to `MENU`.
- `world.resetForNewGame()` -> any state to `RUNNING`.

## How Teammates Use It

- **Member 4 (Graphics & Audio):** Switch between the 3 music tracks (`gameplay`, `pause`, `menu`) based on `world.getState()`.
- **Member 5 (Screens & UI/HUD):** Call `world.pause()`, `world.resume()`, `world.returnToMenu()`, and inspect `world.getState()` to display the pause menu, victory screen, or game-over screen.
