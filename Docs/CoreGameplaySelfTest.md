# `CoreGameplaySelfTest.java`

- **Package:** `com.mazerunner`
- **File:** `core/src/test/java/com/mazerunner/CoreGameplaySelfTest.java`
- **Type:** `public final class CoreGameplaySelfTest`

## What This File Does

`CoreGameplaySelfTest` is a standalone, pure-Java test runner (`public static void main(String[] args)`) that tests all core gameplay rules without opening a window. It is wired to the Gradle task `./gradlew coreGameplaySmokeTest` in `core/build.gradle`.

## How to Run It

```bash
./gradlew coreGameplaySmokeTest
```

Expected output when all checks pass:

```text
All core gameplay checks passed.
```

## The 8 Test Methods Inside This File

1. **`testFactoryIds()`** — verifies `ObjectFactory.create(id, 0, 0)` returns `FreePath` (`0`), `Wall` (`1`), `Entry` (`2`), `Exit` (`3`), `Trap` (`4`), and `Enemy` (`5`), and throws `IllegalArgumentException` for invalid ID `6`.
2. **`testWallBlocksPlayer()`** — verifies the player cannot walk into a `Wall` cell.
3. **`testExitNeedsKeyAndKeyWins()`** — verifies a locked `Exit` blocks the player like a wall, picking up a `Key` unlocks it, and walking onto the unlocked `Exit` switches state to `GameState.VICTORY` and awards points.
4. **`testTrapContactDoesNotDrainLivesEveryFrame()`** — verifies stepping onto a `Trap` removes 1 life (`3 -> 2`), standing still on that trap for `2.0s` (past the `1.0s` cooldown) does not remove another life, and stepping off and back on removes another life (`2 -> 1`).
5. **`testGameOverAfterThreeLogicalHits()`** — verifies 3 separate hazard hits reduce lives to `0` and switch the world to `GameState.GAME_OVER`.
6. **`testPowerUpsAndExtraLife()`** — verifies `ExtraLife` increases lives (`3 -> 4`) and `PowerUp(PowerUpType.SHIELD, 2.0f)` activates `player.hasShield()`.
7. **`testResetRestoresNewGameState()`** — verifies `world.resetForNewGame()` restores `GameState.RUNNING`, moves the player back to `Entry`, restores `3` lives, clears keys and score, and respawns collected items.
8. **`testEventBusSubscription()`** — verifies `EventBus.subscribe(...)` delivers events to listeners and `subscription.close()` stops delivery.

## What Teammates Need to Know

- **Member 3 (Test Lead) & All Teammates:** Run `./gradlew coreGameplaySmokeTest` before pushing or opening a pull request to make sure no core gameplay rules were broken.
