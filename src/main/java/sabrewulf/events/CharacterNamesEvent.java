package sabrewulf.events;

import java.util.Map;

import sabrewulf.game.Color;

public class CharacterNamesEvent implements Event<Map<Color, String>> {

    /**
     *
     */
    private static final long serialVersionUID = -1074792335122599049L;
    private Map<Color, String> names;

    public CharacterNamesEvent(Map<Color, String> names) {
        this.names = names;
    }

    @Override
    public Map<Color, String> get() {
        return names;
    }

    public Map<Color, String> getNames() {
        return get();
    }

}