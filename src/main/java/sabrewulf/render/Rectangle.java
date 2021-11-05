package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

import sabrewulf.game.Vector;

/**
 * This class is responsible for drawing solid color rectangles. Textures
 * should be disabled before using methods in this class. The color should be
 * set by calling static methods on {@link SolidShape}.
 *
 * Constructor is private since all the methods are static only.
 */

public class Rectangle extends SolidShape {

    // Constructor is private, all the methods are static
    private Rectangle() {}

    /**
     * Draw a solid color rectangle
     * @param position the position of the rectangle
     * @param dimensions the dimensions of the rectangle
     */
    public static void draw(Vector position, Vector dimensions) {
        draw(position.x(), position.y(), dimensions.x(), dimensions.y());
    }

    /**
     * Raw draw call to draw a solid color rectangle
     * @param x the x position of the rectangle
     * @param y the y position of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    private static void draw(int x, int y, int width, int height) {
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glEnd();
    }
}
