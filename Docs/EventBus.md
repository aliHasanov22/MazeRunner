# `EventBus.java`

- **Package:** `com.mazerunner.events`
- **File:** `core/src/main/java/com/mazerunner/events/EventBus.java`
- **Type:** `public final class EventBus` (with nested `public interface Subscription`)

## What This File Does

`EventBus` implements the **Mandatory Theme B publish/subscribe event system**. Instead of `GameWorld`, `Key`, `ExtraLife`, or `PowerUp` calling HUD, audio, or achievement code directly, they publish `GameEvent` objects onto `world.getEventBus()`. Other teammates subscribe to the event classes they care about.

## Fields & Methods

- `listeners : Map<Class<?>, List<Consumer<?>>>` — `HashMap` from event class to a `List` of registered listener callbacks.
- `<E> subscribe(Class<E> eventType, Consumer<E> listener) : Subscription` — registers `listener` for events of type `eventType` and returns an `EventBus.Subscription` handle. Calling `subscription.close()` unregisters that listener.
- `publish(Object event) : void` — delivers `event` to all listeners registered for `event.getClass()`.
- `clear() : void` — removes all listeners across all event types.
- `EventBus.Subscription`:
  - `close() : void` — unsubscribes the listener.

## Example for Teammates

```java
EventBus bus = world.getEventBus();

EventBus.Subscription sub = bus.subscribe(GameEvent.LifeLostEvent.class, e -> {
    // e.source(), e.position(), e.livesRemaining(), e.livesLost()
});

// When leaving or disposing a screen:
sub.close();
```

## What Teammates Need to Know

- **Member 2 (Achievements):** Subscribe to `KeyCollectedEvent`, `VictoryEvent`, `LifeLostEvent`, `ScoreChangedEvent`, etc. to unlock achievements from your achievement file without modifying `GameWorld`.
- **Member 4 (Graphics & Audio):** Subscribe to `LifeLostEvent` (damage flash/shake + life-lost sound), `KeyCollectedEvent` (key sound), `VictoryEvent` (victory sound), and `GameOverEvent` (game-over sound).
- **Member 5 (Screens & HUD):** Subscribe to `VictoryEvent` and `GameOverEvent` to switch screens or show end-of-game overlays, and always call `subscription.close()` when disposing a screen so old listeners don't pile up.
