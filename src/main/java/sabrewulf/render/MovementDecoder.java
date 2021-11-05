package sabrewulf.render;

import sabrewulf.game.Vector;

/**
 * This class is responsible for decoding what the character is currently doing
 * (walking, falling, etc.) based on the velocity of the character.
 * <p>
 * Its meant to be used by the {@link Animator} to know which animation should
 * be playing right now.
 */

public class MovementDecoder {

    private Vector facing;

    /** By default, the character faces to the right. */
    public MovementDecoder() {
        this(new Vector(1, 0));
    }

    private MovementDecoder(Vector facing) {
        this.facing = facing;
    }

    /**
     * When passed velocity, decodes the characters movement and returns an enum
     * whether the character is IDLE, WALKING, etc.
     *
     * @param velocity characters velocity
     * @return the CharacterMovement (WALKING, IDLE, etc.)
     */
    public CharacterMovement decode(Vector velocity) {
        // if character has stopped moving, remember the direction he was facing
        if (velocity.x() != 0) {
            facing.setX(velocity.x());
        }

        //IDLE    -> Vector(zero, zero)
        //WALKING -> Vector(any, zero)
        //JUMPING -> Vector(any, positive)
        //FALLING -> Vector(any, negative)
        if (velocity.x() == 0 && velocity.y() == 0) {
            return CharacterMovement.IDLE;
        } else if (velocity.y() == 0) {
            return CharacterMovement.WALKING;
        } else if (velocity.y() > 0) {
            return CharacterMovement.JUMPING;
        } else if (velocity.y() < 0) {
            return CharacterMovement.FALLING;
        }

        // if none of the cases matched, return IDLE to prevent visual glitches
        return CharacterMovement.IDLE;
    }

    /**
     * Retrieves the direction the player was last facing. Useful when the player
     * has stopped moving, but we want to know which direction he was facing before
     * he stopped moving.
     *
     * @return the direction the player was last facing
     */
    public Vector getFacing() {
        return facing;
    }

    /**
     * Retrieves the direction the player was last facing
     * @return true if the player was facing right
     */
    public boolean isFacingRight() {
        return (facing.x() > 0);
    }
}
