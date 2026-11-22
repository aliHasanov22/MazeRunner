# `CollisionResolver.java`

- **Package:** `com.mazerunner.collision`
- **File:** `core/src/main/java/com/mazerunner/collision/CollisionResolver.java`
- **Type:** `public final class CollisionResolver`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`CollisionResolver` resolves everything colliding with the player's current grid cell during a tick:
1. Collects any overlapping `Collectable` items (`Key`, `ExtraLife`, `PowerUp`).
2. Detects overlapping `Damaging` hazards (the current terrain tile if `Damaging`, plus any `Enemy` on the same cell).
3. Deals damage **only on a new hazard contact** when the `1.0s` cooldown is finished so standing on a trap never drains multiple lives.
4. Triggers `world.finishVictory()` if the player is standing on an `Exit` tile while holding a key.

## Constants & Fields

- `public static final float HIT_COOLDOWN_SECONDS = 1.0f` — 1-second invulnerability window after taking a hit.
- `private float cooldownRemaining` — seconds remaining before another hit can deal damage.
- `private final Set<Long> touchingHazards` — IDs of hazards currently overlapping the player (used to tell a new contact apart from standing still on the same hazard).

## Methods

- `update(float deltaSeconds) : void` — decreases `cooldownRemaining` by `deltaSeconds`, clamped at `0`.
- `resolve(GameWorld world) : void` — runs only if `world.getState() == GameState.RUNNING`:
  1. Loops through `world.getCollectables()`, calls `((Collectable) item).collect(world, player)` on colliding items, and removes them via `world.removeCollectable(item)`.
  2. Builds a list containing the tile at `(player.getX(), player.getY())` and all enemies from `world.getMap().getEnemies()`.
  3. Tracks every colliding `Damaging` object's `getId()` in `nowTouching`, and picks the first one whose ID was **not** already in `touchingHazards` as `newHit`.
  4. Replaces `touchingHazards` with `nowTouching`.
  5. If `newHit != null && cooldownRemaining <= 0`, calls `world.damagePlayer(...)` and sets `cooldownRemaining = HIT_COOLDOWN_SECONDS`.
  6. If `tile instanceof Exit && player.hasKey()`, calls `world.finishVictory()`.
- `reset() : void` — sets `cooldownRemaining = 0` and clears `touchingHazards`.
- `getCooldownRemaining() : float` — returns `cooldownRemaining`.

## How Teammates Use It

- **Member 3 (Obstacles & AI):** Any invented obstacle extending `GameObject` and implementing `Damaging` works automatically with `CollisionResolver` once placed in the map tiles or enemy list.
- **Member 4 (Graphics & Audio):** Subscribe to `GameEvent.LifeLostEvent` on `EventBus` rather than polling `CollisionResolver` directly.
