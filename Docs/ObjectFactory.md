# `ObjectFactory.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/ObjectFactory.java`
- **Type:** `public final class ObjectFactory`

## What This File Does

`ObjectFactory` converts the integer type IDs (`0` through `5`) from `.properties` map files into their corresponding `GameObject` subclasses at coordinate `(x, y)`.

## Constants (Map Type IDs)

| Constant | ID Value | Created Class | Meaning in `.properties` Map |
|---|---:|---|---|
| `FREE_PATH_ID` | `0` | `FreePath(x, y)` | Walkable path tile |
| `WALL_ID` | `1` | `Wall(x, y)` | Solid wall tile |
| `ENTRY_ID` | `2` | `Entry(x, y)` | Player spawn point |
| `EXIT_ID` | `3` | `Exit(x, y)` | Locked border exit |
| `TRAP_ID` | `4` | `Trap(x, y)` | Static damaging trap |
| `ENEMY_ID` | `5` | `Enemy(x, y)` | Moving enemy |

## Method

- `create(int typeId, int x, int y) : GameObject` — returns a new instance of the class matching `typeId` at `(x, y)`, or throws `IllegalArgumentException("Unknown map object type ID: " + typeId)` if `typeId` is outside `0..5`.

## What Teammates Need to Know

- **Member 2 (Map Parser):** When parsing `x,y = typeId` lines from a `.properties` file, call `factory.create(typeId, x, y)` and pass the result to `map.setTile(...)`.
- **Member 3 (Obstacles):** If your invented obstacle is loaded from map files with a new ID (e.g., `6`), add a constant and `case` here with Member 1.
