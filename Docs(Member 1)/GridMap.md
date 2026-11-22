# `GridMap.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/GridMap.java`
- **Type:** `public final class GridMap`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GridMap` stores the 2D array of static maze tiles (`cells[x][y]`) and the separate list of moving enemies (`enemies`). It also finds the `Entry` cell where the player spawns and the nearest `Exit` for the HUD arrow. Map file parsing and validation are handled outside this class by Member 2.

## Fields

- `width : int`, `height : int` — grid dimensions in cells (must be `>= 1`).
- `cells : GameObject[][]` — static terrain tiles indexed by `[x][y]`.
- `enemies : List<Enemy>` — moving enemies placed on the map.

## Constructor

- `public GridMap(int width, int height)` — throws `IllegalArgumentException` if `width < 1 || height < 1`; initializes every cell in `cells[x][y]` to `new FreePath(x, y)`.

## Methods

- `getWidth() : int` — returns map width in cells.
- `getHeight() : int` — returns map height in cells.
- `isInside(int x, int y) : boolean` — returns `true` when `0 <= x < width` and `0 <= y < height`.
- `getTileAt(int x, int y) : GameObject` — returns `cells[x][y]`, or `null` if `(x, y)` is outside the map.
- `setTile(GameObject object) : void` — throws `IllegalArgumentException` if `(object.getX(), object.getY())` is outside the map. If `object instanceof Enemy`, adds it to `enemies` and puts `new FreePath(x, y)` in `cells[x][y]`; otherwise stores `object` directly in `cells[x][y]`.
- `getEnemies() : List<Enemy>` — returns an unmodifiable list of enemies.
- `findEntry() : Entry` — scans the grid and returns the `Entry` tile (throws `IllegalStateException` if none exists).
- `findNearestExit(GridPosition from) : Exit` — scans the grid and returns the `Exit` with the smallest Manhattan distance `|x - from.x()| + |y - from.y()|` (or `null` if no exit exists).
- `resetEnemies() : void` — calls `enemy.resetForNewGame()` on every enemy in `enemies`.

## How Teammates Use It

- **Member 2 (Map Loader & Validator):** Create `new GridMap(width, height)`, populate tiles using `map.setTile(factory.create(id, x, y))`, and run your map validation / exit-reachability checks using `getWidth()`, `getHeight()`, `getTileAt(x, y)`, and `findEntry()`.
- **Member 3 (Enemy AI):** Query terrain using `isInside(x, y)`, `getTileAt(x, y)`, and `getEnemies()`.
- **Member 4 (Renderer):** Loop over `x` and `y` calling `getTileAt(x, y)` to render static tiles, then iterate over `getEnemies()` to render enemies.
- **Member 5 (HUD):** Call `map.findNearestExit(player.getPosition())` to calculate the direction for the exit arrow.
