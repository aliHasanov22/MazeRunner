# `EventBus.java`

- **Package:** `com.mazerunner.events`
- **File:** `core/src/main/java/com/mazerunner/events/EventBus.java`
- **Type:** `public final class EventBus`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`EventBus` implements the mandatory publish/subscribe event system so gameplay classes (`GameWorld`, `Key`, `ExtraLife`, `PowerUp`) never call UI, audio, or achievement code directly.

## Field & Methods

- `private final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>()` — maps each event class to its registered listeners.
- `public <E> void subscribe(Class<E> eventType, Consumer<E> listener)` — registers `listener` for events of type `eventType`.
- `public <E> void unsubscribe(Class<E> eventType, Consumer<E> listener)` — removes a previously registered listener.
- `public void publish(Object event)` — copies the registered listener list for `event.getClass()` and invokes each listener with `event`.

## Usage Example for Teammates

```java
EventBus bus = world.getEventBus();

Consumer<GameEvent.LifeLostEvent> onHit = event -> {
    // trigger damage sound and screen flash
};

bus.subscribe(GameEvent.LifeLostEvent.class, onHit);

// Unsubscribe when closing or disposing a screen:
bus.unsubscribe(GameEvent.LifeLostEvent.class, onHit);
```

## How Teammates Use It

- **Member 2 (Achievements):** Subscribe to gameplay events to unlock achievements without editing `GameWorld`.
- **Member 4 (Graphics & Audio):** Subscribe to `LifeLostEvent`, `KeyCollectedEvent`, `VictoryEvent`, and `GameOverEvent` to trigger sound effects and damage VFX.
- **Member 5 (Screens & HUD):** Subscribe to `VictoryEvent` and `GameOverEvent` to switch screens, and unsubscribe in `dispose()`.
