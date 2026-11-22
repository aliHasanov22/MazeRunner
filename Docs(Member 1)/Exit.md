# `Exit.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Exit.java`
- **Type:** `public final class Exit extends GameObject`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Exit` represents an exit tile on the outer border of the maze (map type ID `3` in `ObjectFactory`). It behaves like a wall until the player collects a `Key`. Once unlocked, stepping onto an `Exit` triggers `GameWorld.finishVictory()`.

## Constructor & Method

- `public Exit(int x, int y)` — creates an exit tile at `(x, y)`.
- `blocksMovement(boolean hasKey) : boolean` — returns `!hasKey` (`true` while locked, `false` once the player has a key).

## How Teammates Use It

- **Member 2 (Map Loader & Validator):** Map ID `3` (`ObjectFactory.EXIT_ID`) creates an `Exit`. Validate that every map has at least one `Exit` on the outer border reachable from `Entry`.
- **Member 3 (Enemy AI):** `GameWorld.canEnemyEnter(...)` checks `tile.blocksMovement(false)`, so enemies cannot enter an `Exit` even after it unlocks.
- **Member 4 (Renderer):** Check `tile instanceof Exit` and `world.getPlayer().hasKey()` to draw locked vs. unlocked exit sprites.
- **Member 5 (HUD):** Use `world.getMap().findNearestExit(player.getPosition())` to aim the exit-direction arrow.
