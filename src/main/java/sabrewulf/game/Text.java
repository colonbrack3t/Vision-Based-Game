package sabrewulf.game;

import java.io.Serializable;
import sabrewulf.render.RenderInfo;

public class Text extends GameObject {

    private static final long serialVersionUID = -3669563303202756209L;

    public Text(Vector position, Vector dimensions, Color type, String name) {
        super(position, dimensions, type, name);
    }

    @Override
    public RenderInfo renderInfo() {
        return new RenderInfo(this);
    }

    public static Text generate(int x, int level, int height, Color color, String name) {
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540 };
        int[] wallHeights = new int[] { 250, 430, 610 };
        return new Text(new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(PLATFORM_WIDTH, wallHeights[height - 1]), color, name);
    }
}
