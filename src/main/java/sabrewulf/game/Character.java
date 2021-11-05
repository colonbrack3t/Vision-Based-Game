package sabrewulf.game;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sabrewulf.render.CharacterMeta;
import sabrewulf.render.RenderInfo;
import sabrewulf.ui.SoundPlayer;

public class Character extends GameObject {

    private static final long serialVersionUID = 6981995918404162243L;

    static final Vector characterDimensions = new Vector(50, 95); // was 70 70
    private float vision = VISION_MAX, checkpointVision = VISION_MAX;
    private Vector checkpointLocation;
    private final float visionMin = 0, visionMax = VISION_MAX;
    private int left = 0, right = 0, up = 0;
    final int moveSpeed = 4;
    final int jumpSpeed = 500;
    private Vector gravity = new Vector(0, -1);

    private Set<Checkpoint> checkpointHistory;

    private long elapsedTime;

    private static final Vector DEFAULT_START_POSITION = new Vector(400, 200);

    public static final float VISION_MAX = 100f;

    public Character(Color type) {
        this(DEFAULT_START_POSITION, type);
    }

    public Character(Vector position, Color type) {
        super(position, characterDimensions, type, "character-red");
        checkpointLocation = position;
        checkpointHistory = new HashSet<>();
    }

    public void resetCheckpointHistory() {
        this.checkpointHistory = new HashSet<>();
    }

    /**
     * @return the checkpointHistory
     */
    public Set<Checkpoint> getCheckpointHistory() {
        return checkpointHistory;
    }

    /**
     * @param vision the vision to set
     */
    public void setVision(float vision) {
        this.vision = vision;
    }

    /**
     * @return the vision
     */
    public float getVision() {
        return vision;
    }

    /**
     * changes vision of char, but keeps it within 0 - 100. if n is < 0 the
     * character plays a damage sound
     * 
     * @param n value to change vision by
     */
    public void changeVision(float n) {
        vision += n;
        if (n < 0) {
            if (!SoundPlayer.painSound.isPlaying()) {
                soundPlayer.playPainSound();
            }
        }
        vision = min(vision, visionMax);
        vision = max(vision, visionMin);
    }

    /**
     * @param elapsedTime the elapsedTime to set
     */
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * @return the elapsedTime
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    /**
     * Generate Information for rendering
     *
     * @return
     */
    @Override
    public RenderInfo renderInfo() {
        CharacterMeta meta = new CharacterMeta(type, ClientLevel.getPlayerColor() == this.type, vision, elapsedTime,
                velocity, onGround);
        return new RenderInfo(this, meta);
    }

    /*
     * Sets the Checkpoint Attribute.
     * 
     * @param location The newest checkpoint location
     */
    public void setCheckpointLocation(Checkpoint checkpoint) {
        Vector location = checkpoint.position();
        checkpointHistory.add(checkpoint);
        if (!location.equals(checkpointLocation)) {

            checkpointLocation = location;
            soundPlayer.playCheckpointSound();
        }
    }

    /**
     * Add gravity and movement forces to current velocity, Update positions (using
     * GameObject.updatePosition) Reset some values
     *
     * @param surrounding Surrounding gameobjects
     */
    @Override
    public void updatePosition(List<GameObject> surrounding) {

        velocity = Vector.add(velocity, gravity);
        velocity = Vector.add(velocity, new Vector(left + right, up));

        boolean curGround = onGround, curLeftCollision = leftCollision, curRightCollision = rightCollision,
                curUpCollision = upCollision;

        super.updatePosition(surrounding);

        playMovementSounds();
        playCollisionSounds(curLeftCollision, curRightCollision, curGround, curUpCollision);

        up = 0;
    }

    public void updateOnServer(List<GameObject> surrounding) {
        applyCollisions(surrounding);
    }

    /**
     * determines which moving sounds to play, and plays them (running and skidding)
     */
    private void playMovementSounds() {

        if (onGround && velocity.x() != 0)
            soundPlayer.playRunSound();
        if ((right + left == 0 || !onGround))
            soundPlayer.stopRunSound(false);

    }

    /**
     * determines and plays sounds based on collision (landSound and CollisionSound)
     * 
     * @param curLeftCollision
     * @param curRightCollision
     * @param curGround
     * @param curUpCollision
     */
    private void playCollisionSounds(boolean curLeftCollision, boolean curRightCollision, boolean curGround,
            boolean curUpCollision) {

        if (onGround && !curGround)
            soundPlayer.playLandSound();
        if ((leftCollision && !curLeftCollision) || (rightCollision && !curRightCollision)
                || (upCollision && !curUpCollision))
            soundPlayer.playCollideSound();
    }

    /**
     * Set relevant force, based on input
     *
     * @param direction direction of character movement
     */
    public void move(Direction direction) {
        switch (direction) {
            case JUMP:
                if (onGround) {
                    up = jumpSpeed;
                    soundPlayer.playJumpSound();
                }
                break;
            case LEFT_PRESSED:
                if (onGround) {
                    soundPlayer.playRunSound();
                }
                left = -moveSpeed;
                break;
            case RIGHT_PRESSED:
                if (onGround) {
                    soundPlayer.playRunSound();

                }
                right = moveSpeed;
                break;

            case RIGHT_RELEASED:
                right = 0;
                break;
            case LEFT_RELEASED:
                left = 0;
                break;
        }
    }

    @Override
    public String toString() {
        return "[" + type + position + "]";
    }

    public void resetVision() {
        vision = VISION_MAX;
    }

    /**
     * check whether Character has sustained enough damage to have to reset to
     * checkpoint if so, runs gotoCheckpoint
     */
    public void checkpointCheck() {
        if (checkpointVision == 0)
            return;

        if (vision < checkpointVision - 20 || vision == 0) {

            changeVision(5);
            checkpointVision = vision;
            goToCheckpoint();
        }
    }

    /**
     * Teleports Character to last checkpoint
     */
    public void goToCheckpoint() {

        setPosition(checkpointLocation);
        resetVelocity();
    }

    public void resetVelocity() {
        velocity = new Vector(0, 0);
    }
}
