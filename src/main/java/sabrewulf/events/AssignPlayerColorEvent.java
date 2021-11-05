package sabrewulf.events;

import sabrewulf.game.Color;

public class AssignPlayerColorEvent implements Event<Color> {

    /** */
    private static final long serialVersionUID = 6980293858324972565L;

    private Color playerColor;

    public AssignPlayerColorEvent(Color playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public Color get() {
        return playerColor;
    }

    public Color getPlayerColor() {
        return get();
    }
}
