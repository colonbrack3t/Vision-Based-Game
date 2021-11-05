package sabrewulf.game;

import java.util.Collections;
import java.util.List;

public class SawPlatform extends Platform {
    private static final long serialVersionUID = 1165477174935216143L;
    List<Saw> saws;

    public SawPlatform(Vector position, Vector dimensions, Color color, List<Saw> saw) {
        super(position, dimensions);
        this.saws = saw;
    }

    public SawPlatform(Vector position, Vector dimensions, List<Saw> saw) {
        this(position, dimensions, Color.NEUTRAL, saw);
    }

    public void add(Saw saw) {
        saws.add(saw);
    }

    public SawPlatform(Vector position, Vector dimensions, Saw saw) {
        this(position, dimensions, Collections.singletonList(saw));
    }

    public static SawPlatform generate(int x, int level, int width, Color color, List<Saw> saws) {
        int PLATFORM_WIDTH = 70, PLATFORM_HEIGHT = 70;
        int[] levelCoords = new int[] {0, 180, 360, 540};
        return new SawPlatform(
                new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(width * PLATFORM_WIDTH, PLATFORM_HEIGHT),
                color,
                saws);
    }

    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        super.onCollision(nextPos, curPos, obj);
        if (obj instanceof Character)
            if (nextPos.y() == position.y() + height())
                for (Saw s : saws) {
                    s.addTarget((Character) obj);
                }
    }
}
