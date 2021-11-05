package sabrewulf.render;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds sets of {@link Texture} to be used in the
 * {@link Animator} for character animations (walking, jumping, etc.).
 *
 * TextureSet can either be a single image in case of single-image
 * animations (eg. falling), or multiple images, in case of multiple-image
 * animations (eg. walking). The {@link #animated} should be set to true in
 * case of multiple-image animations.
 */

public class TextureSet {

    private List<Texture> textures;
    private long displayDuration;
    private boolean animated;

    private int currentTexture;

    /**
     * Creates a single-image TextureSet. Sets {@link #animated} accordingly.
     *
     * @param file the single image to hold in this TextureSet
     */
    public TextureSet(String file) {
        this(null, 0, false);

        textures = new ArrayList<>();
        textures.add(new Texture(file));
    }

    /**
     * Creates a multiple-image TextureSet
     *
     * @param path the common root path of the textures to hold in this
     *             TextureSet (with prefix if the images have a prefix)
     * @param files the images to hold in this TextureSet
     * @param displayDuration how long a single image in this TextureSet
     *                        should be displayed for
     * @param animated true if multiple-image animation
     */
    public TextureSet(String path, String[] files, long displayDuration, boolean animated) {
        this(null, displayDuration, animated);

        textures = new ArrayList<>();
        for (var file : files) {
            textures.add(new Texture(path + file));
        }
    }

    /**
     * Creates a TextureSet with the provided Textures
     *
     * @param textures the textures that should be held in this TextureSet
     * @param displayDuration how long a single image in this TextureSet
     *                        should be displayed for
     * @param animated true if multiple-image animation
     */
    private TextureSet(List<Texture> textures, long displayDuration, boolean animated) {
        this.textures = textures;
        this.displayDuration = displayDuration;
        this.animated = animated;
    }

    /**
     * Switch to the next image in this multiple-image TextureSet
     */
    public void next() {
        currentTexture++;
        if (currentTexture > textures.size() - 1) {
            currentTexture = 0;
        }
    }

    /**
     * Get the current image in this TextureSet
     * @return the current image in this TextureSet
     */
    public Texture getCurrent() {
        return textures.get(currentTexture);
    }

    public long getDisplayDuration() {
        return displayDuration;
    }

    public boolean isAnimated() {
        return animated;
    }
}
