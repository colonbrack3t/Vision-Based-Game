package sabrewulf.render;

import java.util.HashMap;
import java.util.Map;
import sabrewulf.game.Vector;

/**
 * This class is responsible for displaying the character animations for a
 * single character. Multiple instances should be created to support multiple
 * characters.
 * <p>
 * It works by constructing multiple {@link TextureSet} internally, based on a
 * common root path. The root path should point to a folder with all the image
 * files for a single character (including a file prefix if the files have
 * one).
 * <p>
 * Different animations (e.g. walking, jumping) require different modes.
 * Therefore, two modes are supported for each animation: single-image and
 * multiple-image. In case of single-image mode, only one image is displayed. In
 * multiple-image, the images are swapped one after another to create the
 * illusion that the player is e.g. walking.
 * <p>
 * The animations currently supported are: walk, idle, jump, fall. In
 * single-image mode the files should be named accordingly, e.g. idle.png. In
 * multiple-image mode files should be named walk0.png, walk1.png, etc.
 * <p>
 * The animation to be played is determined by the characters velocity.
 */

public class Animator {

    public static final Vector HITBOX_OFFSET = new Vector(-23, 0);
    public static final long WALK_DISPLAY_DURATION = 36 * 1000 * 1000L;

    private Map<CharacterMovement, TextureSet> textureSets;
    private CharacterMovement animation = CharacterMovement.IDLE;

    private long lastUpdate;

    private Vector position = new Vector(0, 0);
    private Vector lastPosition = new Vector(0, 0);
    private MovementDecoder decoder;

    /**
     * Returns an initialised Animator instance for a single character based
     * on the rootPath. The root path should point to a folder with all the
     * image files for a single character (including a file prefix if the
     * files have one). Returned instance will support the following
     * animations: walk, idle, jump, fall.
     *
     * @param rootPath path to folder with all image files (with file prefix)
     * @return Animator instance with animations: walk, idle, jump, fall.
     */
    public static Animator create(String rootPath) {
        Animator animator = new Animator();
        animator.initialise(rootPath);
        return animator;
    }

    // Constructor is private, create should be used instead
    private Animator() {
        textureSets = new HashMap<>();
        decoder = new MovementDecoder();
    }

    /**
     * Responsible for loading the actual image files and storing them in
     * TextureSets. The animations loaded are: walk, idle, jump, fall. The walk
     * animation consists of multiple files, therefore they all need to be
     * loaded to create the illusion that the character is walking.
     * <p>
     * This method is automatically called in {@link #create(String)}
     *
     * @param rootPath path to folder with all image files (with file prefix)
     */
    private void initialise(String rootPath) {
        String[] files = new String[] {
                "walk0.png", "walk1.png", "walk2.png", "walk3.png",
                "walk4.png", "walk5.png", "walk6.png", "walk7.png"
        };

        textureSets.put(CharacterMovement.WALKING, new TextureSet(rootPath, files, WALK_DISPLAY_DURATION, true));
        textureSets.put(CharacterMovement.IDLE, new TextureSet(rootPath + "idle.png"));
        textureSets.put(CharacterMovement.JUMPING, new TextureSet(rootPath + "jump.png"));
        textureSets.put(CharacterMovement.FALLING, new TextureSet(rootPath + "fall.png"));

        startTimer();
    }

    /**
     * Starts the timer responsible for switching multiple-image animations.
     */
    private void startTimer() {
        lastUpdate = System.nanoTime();
    }

    /**
     * Responsible for drawing the animation at the characters position.
     * <p>
     * This method invokes {@link #update(Vector)} so that the animation is
     * switched as necessary to reflect what the character is currently doing
     * (walking, jumping, etc.) based on the characters velocity.
     *
     * @param character the {@link RenderInfo} object corresponding to the
     *                  character
     */
    public void draw(RenderInfo character) {
        setPosition(character.getPosition());

        Vector velocity = character.getCharacterMeta().getVelocity(); // calculateVelocity();
        update(velocity);

        Vector drawingPosition = Vector.add(position, HITBOX_OFFSET);

        if (decoder.isFacingRight()) {
            Sprite.draw(drawingPosition, getCurrentSet().getCurrent());
        } else {
            Sprite.drawMirrored(drawingPosition, getCurrentSet().getCurrent());
        }
    }

    /**
     * This method updates the animation based on what the character is
     * currently doing (walking, jumping, etc.) based on the characters
     * velocity. Additionally, in multiple-image mode (isAnimated) this
     * method swaps the images one after another to create the illusion that
     * the character is e.g. walking. Therefore, it should be called every
     * frame.
     *
     * @param velocity the velocity of the character
     */
    public void update(Vector velocity) {
        setAnimation(decoder.decode(velocity));

        if (getCurrentSet().isAnimated()) {
            long elapsed = System.nanoTime() - lastUpdate;

            if (elapsed > getCurrentSet().getDisplayDuration()) {
                getCurrentSet().next();
                startTimer();
            }
        }
    }

    /**
     * Sets the current animation (walk, idle, etc.). Additionally, checks
     * whether the animation is changed. If so, the animation timer is
     * restarted as the previous animation could have been in single-image
     * mode, and therefore the timer could have not received any resets for a
     * long time.
     *
     * @param animation enum, the current characters animation to set
     */
    public void setAnimation(CharacterMovement animation) {
        if (animation != this.animation) {
            if (this.animation == CharacterMovement.JUMPING && animation != CharacterMovement.FALLING)
                return;
            this.animation = animation;
            startTimer();
        }
    }

    /**
     * Retrieves the current {@link TextureSet} that should be displayed (in
     * single-image mode) or played back (in multiple-image mode).
     *
     * @return the TextureSet corresponding to the current animation.
     */
    public TextureSet getCurrentSet() {
        return textureSets.get(animation);
    }

    /**
     * Sets the position where the animations should be drawn on the screen.
     * This should the characters position.
     *
     * @param newPosition the position of the character
     */
    public void setPosition(Vector newPosition) {
        lastPosition = position;
        position = newPosition;
    }

    /**
     * This method is used when the current animation is determined by
     * character position only. The velocity is calculated from the current
     * position and the last position. When the current animation is
     * determined by the velocity, this method should not be called.
     *
     * @return the characters velocity based on current position and the last
     * position
     */
    public Vector calculateVelocity() {
        return Vector.subtract(position, lastPosition);
    }
}
