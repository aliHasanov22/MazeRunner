# `GridPosition.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/GridPosition.java`
- **Type:** `public record GridPosition(int x, int y)`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GridPosition` is an immutable value type holding a cell coordinate `(x, y)` in grid units (not screen pixels). Being a Java `record`, it automatically provides `x()`, `y()`, `equals(...)`, `hashCode()`, and `toString()`.

## Components & Method

- `x() : int` — horizontal cell index (`0` to `width - 1`).
- `y() : int` — vertical cell index (`0` to `height - 1`).
- `move(Direction direction) : GridPosition` — returns `new GridPosition(x + direction.getDx(), y + direction.getDy())`.

## How Teammates Use It

- **Member 2 (Map Validator & Save):** Use `GridPosition` in BFS reachability queues/sets or when saving entity coordinates.
- **Member 3 (AI & Pathfinding):** Use `GridPosition` as keys in `HashSet` / `HashMap` for BFS or A*, and step to neighbours via `pos.move(dir)`.
- **Member 4 (Graphics) & Member 5 (HUD):** Multiply `pos.x()` and `pos.y()` by your tile pixel size when drawing sprites or calculating the vector to the nearest exit.
