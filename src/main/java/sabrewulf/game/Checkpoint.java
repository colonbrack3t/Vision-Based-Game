package sabrewulf.game;

public class Checkpoint extends GameObject {
    /**
     *
     */
    private static final long serialVersionUID = 4683847934071868364L;

    /**
     * Checkpoint Constructor. Its always visible for all players
     *
     * @param position   Position
     * @param dimensions Dimension
     */
    public Checkpoint(Vector position, Vector dimensions) {
        super(position, dimensions, Color.NEUTRAL, "checkpoint-down");
    }

    public static Checkpoint generate(int x, int floor) {
        int PLATFORM_WIDTH = 70, FLOOR_HEIGHT = 180;
        return new Checkpoint(new Vector(x * PLATFORM_WIDTH, (floor * FLOOR_HEIGHT) + PLATFORM_WIDTH),
                new Vector(PLATFORM_WIDTH, PLATFORM_WIDTH));
    }

    /**
     * Has no physical collision, but if the collided object is a character, their
     * checkpoint is set
     *
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        if (obj instanceof Character) {
            Character c = (Character) obj;
            c.setCheckpointLocation(this);
        }
    }
}
