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

    /**
     * Registers a listener callback for a specific event class.
     *
     * @param <E>       the event type being subscribed to
     * @param eventType class object of the event type to listen for
     * @param listener  callback invoked whenever an event of {@code eventType} is published
     */
    public <E> void subscribe(Class<E> eventType, Consumer<E> listener) {
        listeners.putIfAbsent(eventType, new ArrayList<>());
        listeners.get(eventType).add(listener);
    }

    /**
     * Unregisters a previously added listener callback for a specific event class.
     *
     * @param <E>       the event type
     * @param eventType class object of the event type
     * @param listener  the callback to remove
     */
    public <E> void unsubscribe(Class<E> eventType, Consumer<E> listener) {
        List<Consumer<?>> list = listeners.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    /**
     * Delivers an event object to every listener currently subscribed to its runtime class.
     *
     * @param event the event instance to publish
     */
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
