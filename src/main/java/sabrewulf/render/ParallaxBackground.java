package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedHashMap;
import java.util.Map;
import sabrewulf.game.Vector;

/**
 * This class is responsible for rendering the parallax background effect,
 * including both the background (eg. stars, mountains, clouds) and the
 * foreground (eg. trees).
 * <p>
 * The parallax effect consists of multiple layers (tiled infinitely) and
 * scrolling at different speeds.
 * <p>
 * The most important fields are {@link #backgounds} and {@link #foregrounds}
 * which are maps of {@link Texture} and floats. The texture corresponds to the
 * layer that is being drawn, and the float corresponds to the speed that the
 * layer will scroll at. Speed of 0 means the layer will not move at all.
 */

public class ParallaxBackground {

    private static final int BACKGROUND_TILE_COUNT = 30;
    private static final int FOREGROUND_TILE_COUNT = 60;

    private static final int BACKGROUND_OFFSET_Y = 0;
    private static final int FOREGROUND_OFFSET_Y = -140;

    private static final int LAYER_OFFSET_X = 320;

    private int windowWidth;
    private int windowHeight;

    private Map<Texture, Float> backgounds;
    private Map<Texture, Float> foregrounds;

    /**
     * Creates the ParallaxBackground and initialises it.
     * @param windowWidth the width of the window
     * @param windowHeight the height of the window
     * @return the initialised ParallaxBackground
     */
    public static ParallaxBackground create(int windowWidth, int windowHeight) {
        ParallaxBackground background = new ParallaxBackground(windowWidth, windowHeight);
        background.initialise();
        return background;
    }

    /**
     * Constructor is private, {@link #create(int, int)} should be used instead.
     *
     * @param windowWidth the width of the window
     * @param windowHeight the height of the window
     */
    private ParallaxBackground(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    /** Draws the background layers of the parallax effect */
    public void drawBackgrounds(int cameraTranslation) {
        draw(backgounds, cameraTranslation, BACKGROUND_TILE_COUNT, BACKGROUND_OFFSET_Y);
    }

    /** Draws the foreground layers of the parallax effect */
    public void drawForegrounds(int cameraTranslation) {
        draw(foregrounds, cameraTranslation, FOREGROUND_TILE_COUNT, FOREGROUND_OFFSET_Y);
    }

    /**
     * Draws either background or foreground layers of the parallax effect.
     * @param layers the map of textures to floats, corresponding to the
     *               layer being drawn and its scroll speed
     * @param cameraTranslation the absolute position of the camera along the
     *                          horizontal axis
     * @param tileCount the number of times to tile the layer horizontally
     * @param offsetY the amount to offset the layer vertically
     */
    private void draw(Map<Texture, Float> layers, int cameraTranslation, int tileCount, int offsetY) {
        for (Map.Entry<Texture, Float> layer : layers.entrySet()) {
            Texture texture = layer.getKey();
            float scrollSpeed = layer.getValue();

            Projection.temporary();
            glTranslatef(-(cameraTranslation + LAYER_OFFSET_X) * scrollSpeed, 0, 0);

            for (int i = 0; i < tileCount; i++) {
                int startX = -texture.getWidth();
                Sprite.draw(new Vector(startX + (i * texture.getWidth()), offsetY), texture);
            }

            Projection.dispose();
        }
    }

    /**
     * Initialises the ParallaxBackground. Load all the background and
     * foreground layers, and map them to their scroll speeds.
     */
    private void initialise() {
        backgounds = new LinkedHashMap<>();
        foregrounds = new LinkedHashMap<>();

        backgounds.put(new Texture("./assets/background-1/sky.png"), 0f);
        backgounds.put(new Texture("./assets/background-1/clouds_1.png"), .3f);
        backgounds.put(new Texture("./assets/background-1/rocks.png"), .8f);
        backgounds.put(new Texture("./assets/background-1/clouds_2.png"), .92f);

        foregrounds.put(new Texture("./assets/background-1/ground-transparent.png"), 1.6f);
    }

}
