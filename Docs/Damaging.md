# `Damaging.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Damaging.java`
- **Type:** `public interface Damaging`

## What This File Does

`Damaging` marks any maze object that removes player lives on contact (currently implemented by `Trap` and `Enemy`).

## Method

- `getDamageAmount() : int` — returns how many lives are removed by one logical hit (`1` for both `Trap` and `Enemy`).

## What Teammates Need to Know

- **Member 3 (Obstacles & AI):** Your invented obstacle class must implement `Damaging` (and extend `GameObject`) and return `1` from `getDamageAmount()` so `CollisionResolver` and `GameWorld.damagePlayer(...)` remove one life per hit.
