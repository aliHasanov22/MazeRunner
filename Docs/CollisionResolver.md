# `CollisionResolver.java`

- **Package:** `com.mazerunner.collision`
- **File:** `core/src/main/java/com/mazerunner/collision/CollisionResolver.java`
- **Type:** `public final class CollisionResolver`

## What This File Does

`CollisionResolver` checks everything at the player's current grid cell during a tick:
1. Picks up any `Collectable` items (`Key`, `ExtraLife`, `PowerUp`) on that cell.
2. Checks if the player touched a `Damaging` hazard (`Trap` on the tile or any `Enemy` at that cell).
3. Enforces **contact-entry damage** and a **1.0-second hit cooldown** (`DEFAULT_HIT_COOLDOWN_SECONDS = 1.0f`) so a single collision only removes 1 life and standing still on a trap does not keep draining lives.
4. Triggers `world.finishVictory()` if the player is on an `Exit` tile while holding a key (`player.hasKey()`).

## Constants & Fields

- `DEFAULT_HIT_COOLDOWN_SECONDS = 1.0f` — default invulnerability window (1 second) after taking a hit.
- `hitCooldownSeconds : float` — configured cooldown duration.
- `cooldownRemaining : float` — remaining cooldown time in seconds.
- `activeDamageContacts : Set<Long>` — IDs of hazards currently overlapping the player.

## Constructors & Methods

- `CollisionResolver()` — uses `DEFAULT_HIT_COOLDOWN_SECONDS` (`1.0f`).
- `CollisionResolver(float hitCooldownSeconds)` — custom cooldown duration (must be `>= 0`).
- `update(float deltaSeconds)` — subtracts `deltaSeconds` from `cooldownRemaining` down to `0`.
- `resolve(GameWorld world)` — runs item collection, hazard damage check, and unlocked-exit victory check if `world.getState() == GameState.RUNNING`.
- `reset()` — resets `cooldownRemaining = 0` and clears `activeDamageContacts` when starting a new game.
- `getCooldownRemaining()` — returns `cooldownRemaining`.

## How Contact-Entry Damage Works

- Each `GameObject` has a unique `getId()`.
- When the player steps onto a `Trap` or collides with an `Enemy` whose ID was **not** in `activeDamageContacts` on the previous check (and `!player.hasShield()` and `cooldownRemaining <= 0`), `CollisionResolver` calls `world.damagePlayer(...)` and starts the `1.0s` cooldown.
- On every check, `activeDamageContacts` is updated to the set of hazards currently overlapping the player. Because staying on the same tile keeps that hazard ID in `activeDamageContacts`, standing still on a trap will never remove a second life.
- Once the player walks off the trap, its ID leaves `activeDamageContacts`, so stepping back onto it later counts as a new hit.

## What Teammates Need to Know

- **Member 3 (Obstacles & AI):** Any new invented obstacle that extends `GameObject` and implements `Damaging` will automatically work with `CollisionResolver`.
- **Member 4 (Graphics):** You can check `player.hasShield()` or subscribe to `LifeLostEvent` to trigger damage flash/shake visuals.
