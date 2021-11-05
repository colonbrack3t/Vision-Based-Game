package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

import sabrewulf.game.Vector;

/**
 * This class is responsible for drawing the rotating saws visible in the game.
 * This class has static methods only and should not be instantiated.
 * <p>
 * The saws are rotated by using the raw OpenGL calls. The rotation logic is
 * fully contained inside the {@link #draw(RenderInfo)} method, and therefore
 * that is the only method that needs to be called. It should be called every
 * frame.
 */

public class Saws {

    public static final int SAW_WIDTH = 64;
    public static final int SAW_HEIGHT = 64;

    private static float angle;

    // Constructor should not be called since class contains static methods only
    private Saws() {}

    /**
     * Draw a rotating saw
     * @param info the {@link RenderInfo} corresponding to a saw
     */
    public static void draw(RenderInfo info) {
        draw(info.getPosition(), info.getTexture());
    }

    /**
     * The raw draw call to draw a rotating saw. Rotation logic is contained
     * inside this method, and no additional methods need to be called.
     * @param position the position of the saw
     * @param texture the texture of the saw
     */
    public static void draw(Vector position, Texture texture) {
        int halfWidth = SAW_WIDTH / 2;
        int halfHeight = SAW_HEIGHT / 2;

        int x = position.x();
        int y = position.y();

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        glTranslatef(x + halfWidth, y + halfHeight, 0);
        glRotatef(angle, 0, 0, 1);
        glTranslatef(-(x + halfWidth), -(y + halfHeight), 0);

        Sprite.draw(position, texture);

        glPopMatrix();
        glMatrixMode(GL_PROJECTION);

        angle = (angle + 5f) % 360f;
    }
}
