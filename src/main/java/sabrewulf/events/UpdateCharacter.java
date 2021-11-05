package sabrewulf.events;

import sabrewulf.game.Vector;

public class UpdateCharacter implements Event<Vector> {

    /**
     *
     */
    private static final long serialVersionUID = -8081713667130223817L;
    private Vector vel;
    private Vector pos;

    public UpdateCharacter(Vector vel, Vector pos) {
        this.vel = vel;
        this.pos = pos;
    }

    public Vector get() {
        return null;
    }

    public Vector getPosition() {
        return pos;
    }

    public Vector getVelocity() {
        return vel;
    }
}