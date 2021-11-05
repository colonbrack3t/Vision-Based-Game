package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

import sabrewulf.game.Color;
import sabrewulf.game.Vector;

/**
 * This class is responsible for drawing the character markers above the
 * characters so that players know which character corresponds to which color.
 * <p>
 * The character markers drawn are in the shape of a small upside down
 * triangle.
 * <p>
 * There are two modes available: focused, and unfocused.
 * <p>
 * The focused mode draws a bigger marker, together with a white outline around
 * the marker that fades in and out over time. The focused mode is intended to
 * be used for the character that the player is currently controlling.
 * <p>
 * The unfocused mode draws a smaller marker without the white outline. The
 * unfocused mode is intended to be used for all the other characters.
 */

public class CharacterMarker extends SolidShape {

    private static final Vector FOCUSED_OFFSET = new Vector(14, 102);
    private static final Vector FOCUSED_DIMENSIONS = new Vector(22, 14);

    private static final Vector UNFOCUSED_OFFSET = new Vector(18, 102);
    private static final Vector UNFOCUSED_DIMENSIONS = new Vector(16, 8);

    private static final Vector OUTLINE_OFFSET = new Vector(12, 100);
    private static final Vector OUTLINE_DIMENSIONS = new Vector(26, 17);

    private static float outlineAlpha = 0f;
    private static float outlineAlphaIncrement = .013f;

    private static final float OUTLINE_ALPHA_MIN = 0f;
    private static final float OUTLINE_ALPHA_MAX = 1f;

    // Constructor is private since this class contains static methods only
    private CharacterMarker() {}

    /**
     * Draws the character marker for the character that has been passed. Any
     * character can be passed, this method figures out whether the character is
     * the one that is being controlled by the user, and selects the mode
     * (focused, unfocused) accordingly.
     * <p>
     * In focused mode, the marker is larger, and additionally has a white
     * outline that fades in and out over time. In unfocused mode, the marker is
     * smaller and has no outline.
     * <p>
     * The character color is checked, and the marker is drawn in the
     * appropriate color. The following colors are supported: red, green, blue,
     * yellow.
     *
     * @param info the character to draw the character marker for
     */
    public static void draw(RenderInfo info) {
        boolean focused = info.getCharacterMeta().shouldFocus();
        Color type = info.getCharacterMeta().getType();
        Vector position = info.getPosition();

        if (focused) {
            drawOutline(position);
        }

        switch (type) {
            case RED:
                useRed();
                break;
            case GREEN:
                useGreen();
                break;
            case BLUE:
                useBlue();
                break;
            case YELLOW:
                useYellow();
                break;
        }

        if (focused) {
            drawFocused(position);
        } else {
            drawUnfocused(position);
        }
    }

    /**
     * Draws a white outline that fades in and out over time. This is
     * intended to be used in focused mode. The fade in and out logic is also
     * contained in this method, and therefore should be called each frame
     * for the fading to be visible.
     *
     * @param position the characters position (offset is applied before
     *                 drawing the marker)
     */
    private static void drawOutline(Vector position) {
        if (outlineAlpha > OUTLINE_ALPHA_MAX || outlineAlpha < OUTLINE_ALPHA_MIN) {
            outlineAlphaIncrement = -outlineAlphaIncrement;
        }
        outlineAlpha += outlineAlphaIncrement;
        useColor(1f, 1f, 1f, outlineAlpha);

        draw(Vector.add(position, OUTLINE_OFFSET), OUTLINE_DIMENSIONS);
    }

    /**
     * Draws the focused marker
     *
     * @param position the characters position (offset is applied before
     *                 drawing the marker)
     */
    private static void drawFocused(Vector position) {
        draw(Vector.add(position, FOCUSED_OFFSET), FOCUSED_DIMENSIONS);
    }

    /**
     * Draws the unfocused marker
     *
     * @param position the characters position (offset is applied before
     *                 drawing the marker)
     */
    private static void drawUnfocused(Vector position) {
        draw(Vector.add(position, UNFOCUSED_OFFSET), UNFOCUSED_DIMENSIONS);
    }

    /**
     * Draws a marker at the provided position (no offset is applied), with
     * the provided marker dimensions.
     *
     * @param position the marker position (no offset is applied)
     * @param dimensions the marker dimensions
     */
    private static void draw(Vector position, Vector dimensions) {
        GLWrapper.disableTextures();
        draw(position.x(), position.y(), dimensions.x(), dimensions.y());
        GLWrapper.enableTextures();
    }

    /**
     * Raw draw call to draw a marker at (x, y) with dimensions (width,
     * height). This method relies on textures being turned off, otherwise it
     * results in unknown behaviour.
     *
     * @param x the x position of the marker
     * @param y the y position of the marker
     * @param width the width of the marker
     * @param height the height of the marker
     */
    private static void draw(int x, int y, int width, int height) {
        float halfWidth = (float) width / 2f;
        glBegin(GL_TRIANGLES);
        glVertex2f(x + halfWidth, y);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glEnd();
    }
}
