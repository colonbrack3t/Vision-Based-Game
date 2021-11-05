package sabrewulf.game;

import sabrewulf.render.RenderInfo;
import sabrewulf.render.TileMode;

public class Platform extends GameObject {

    private static final long serialVersionUID = 2408005624998653632L;

    public Platform(Vector position, Vector dimensions) {
        this(position, dimensions, Color.NEUTRAL);
    }

    public Platform(Vector position, Vector dimensions, Color type) {
        super(position, dimensions, type, "platform-current");
    }

    @Override
    public RenderInfo renderInfo() {
        return new RenderInfo(this, TileMode.PLATFORM);
    }

    public static Platform generate(int x, int level, int width, Color color) {
        int PLATFORM_WIDTH = 70, PLATFORM_HEIGHT = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540 };
        return new Platform(new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(width * PLATFORM_WIDTH, PLATFORM_HEIGHT), color);
    }
}
