package sabrewulf.game;

import sabrewulf.render.RenderInfo;
import sabrewulf.render.TileMode;

public class Wall extends GameObject {

    private static final long serialVersionUID = -3669563303202756209L;

    public Wall(Vector position, Vector dimensions, Color type) {
        super(position, dimensions, type, "platform-current");
    }

    @Override
    public RenderInfo renderInfo() {
        return new RenderInfo(this, TileMode.WALL);
    }

    public static Wall generate(int x, int level, int height, Color color) {
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540, 720, 900 };
        int[] wallHeights = new int[] { 250, 430, 610, 790, 970, 1150 };
        return new Wall(new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(PLATFORM_WIDTH, wallHeights[height - 1]), color);
    }
}
