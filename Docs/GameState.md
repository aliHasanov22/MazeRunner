# `GameState.java`

- **Package:** `com.mazerunner.core`
- **File:** `core/src/main/java/com/mazerunner/core/GameState.java`
- **Type:** `public enum GameState`

## What This File Does

`GameState` lists the 5 states a game session can be in. `GameWorld` uses this enum to decide whether the simulation should run or stay frozen, and UI/menu screens check it via `world.getState()` to know what screen or overlay to show.

## Enum Values

| Value | Meaning |
|---|---|
| `RUNNING` | Active gameplay. Timer, enemies, player movement, and collisions are updating every frame. |
| `PAUSED` | Gameplay is paused (for example when `Esc` is pressed). Movement and timers are frozen. |
| `VICTORY` | Player collected a key and stepped onto an `Exit` tile. |
| `GAME_OVER` | Player lost all lives (`lives <= 0`). |
| `MENU` | Player exited to the main menu from pause, victory, or game over. |

## How State Transitions Work in `GameWorld`

- **Start of game:** `new GameWorld(map)` starts in `RUNNING`.
- **`world.pause()`:** `RUNNING` -> `PAUSED` (also clears held player movement so the player doesn't keep moving on resume).
- **`world.resume()`:** `PAUSED` -> `RUNNING`.
- **`world.finishVictory()`:** `RUNNING` -> `VICTORY`.
- **`world.damagePlayer(...)` (when lives reach 0):** `RUNNING` -> `GAME_OVER`.
- **`world.returnToMenu()`:** `PAUSED`, `VICTORY`, or `GAME_OVER` -> `MENU` (does nothing if called directly from `RUNNING`).
- **`world.resetForNewGame()`:** any state -> `RUNNING`.

## What Teammates Need to Know

- **Member 4 (Graphics & Audio):** Check `world.getState()` (or listen to events) to switch background music between gameplay (`RUNNING`), pause (`PAUSED`), and menu (`MENU`).
- **Member 5 (Screens & UI/HUD):** Call `world.pause()` when `Esc` is pressed, `world.resume()` on *Continue*, `world.returnToMenu()` on *Return to Menu*, and check `world.getState()` to display the pause overlay, victory screen, or game-over screen.
