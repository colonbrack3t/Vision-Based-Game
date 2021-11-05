package sabrewulf.events;

import java.util.Map;

public class GameUpdateEvent implements Event<Map<String, Object>> {

    /** */
    private static final long serialVersionUID = 433533038212380962L;

    private Map<String, Object> updates;

    public GameUpdateEvent(Map<String, Object> updates) {
        this.updates = updates;
    }

    @Override
    public Map<String, Object> get() {
        return updates;
    }
}
