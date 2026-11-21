# `Collidable.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Collidable.java`
- **Type:** `public interface Collidable`

## What This File Does

`Collidable` defines the basic collision check interface for maze entities. `GameObject` implements `Collidable` so every object in the game can check whether it occupies the same grid cell as another `GameObject`.

## Method

- `collidesWith(GameObject other) : boolean` — returns `true` when both objects occupy the same `(x, y)` maze cell.

## What Teammates Need to Know

- Because `GameObject` already implements `collidesWith(GameObject other)` as a `final` method comparing `x == other.x && y == other.y`, subclasses automatically inherit same-cell collision detection without extra code.
