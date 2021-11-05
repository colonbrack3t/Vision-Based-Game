package sabrewulf.render;

/**
 * This enum was originally used to specify the tiling mode for the sprite
 * being drawn, and the texture for the sprite being drawn. However this enum
 * is now deprecated, and most functionality has been moved to {@link Sprite}
 * and {@link TextureMapper}.
 */

public enum TextureType {
    CHARACTER,
    PLATFORM,
    WALL,
    SPIKE;

    public enum PlatformType {
        GRASS,
        SNOW,
        CASTLE;
    }

    public enum CharacterType {
        RED,
        GREEN,
        BLUE,
        YELLOW;
    }
}
