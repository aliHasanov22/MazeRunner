# `Collidable.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Collidable.java`
- **Type:** `public interface Collidable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Collidable` defines the collision contract for maze objects. `GameObject` implements `Collidable`, so every entity in the game can be checked for same-cell overlap with any other `GameObject`.

## Method

- `collidesWith(GameObject other) : boolean` — returns `true` if both objects are on the same `(x, y)` grid cell.

## How Teammates Use It

- All subclasses of `GameObject` automatically inherit `collidesWith(GameObject other)` and work with `CollisionResolver` without having to re-implement collision math.
