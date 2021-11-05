package sabrewulf.events;

import sabrewulf.game.Color;

public class SetClientColorEvent implements Event<Color> {

    /** */
    private static final long serialVersionUID = -2596985883313308176L;

    private Color color;

    public SetClientColorEvent(Color color) {
        this.color = color;
    }

    @Override
    public Color get() {
        return color;
    }

    public Color getColor() {
        return get();
    }
}
