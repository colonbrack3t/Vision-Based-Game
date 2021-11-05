package sabrewulf.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private Map<String, Set<EventListener>> channels;

    public EventBus() {
        channels = new HashMap<>();
    }

    public void trigger(Event event) {
        String channel = getChannel(event);
        for (EventListener listener : addChannel(channel)) {
            listener.notify(event);
        }
    }

    public void subscribe(Class<? extends Event> eventType, EventListener listener) {
        String channel = getChannel(eventType);
        var listeners = addChannel(channel);
        listeners.add(listener);
    }

    public void unsubscribe(Class<? extends Event> eventType, EventListener listener) {
        String channel = getChannel(eventType);
        channels.computeIfPresent(
                channel,
                (key, listeners) -> {
                    listeners.remove(listener);
                    return listeners;
                });
    }

    private String getChannel(Class<? extends Event> eventType) {
        return eventType.getName();
    }

    private String getChannel(Event event) {
        return getChannel(event.getClass());
    }

    /**
     * Adds a channel if it doesn't exist already
     *
     * @returns the listeners for the channel (which are empty if it was just created)
     */
    private Set<EventListener> addChannel(String channel) {
        return channels.computeIfAbsent(channel, c -> new HashSet<>());
    }
}
