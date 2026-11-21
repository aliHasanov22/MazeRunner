package com.mazerunner.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple publish/subscribe event bus used by the gameplay layer to notify
 * UI, HUD, and audio classes without calling them directly.
 *
 * @author Ali Hasanov
 */
public final class EventBus {

    private final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();

    /**
     * Registers a listener for a specific event class and returns a Subscription
     * handle that can be used to unsubscribe later.
     */
    public <E> Subscription subscribe(Class<E> eventType, Consumer<E> listener) {
        if (eventType == null || listener == null) {
            throw new IllegalArgumentException("Event type and listener are required");
        }
        List<Consumer<?>> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            listeners.put(eventType, eventListeners);
        }
        eventListeners.add(listener);

        List<Consumer<?>> targetList = eventListeners;
        return new Subscription() {
            @Override
            public void close() {
                targetList.remove(listener);
            }
        };
    }

    /** Sends an event to all listeners registered for that event class. */
    @SuppressWarnings("unchecked")
    public void publish(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        List<Consumer<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) {
            return;
        }
        List<Consumer<?>> snapshot = new ArrayList<>(eventListeners);
        for (Consumer<?> rawListener : snapshot) {
            Consumer<Object> listener = (Consumer<Object>) rawListener;
            listener.accept(event);
        }
    }

    /** Removes all registered listeners. */
    public void clear() {
        listeners.clear();
    }

    /** Handle used to unregister an event listener. */
    public interface Subscription {
        /** Unregisters this listener from the event bus. */
        void close();
    }
}
