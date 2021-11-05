package sabrewulf.render;

import sabrewulf.game.Color;
import sabrewulf.game.Vector;

/**
 * This class acts as a container that stores all the additional attributes that
 * the characters have. It is intended to be a part of {@link RenderInfo}.
 * <p>
 * The attributes include: the characters color, the characters health, the
 * characters velocity, and so on.
 */

public class CharacterMeta {

    private Color type;
    private boolean shouldFocus;
    private float vision;
    private long elapsedTime;
    private Vector velocity;
    private boolean onGround;

    /**
     * Creates a CharacterMeta containing all the attributes of the character.
     *
     * @param type the color of the character
     * @param shouldFocus true if the character is the one that is being
     *                    currently controlled by the user
     * @param vision the health (vision) of the character
     * @param elapsedTime the elapsed time since level started
     * @param velocity the velocity of the character
     * @param onGround true if the character is currently on the ground
     */
    public CharacterMeta(Color type, boolean shouldFocus, float vision, long elapsedTime, Vector velocity, boolean onGround) {
        this.type = type;
        this.shouldFocus = shouldFocus;
        this.vision = vision;
        this.elapsedTime = elapsedTime;
        this.velocity = velocity;
        this.onGround = onGround;
    }

    /** @return the elapsed time since level started */
    public long getRunningTime() {
        return elapsedTime;
    }

    /** @return the color of the character */
    public Color getType() {
        return type;
    }

    /**
     * @return true if the character is the one that is being currently
     * controlled by the user
     */
    public boolean shouldFocus() {
        return shouldFocus;
    }

    /** @return the health (vision) of the character */
    public float getVision() {
        return vision;
    }

    /** @return the velocity of the character */
    public Vector getVelocity() {
        return velocity;
    }

    /** @return true if the character is currently on the ground */
    public boolean isOnGround() {
        return onGround;
    }

}
