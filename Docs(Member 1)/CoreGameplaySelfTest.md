# `CoreGameplaySelfTest.java`

- **Package:** `com.mazerunner`
- **File:** `core/src/test/java/com/mazerunner/CoreGameplaySelfTest.java`
- **Type:** `public final class CoreGameplaySelfTest`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`CoreGameplaySelfTest` is a standalone, pure-Java test runner (`main(String[] args)`) that verifies all Member 1 core gameplay rules without opening a desktop window. It is hooked up to the Gradle task `coreGameplaySmokeTest` in `core/build.gradle`.

## How to Run It

```bash
./gradlew coreGameplaySmokeTest
```

Expected output:

```text
All core gameplay checks passed.
```

## Static Field & Methods

- `private static int eventCalls = 0` — tracks how many times the `EventBus` test listener was invoked.
- `public static void main(String[] args)` — runs every gameplay check in order and throws `AssertionError` if any fails:
  1. `ObjectFactory` IDs `0–5` create `FreePath`, `Wall`, `Entry`, `Exit`, `Trap`, and `Enemy`.
  2. `Wall` blocks player movement.
  3. Locked `Exit` blocks movement like a wall and keeps state `RUNNING`.
  4. Collecting a `Key` unlocks `Exit`, triggers `GameState.VICTORY`, and awards score (`>= 600`).
  5. `Trap` removes 1 life on entry (`3 -> 2`), standing still on the trap for `2.0s` does not remove extra lives, stepping off and back on removes another life (`2 -> 1`), and a third hit triggers `GameState.GAME_OVER` at `0` lives.
  6. `ExtraLife` increases lives (`3 -> 4`) and `PowerUp(SHIELD)` activates `player.hasShield()`.
  7. `resetForNewGame()` restores `RUNNING`, entry position `(0, 1)`, `3` lives, `0` score, clears shield, and restores collectables.
  8. `pause()` freezes `elapsedSeconds` during `tick(1.0f)`.
  9. `EventBus` delivers events after `subscribe(...)` and stops delivering after `unsubscribe(...)`.
- `private static void stepOffAndBack(GameWorld world)` — moves the player left off `(1, 1)` and back onto `(1, 1)`.
- `private static GridMap basicMap()` — builds a 4x3 test map with `Entry(0, 1)` and `Exit(3, 1)`.
- `private static void check(boolean condition, String message)` — throws `AssertionError(message)` when `condition` is `false`.

## How Teammates Use It

- **Member 3 (Test Lead) & All Teammates:** Run `./gradlew coreGameplaySmokeTest` before pushing changes to verify no core gameplay rule was broken.
