package sabrewulf.render;

import sabrewulf.game.Vector;

/**
 * This class represents a single particle in a {@link ParticleSystem}.
 */

public class Particle {

    private Vector position;
    private Vector velocity;

    private Texture texture;

    /**
     * Create a particle
     * @param position the particles position
     * @param velocity the particles velocity
     * @param texture the particles texture
     */
    public Particle(Vector position, Vector velocity, Texture texture) {
        this.position = position;
        this.velocity = velocity;
        this.texture = texture;
    }

    /** Update the particles position. Should be called every frame */
    public void update() {
        position = Vector.add(position, velocity);
    }

    /** Get the particles position */
    public Vector getPosition() {
        return position;
    }

    /** Set the particles position */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /** Get the particles texture */
    public Texture getTexture() {
        return texture;
    }

}
