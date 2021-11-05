package sabrewulf.events;

import sabrewulf.game.Vector;

public class UpdateCharacterPosition implements Event<Vector> {

    /**
     *
     */
    private static final long serialVersionUID = -8081713667130223817L;
    private Vector position;

    public UpdateCharacterPosition(Vector position) {
        this.position = position;
    }

    public Vector get() {
        return position;
    }

    public Vector getPosition() {
        return get();
    }
}