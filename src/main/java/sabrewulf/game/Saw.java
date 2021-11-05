package sabrewulf.game;

import java.util.List;

public class Saw extends AIGameObject {

    private static final long serialVersionUID = -8525985105697202784L;
    boolean sawMovementPlaying = false;
    boolean moving = false;

    public int min, max;

    public Saw(Vector position, Vector dimensions, String textureName, int min, int max) {
        this(position, dimensions, textureName, Color.NEUTRAL, min, max);
    }

    public Saw(Vector position, Vector dimensions, String textureName, Color type, int min, int max) {
        super(position, dimensions, type, textureName);
        position.setY(position.y() - (height() / 2)); // saw is half in ground
        this.min = min - (width() / 2);
        this.max = max - (width() / 2);
    }

    public static Saw generate(int x, int level, int min, int max) {
        int PLATFORM_HEIGHT = 70;
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540 };
        return new Saw(new Vector(x * PLATFORM_WIDTH, PLATFORM_HEIGHT + levelCoords[level]), new Vector(60, 60),
                "saw-neutral", min * PLATFORM_WIDTH, max * PLATFORM_WIDTH);
    }

    /**
     * @param surrounding Surrounding GameObjects
     */
    @Override
    public void updatePosition(List<GameObject> surrounding) {
        moveSaw();
        super.updatePosition(surrounding);
    }

    /**
     * Move to the closest plyaer, based off "targets"
     */
    public void moveSaw() {
        if (targets.isEmpty()) {
            soundPlayer.stopSawSound(false);
            return;
        }

        moving = true;
        soundPlayer.playSawSound();
        int closestX = max;
        GameObject closest = null;

        for (GameObject c : targets) {
            if (closest == null)
                closest = c;
            int dist = Math.abs(position.x() - c.position.x());
            if (dist <= closestX) {
                closestX = dist;
                closest = c;
            }
        }

        int sawSpeed = 5;

        if (position.x() - closest.position.x() < 0) {
            position.setX(Math.min(position.x() + sawSpeed, max));
        } else if (position.x() - closest.position.x() > 0) {
            position.setX(Math.max(position.x() - sawSpeed, min));
        }

        targets.clear();
    }

}
