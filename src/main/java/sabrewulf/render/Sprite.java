package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

import sabrewulf.game.Vector;

/**
 * This class is used to draw {@link Texture} on the screen by abstracting low
 * level OpenGL calls, while adding additional features such as tiling for
 * platforms.
 */

public class Sprite {
    public Sprite(Texture texture) {}

    /**
     * The general draw method which should be used to draw objects on the
     * screen.
     * @param info the {@link RenderInfo} of the GameObject
     */
    public static void draw(RenderInfo info) {
        Vector position = info.getPosition();
        Vector dimensions = info.getDimensions();
        Texture texture = info.getTexture();
        TileMode tileMode = info.getTileMode();

        draw(position, dimensions, texture, tileMode);
    }

    /**
     * The general draw method which should be used to draw objects on the
     * screen. It handles the necessary tiling for the object so eg.
     * platforms are displayed correctly.
     *
     * @param position the position of the object
     * @param dimensions the dimensions of the object
     * @param texture the texture to shade the object with
     * @param tileMode the tiling to apply to the texture
     */
    public static void draw(
            Vector position, Vector dimensions, Texture texture, TileMode tileMode) {
        switch (tileMode) {
            case PLATFORM:
                int tileCountX = dimensions.x() / texture.getWidth();
                for (int i = 0; i < tileCountX; i++) {
                    int tiledX = position.x() + (i * texture.getWidth());
                    Sprite.draw(new Vector(tiledX, position.y()), texture);
                }
                break;
            case WALL:
                final int TILE_OVERLAP_AMOUNT = 30;
                int lastY = dimensions.y() - texture.getHeight();
                for (int y = 0; y <= lastY; y += TILE_OVERLAP_AMOUNT) {
                    Sprite.draw(new Vector(position.x(), position.y() + y), texture);
                }
                break;
            case SINGLE:
                Sprite.draw(position, texture);
                break;
        }
    }

    /**
     * Draw the sprite on the screen (no tint, no tiling, no mirror)
     *
     * @param position lower left corner of the sprite
     * @param texture texture to shade the sprite with
     */
    public static void draw(Vector position, Texture texture) {
        draw(position, texture, 0);
    }

    /**
     * Draw the sprite on the screen mirrored (no tint, no tiling)
     *
     * @param position lower left corner of the sprite
     * @param texture texture to shade the sprite with
     */
    public static void drawMirrored(Vector position, Texture texture) {
        draw(position, texture, 1);
    }

    /**
     * The raw draw call to draw a sprite on the screen
     *
     * @param position the position to draw the sprite at
     * @param texture the texture to bind and use
     * @param uCoord the texture u coordinate to start sampling from
     */
    private static void draw(Vector position, Texture texture, int uCoord) {
        // setting color affects the overall tint and transparency of the sprite
        glColor4f(1f, 1f, 1f, 1f);

        // bind the sprite so it can be drawn on the screen
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        int x = position.x(), y = position.y();
        int width = texture.getWidth(), height = texture.getHeight();

        glBegin(GL_QUADS);
        glTexCoord2f(uCoord, 1);
        glVertex2f(x, y);
        glTexCoord2f(uCoord, 0);
        glVertex2f(x, y + height);
        glTexCoord2f(uCoord ^ 1, 0);
        glVertex2f(x + width, y + height);
        glTexCoord2f(uCoord ^ 1, 1);
        glVertex2f(x + width, y);
        glEnd();
    }
}
