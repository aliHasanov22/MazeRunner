# `Exit.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Exit.java`
- **Type:** `public final class Exit extends GameObject`

## What This File Does

`Exit` is the maze exit cell (created by `ObjectFactory` for map type ID `3`). Every `Exit` must sit on the outer border of the map. It acts like a solid wall while locked (`!hasKey`), and becomes walkable once the player collects at least one `Key`. Stepping onto an unlocked `Exit` triggers `GameWorld.finishVictory()`.

## Constructor & Method

- `Exit(int x, int y)` — creates an exit at `(x, y)`.
- `blocksMovement(boolean hasKey) : boolean` — returns `!hasKey` (blocks movement when the player has no key; allows entry when `hasKey` is `true`).

## What Teammates Need to Know

- **Member 2 (Map Loader & Validator):** Map ID `3` (`ObjectFactory.exit_ID`) creates an `Exit`. Every map must have at least one `Exit`, all exits must be on the outer border, and at least one exit must be reachable from `Entry`.
- **Member 3 (Enemy AI):** `GameWorld.enemyaccess(...)` checks `tile.blocksMovement(false)`, so enemies are never allowed to step onto an `Exit` tile even after the player picks up the key.
- **Member 4 (Renderer):** Draw `Exit` with a distinct sprite (or locked vs. unlocked door sprite based on `world.getplayer().haskey()`).
- **Member 5 (HUD):** Use `world.getmap().findNearestExit(player.getPosition())` to point the HUD arrow toward the nearest `Exit`.
