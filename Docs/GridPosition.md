# `GridPosition.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/GridPosition.java`
- **Type:** `public record GridPosition(int x, int y)`

## What This File Does

`GridPosition` is an immutable Java `record` representing a cell coordinate `(x, y)` in the maze grid (measured in grid cells, not pixels). Because it is a `record`, Java automatically provides `x()`, `y()`, `equals(...)`, `hashCode()`, and `toString()`.

## Components & Methods

- `x() : int` — horizontal grid column (`0` to `width - 1`).
- `y() : int` — vertical grid row (`0` to `height - 1`).
- `move(Direction direction) : GridPosition` — returns a new `GridPosition(x + direction.getDx(), y + direction.getDy())` one cell away in the given `Direction` (throws `IllegalArgumentException` if `direction == null`).

## What Teammates Need to Know

- **Member 2 (Map/Save):** Use `GridPosition` when checking coordinates or storing positions.
- **Member 3 (AI & Pathfinding):** Because `GridPosition` already implements value-based `equals` and `hashCode`, you can use it directly as a key in a `HashSet<GridPosition>` or `HashMap<GridPosition, ...>` for BFS or A* pathfinding, and step to neighbors with `pos.move(dir)`.
- **Member 4 (Graphics) & Member 5 (HUD):** Multiply `pos.x()` and `pos.y()` by your tile pixel size when drawing sprites or computing the direction arrow to the nearest exit.
