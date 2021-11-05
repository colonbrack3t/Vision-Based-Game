package sabrewulf.render;

import sabrewulf.game.*;
import sabrewulf.game.Character;

/**
 * This class is the bridge between game logic and rendering. Each GameObject
 * returns a {@link RenderInfo} which has various fields that describe how
 * the object should be rendered.
 */

public class RenderInfo {

    private Vector position, dimensions;
    private Texture texture;
    private TileMode tileMode;

    private CharacterMeta characterMeta;

    private int zOrder;

    /**
     * Constructor for objects that do not require tiling, eg. Spikes
     *
     * @param obj
     */
    public RenderInfo(GameObject obj) {
        this(obj.position(), obj.getDimensions(), obj.getTexture(), TileMode.SINGLE);
        assignZOrder(obj);
    }

    /**
     * Constructor for Characters, CharacterMeta must be supplied
     *
     * @param obj
     * @param meta
     */
    public RenderInfo(GameObject obj, CharacterMeta meta) {
        this(obj);
        this.characterMeta = meta;
        assignZOrder(obj);
    }

    /**
     * Constructor for tiled objects, eg. Platform or Wall
     *
     * @param obj
     * @param tileMode
     */
    public RenderInfo(GameObject obj, TileMode tileMode) {
        this(obj.position(), obj.getDimensions(), obj.getTexture(), tileMode);
        assignZOrder(obj);
    }

    /**
     * The main RenderInfo constructor
     *
     * @param position the position of the GameObject
     * @param dimensions the dimensions of the GameObject
     * @param texture the texture to use (will most likely get refactored/changed)
     * @param tileMode tiling mode, set to TileMode.SINGLE for no tiling
     */
    public RenderInfo(Vector position, Vector dimensions, Texture texture, TileMode tileMode) {
        this.position = position;
        this.dimensions = dimensions;
        this.texture = texture;
        this.tileMode = tileMode;
    }

    /**
     * Assigns the z order of the objects, which determines which objects get
     * rendered on the top, and which ones on the bottom.
     * @param obj the GameObject based on which the z order is assigned
     */
    private void assignZOrder(GameObject obj) {
        if (obj instanceof Saw) {
            zOrder = 6; // zOrder of 6 is specifically reserved for saws
        } else if (obj instanceof Platform || obj instanceof Wall) {
            zOrder = 10;
        } else if (obj instanceof Exit) {
            zOrder = 20;
        } else if (obj instanceof Character) {
            zOrder = 30;
        }
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getDimensions() {
        return dimensions;
    }

    public Texture getTexture() {
        return texture;
    }

    public TileMode getTileMode() {
        return tileMode;
    }

    public CharacterMeta getCharacterMeta() {
        return characterMeta;
    }

    public int getZOrder() {
        return zOrder;
    }

}
