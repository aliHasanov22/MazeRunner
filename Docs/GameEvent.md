# `GameEvent.java`

- **Package:** `com.mazerunner.events`
- **File:** `core/src/main/java/com/mazerunner/events/GameEvent.java`
- **Type:** `public interface GameEvent` (with 8 nested `record` classes)

## What This File Does

`GameEvent` is the marker interface for all events published by the core gameplay module on `EventBus`. All 8 concrete event types are defined as immutable Java `record` classes inside `GameEvent`.

## The 8 Event Records

| Record Name | Fields / Accessor Methods | Published By | When It Fires |
|---|---|---|---|
| `GameEvent.LifeLostEvent` | `String source()`<br>`GridPosition position()`<br>`int livesRemaining()`<br>`int livesLost()` | `GameWorld.damagePlayer(...)` | Player loses $\ge 1$ life from a `Trap`, `Enemy`, or hazard. |
| `GameEvent.KeyCollectedEvent` | `GridPosition position()`<br>`int totalKeys()` | `Key.collect(...)` | Player picks up a `Key`. |
| `GameEvent.ScoreChangedEvent` | `int newScore()`<br>`int pointsAdded()`<br>`String reason()` | `GameWorld.addScore(...)` | Score increases (key, extra life, power-up, or victory bonus). |
| `GameEvent.ExtraLifeCollectedEvent` | `int livesNow()`<br>`boolean lifeWasAdded()` | `ExtraLife.collect(...)` | Player picks up an `ExtraLife` (`lifeWasAdded` is `false` if already at max 5 lives). |
| `GameEvent.PowerUpCollectedEvent` | `PowerUpType type()`<br>`float durationSeconds()` | `PowerUp.collect(...)` | Player picks up a `SPEED_BOOST` or `SHIELD` power-up. |
| `GameEvent.VictoryEvent` | `int finalScore()`<br>`float completionTimeSeconds()`<br>`int livesRemaining()` | `GameWorld.finishVictory()` | Player reaches an `Exit` while holding a key. |
| `GameEvent.GameOverEvent` | `int finalScore()` | `GameWorld.damagePlayer(...)` | Player loses their last life (`lives <= 0`). |
| `GameEvent.NewGameStartedEvent` | *(no fields)* | `GameWorld.resetForNewGame()` | Game is reset for a fresh run. |

## What Teammates Need to Know

- **Member 2 (Achievements):** Listen to these records on `world.geteventBus()` to check achievement conditions (e.g., winning without losing a life via `VictoryEvent.livesRemaining()`, or finishing under a target time via `VictoryEvent.completionTimeSeconds()`).
- **Member 4 (Audio & VFX):** The 4 required sound effects map directly to 4 events here:
  - Life lost SFX + damage VFX -> `GameEvent.LifeLostEvent`
  - Key collected SFX -> `GameEvent.KeyCollectedEvent`
  - Victory SFX -> `GameEvent.VictoryEvent`
  - Game over SFX -> `GameEvent.GameOverEvent`
- **Member 5 (Screens & HUD):** Use `VictoryEvent` and `GameOverEvent` to show final score and completion time on the Victory / Game Over screens.
