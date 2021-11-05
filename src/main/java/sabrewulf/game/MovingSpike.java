package sabrewulf.game;

import sabrewulf.ui.SoundPlayer;

import java.util.List;

public class MovingSpike extends AIGameObject {

    private static final long serialVersionUID = -4049027916554258697L;
    private int maxY;
    private int minY;

    /**
     * Constructor for Moving Spike: When a Character collides on a SpikePlatform
     * while also being above this spike it will rise up to the surface
     *
     * @param position    Start position
     * @param dimensions  Dimensions and hit-box
     * @param type        Color type
     * @param textureName Texture name
     */
    public MovingSpike(Vector position, Vector dimensions, Color type, String textureName) {
        super(position, dimensions, type, textureName);
        damage = 20/60f;
        minY = position.y();
        maxY = position.y() + 70;
    }

    /**
     * Set the hieghest point spike should rise to
     *
     * @param m
     */
    public void setMaxY(int m) {
        maxY = m;
    }


    /**
     * Generates moving spike with Database parameters
     * @param x
     * @param level
     * @param spikeColour
     * @param viewColor
     * @return
     */
    public static MovingSpike generate(int x, int level, Color spikeColour, Color viewColor) {
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[]{0, 180, 360, 540};
        if (spikeColour.equals(Color.RED)) {
            return new MovingSpike(new Vector(x * PLATFORM_WIDTH, levelCoords[level]), new Vector(60, 10), viewColor,
                    "spike-red");
        } else if (spikeColour.equals(Color.BLUE)) {
            return new MovingSpike(new Vector(x * PLATFORM_WIDTH, levelCoords[level]), new Vector(60, 10), viewColor,
                    "spike-blue");
        } else if (spikeColour.equals(Color.GREEN)) {
            return new MovingSpike(new Vector(x * PLATFORM_WIDTH, levelCoords[level]), new Vector(60, 10), viewColor,
                    "spike-green");
        } else
            return new MovingSpike(new Vector(x * PLATFORM_WIDTH, levelCoords[level]), new Vector(60, 10), viewColor,
                    "spike-yellow");
    }

    /**
     * if the enemy boolean is set to true (by it's Spike Platform) the spike will
     * rise to its max y. Otherwise it will try to retract (or stay stationary if
     * already fully recessed)
     *
     * @param surrounding
     */
    @Override
    public void updatePosition(List<GameObject> surrounding) {
        int moveUpSpeed = 20, moveDownSpeed = 1;
        if (!targets.isEmpty()) {

            position.setY(Math.min(position.y() + moveUpSpeed, maxY));
            if (position.y() == maxY) {

                targets.clear();
            }
        } else {

            position.setY(Math.max(position.y() - moveDownSpeed, minY));
        }

        super.updatePosition(surrounding);
    }

    /**
     * Plays spike sound then adds target
     * @param obj
     */
    @Override
    public void addTarget(GameObject obj) {
        if(!targets.contains(obj))
            soundPlayer.playSpikeSound();
        super.addTarget(obj);
    }

    /**
     * Only cause damage when fully extended
     *
     * @param nextPos
     * @param curPos
     * @param obj
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        if (position.y() == maxY)
            super.onCollision(nextPos, curPos, obj);
    }
}
