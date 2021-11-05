package sabrewulf.game;

public class FakePlatform extends Platform {

    private static final long serialVersionUID = -8461055513642999808L;

    public FakePlatform(Vector position, Vector dimensions, Color color) {
        super(position, dimensions, color);
    }

    /**
     * Generate a spike platform
     *
     * @param x Position in x, measured in tiles as wide ass the Standard Platform Width 60. (eg if
     *     x = 4, position.x = 4 * 60)
     * @param level Height of platform must be a number from [0-3] (fixed heights)
     * @param width Width, measured in PlatformWidth
     * @param color Color of Fake Platform
     * @return
     */
    public static FakePlatform generate(int x, int level, int width, Color color) {
        int PLATFORM_WIDTH = 70, PLATFORM_HEIGHT = 70;
        int[] levelCoords = new int[] {0, 180, 360, 540};
        return new FakePlatform(
                new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(width * PLATFORM_WIDTH, PLATFORM_HEIGHT),
                color);
    }

    /**
     * Ignore Collision, this platform is a dud
     *
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos Current Corner of the colliding gameObject
     * @param obj gameobject that collided with this
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        // super.onCollision(nextPos, curPos, obj);
    }
}
