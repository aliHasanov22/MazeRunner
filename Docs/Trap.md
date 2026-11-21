# `Trap.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Trap.java`
- **Type:** `public final class Trap extends GameObject implements Damaging`

## What This File Does

`Trap` is a static damaging tile (such as spikes or lava, created by `ObjectFactory` for map type ID `4`). The player can walk onto a `Trap` cell, and doing so removes 1 life on contact entry.

## Constructor & Method

- `Trap(int x, int y)` — creates a trap at `(x, y)`.
- `getDamageAmount() : int` — returns `1` (removes 1 life per logical hit).

## What Teammates Need to Know

- **Member 2 (Map Loader):** Map ID `4` (`ObjectFactory.TRAP_ID`) creates a `Trap`.
- **Member 3 (Obstacles & AI):** `Trap` is the required static obstacle kind. Because `CollisionResolver` tracks contact-entry IDs (`activeDamageContacts`), walking onto a `Trap` and standing still on it only removes 1 life; the player must step off and step back on (after the 1.0s cooldown) to take damage again.
- **Member 4 (Renderer):** Check `tile instanceof Trap` when rendering map cells to draw spikes/lava sprites.
