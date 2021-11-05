package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class is the base class for any shapes that do not have a texture,
 * and are drawn using a solid color only.
 *
 * Contains static methods to change the drawing color.
 */

public abstract class SolidShape {

    public SolidShape() {}

    /** Set the drawing color to red */
    public static void useRed() {
        useColor(1f, 0f, 0f);
    }

    /** Set the drawing color to green */
    public static void useGreen() {
        useColor(0f, 1f, 0f);
    }

    /** Set the drawing color to blue */
    public static void useBlue() {
        useColor(0f, 0f, 1f);
    }

    /** Set the drawing color to yellow */
    public static void useYellow() {
        useColor(1f, 1f, 0f);
    }

    /** Set the drawing color to white */
    public static void useWhite() {
        useColor(1f, 1f, 1f);
    }

    /**
     * Set the drawing color
     * @param r red amount
     * @param g green amount
     * @param b blue amount
     */
    public static void useColor(float r, float g, float b) {
        glColor4f(r, g, b, 1f);
    }

    /**
     * Set the drawing color with transparency
     * @param r red amount
     * @param g green amount
     * @param b blue amount
     * @param alpha transparency amount
     */
    public static void useColor(float r, float g, float b, float alpha) {
        glColor4f(r, g, b, alpha);
    }
}
