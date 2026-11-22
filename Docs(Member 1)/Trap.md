# `Trap.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Trap.java`
- **Type:** `public final class Trap extends GameObject implements Damaging`
- **Author:** Ali Hasanov *(starter class created for `ObjectFactory` ID `4`; Member 3 owns obstacle behavior)*

## What This File Does

`Trap` is a static damaging tile (such as spikes or lava, created by `ObjectFactory` for map type ID `4`). Walking onto a `Trap` removes 1 life on contact entry (handled by `CollisionResolver`).

## Constructor & Method

- `public Trap(int x, int y)` — creates a trap at `(x, y)`.
- `getDamageAmount() : int` — returns `1`.

## How Teammates Use It

- **Member 2 (Map Loader):** Map ID `4` (`ObjectFactory.TRAP_ID`) creates a `Trap`.
- **Member 3 (Obstacles & AI):** Owns the static trap obstacle requirement and can extend or refine `Trap` as needed.
- **Member 4 (Renderer):** Check `tile instanceof Trap` when drawing tiles to render spike/lava sprites.
