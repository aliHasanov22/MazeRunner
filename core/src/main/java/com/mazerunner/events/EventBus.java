package com.mazerunner.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple publish/subscribe event bus. Gameplay code publishes events here
 * so it never has to call UI or audio code directly.
 *
 * @author Ali Hasanov
 */
public final class EventBus {

    private final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();

    /** Registers a listener for one event type. */
    public <E> void subscribe(Class<E> eventType, Consumer<E> listener) {
        listeners.putIfAbsent(eventType, new ArrayList<>());
        listeners.get(eventType).add(listener);
    }

    /** Removes a listener that was registered before. */
    public <E> void unsubscribe(Class<E> eventType, Consumer<E> listener) {
        List<Consumer<?>> list = listeners.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    /** Sends the event to every listener registered for its type. */
    @SuppressWarnings("unchecked")
    public void publish(Object event) {
        List<Consumer<?>> list = listeners.get(event.getClass());
        if (list == null) {
            return;
        }
        for (Consumer<?> listener : new ArrayList<>(list)) {
            ((Consumer<Object>) listener).accept(event);
        }
    }
}
