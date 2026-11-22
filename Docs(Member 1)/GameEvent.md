# `GameEvent.java`

- **Package:** `com.mazerunner.events`
- **File:** `core/src/main/java/com/mazerunner/events/GameEvent.java`
- **Type:** `public interface GameEvent` (with 8 nested `record` types)
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GameEvent` is the base interface for all gameplay events published on `EventBus`. All 8 concrete event payloads are declared as immutable Java `record` types inside `GameEvent`.

## All 8 Event Records

| Event Record | Fields / Accessors | Published By | When It Fires |
|---|---|---|---|
| `GameEvent.LifeLostEvent` | `String source()`<br>`GridPosition position()`<br>`int livesRemaining()`<br>`int livesLost()` | `GameWorld.damagePlayer(...)` | Player takes damage from a hazard. |
| `GameEvent.KeyCollectedEvent` | `GridPosition position()`<br>`int totalKeys()` | `Key.collect(...)` | Player collects a `Key`. |
| `GameEvent.ScoreChangedEvent` | `int newScore()`<br>`int pointsAdded()`<br>`String reason()` | `GameWorld.addScore(...)` | Score changes (key, extra life, power-up, or victory bonus). |
| `GameEvent.ExtraLifeCollectedEvent` | `int livesNow()`<br>`boolean lifeWasAdded()` | `ExtraLife.collect(...)` | Player collects an `ExtraLife`. |
| `GameEvent.PowerUpCollectedEvent` | `PowerUpType type()`<br>`float durationSeconds()` | `PowerUp.collect(...)` | Player collects a `SPEED_BOOST` or `SHIELD` power-up. |
| `GameEvent.VictoryEvent` | `int finalScore()`<br>`float completionTimeSeconds()`<br>`int livesRemaining()` | `GameWorld.finishVictory()` | Player reaches an unlocked `Exit`. |
| `GameEvent.GameOverEvent` | `int finalScore()` | `GameWorld.damagePlayer(...)` | Player loses their last life (`lives <= 0`). |
| `GameEvent.NewGameStartedEvent` | *(none)* | `GameWorld.resetForNewGame()` | Game is reset for a new run. |

## How Teammates Use It

- **Member 4 (Audio & VFX):** Maps directly to the 4 required sound effects (`LifeLostEvent`, `KeyCollectedEvent`, `VictoryEvent`, `GameOverEvent`) plus damage flash/shake VFX.
- **Member 5 (Screens & HUD):** Uses `VictoryEvent` and `GameOverEvent` to show final score and completion time.
- **Member 2 (Achievements):** Listens to these events to unlock achievements (e.g., no-hit run via `VictoryEvent.livesRemaining()`).
