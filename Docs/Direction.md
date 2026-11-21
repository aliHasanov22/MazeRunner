# `Direction.java`

- **Package:** `com.mazerunner.input`
- **File:** `core/src/main/java/com/mazerunner/input/Direction.java`
- **Type:** `public enum Direction`

## What This File Does

`Direction` defines the 4 cardinal grid directions (`UP`, `DOWN`, `LEFT`, `RIGHT`) and their `(dx, dy)` step offsets on the maze grid.

## Enum Values & Offsets

| Direction | `getDx()` | `getDy()` | Grid Effect |
|---|---:|---:|---|
| `UP` | `0` | `+1` | Moves one row up (`y + 1`) |
| `DOWN` | `0` | `-1` | Moves one row down (`y - 1`) |
| `LEFT` | `-1` | `0` | Moves one column left (`x - 1`) |
| `RIGHT` | `+1` | `0` | Moves one column right (`x + 1`) |

## Methods

- `getDx() : int` — horizontal cell change (`-1`, `0`, or `+1`).
- `getDy() : int` — vertical cell change (`-1`, `0`, or `+1`).

## What Teammates Need to Know

- **Member 2 (Key Bindings Config) & Member 5 (Input):** Map keyboard keys (Arrow keys / WASD or custom key bindings from config) to `Direction.UP`, `Direction.DOWN`, `Direction.LEFT`, `Direction.RIGHT` (or `null` when no movement key is pressed) and pass that to `world.setMovement(direction, sprintHeld)`.
- **Member 3 (Enemy AI):** Iterate over `Direction.values()` and call `position.move(dir)` to inspect all 4 neighboring cells around an enemy.
