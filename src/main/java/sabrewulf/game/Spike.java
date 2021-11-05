package sabrewulf.game;

public class Spike extends GameObject {
    private static final long serialVersionUID = -3806049511884766595L;

    public Spike(Vector position, Vector dimensions, Color type, String textureName) {
        super(position, dimensions, type, textureName);
    }

    public static Spike generate(int x, int level, Color spikeColour, Color viewColor) {
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[] {0, 180, 360, 540};
        if (spikeColour.equals(Color.RED)) {
            return new Spike(
                    new Vector(x * PLATFORM_WIDTH, 70 + levelCoords[level]),
                    new Vector(60, 10),
                    viewColor,
                    "spike-red");
        } else if (spikeColour.equals(Color.BLUE)) {
            return new Spike(
                    new Vector(x * PLATFORM_WIDTH, 70 + levelCoords[level]),
                    new Vector(60, 10),
                    viewColor,
                    "spike-blue");
        } else if (spikeColour.equals(Color.GREEN)) {
            return new Spike(
                    new Vector(x * PLATFORM_WIDTH, 70 + levelCoords[level]),
                    new Vector(60, 10),
                    viewColor,
                    "spike-green");
        } else
            return new Spike(
                    new Vector(x * PLATFORM_WIDTH, 70 + levelCoords[level]),
                    new Vector(60, 10),
                    viewColor,
                    "spike-yellow");
    }


    /**
     * deal damage to player
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        float damage = 6/60f;
        if (obj instanceof Character) {
            Character c = (Character) obj;
            c.changeVision(-damage);
            c.checkpointCheck();

        }
    }
}
