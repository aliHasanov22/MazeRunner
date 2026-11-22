# `Damaging.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Damaging.java`
- **Type:** `public interface Damaging`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Damaging` is the interface implemented by any hazard that removes player lives on contact (currently `Trap` and `Enemy`).

## Method

- `getDamageAmount() : int` — returns how many lives are removed from one logical hit (`1` for both `Trap` and `Enemy`).

## How Teammates Use It

- **Member 3 (Obstacles & AI):** Your invented obstacle should implement `Damaging` (and extend `GameObject`) and return `1` from `getDamageAmount()` so `CollisionResolver` and `GameWorld.damagePlayer(...)` remove 1 life on contact.
