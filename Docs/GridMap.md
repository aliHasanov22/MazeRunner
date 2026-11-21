# `GridMap.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/GridMap.java`
- **Type:** `public final class GridMap`

## What This File Does

`GridMap` stores the 2D maze grid (`GameObject[][] cells`) and the list of moving enemies (`List<Enemy> enemies`). It also validates the core map rules (one entry, at least one exit on the outer border, and a walkable path from entry to an exit using BFS) and finds the nearest exit for the HUD compass arrow.

## Fields & Constructor

- `width : int`, `height : int` — grid dimensions in cells (must be `>= 1`).
- `cells : GameObject[][]` — static terrain tiles at `[x][y]`.
- `enemies : List<Enemy>` — moving enemies placed on the map.
- `GridMap(int width, int height)` — creates the grid and fills every cell with `new FreePath(x, y)` by default.

## Key Methods

- `getWidth()` / `getHeight()` — returns map dimensions in cells.
- `isInside(int x, int y)` — returns `true` if `(x, y)` is inside `0..width-1` and `0..height-1`.
- `getTileAt(int x, int y)` — returns the terrain `GameObject` at `(x, y)`, or `null` if out of bounds.
- `setTile(GameObject object)` — places an object at `(object.getX(), object.getY())`:
  - If `object` is an `Enemy`, it adds the enemy to `enemies` and leaves a `FreePath(x, y)` tile in `cells[x][y]` (replacing any previous enemy at that cell).
  - Otherwise, it stores `object` directly in `cells[x][y]`.
- `getEnemies()` — returns an unmodifiable list of all enemies on the map.
- `getAllObjects()` — returns all terrain tiles followed by all enemies.
- `findEntry()` — finds and returns the single `Entry` tile (throws `IllegalStateException` if there are `0` or `> 1` entries).
- `getExits()` — returns all `Exit` tiles in the map.
- `findNearestExit(GridPosition from)` — returns the `Exit` with the smallest Manhattan distance `|exit.x - from.x| + |exit.y - from.y|`.
- `validate()` — checks all core map rules and throws `IllegalStateException` if any fail:
  1. Exactly one `Entry` exists.
  2. At least one `Exit` exists.
  3. Every `Exit` is on the outer border (`x == 0`, `y == 0`, `x == width - 1`, or `y == height - 1`).
  4. At least one `Exit` is reachable from `Entry` via 4-way BFS (`isExitReachable`).
- `isWalkable(int x, int y, boolean hasKey)` — returns `true` if `(x, y)` is inside the map and `!cells[x][y].blocksMovement(hasKey)`.
- `resetEnemies()` — calls `enemy.resetForNewGame()` on every enemy in `enemies`.

## What Teammates Need to Know

- **Member 2 (Map Parser & Validator):** After reading a `.properties` file and creating objects with `ObjectFactory`, put each object into `GridMap` via `map.setTile(obj)` and call `map.validate()`. Missing coordinates in a `.properties` file automatically default to `FreePath` because the constructor pre-fills the grid with `FreePath`.
- **Member 3 (Enemy AI):** Use `map.isInside(x, y)`, `map.isWalkable(x, y, false)`, and `map.getTileAt(x, y)` for AI line-of-sight or A* pathfinding.
- **Member 4 (Renderer):** Loop over `0..map.getWidth()-1` and `0..map.getHeight()-1` using `map.getTileAt(x, y)` to draw floor/wall/entry/exit/trap sprites, then loop over `map.getEnemies()` to draw enemies.
- **Member 5 (HUD):** Call `map.findNearestExit(player.getPosition())` to point the HUD arrow toward the closest exit.
