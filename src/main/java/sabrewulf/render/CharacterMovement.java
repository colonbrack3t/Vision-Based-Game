package sabrewulf.render;

/**
 * Simple enum that lists all the supported movements in the
 * {@link MovementDecoder}. Ultimately, this is used to represent which
 * animation should be played by the {@link Animator}.
 */

public enum CharacterMovement {
    IDLE,
    WALKING,
    JUMPING,
    FALLING;
}
