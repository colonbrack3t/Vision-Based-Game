package sabrewulf.render;

/**
 * Simple class that represents an RGB based drawing color using 3 floats: red,
 * green, and blue.
 */

public class Color {
    private float red;
    private float green;
    private float blue;

    /**
     * Create an RGB based drawing color
     * @param red the red amount
     * @param green the green amount
     * @param blue the blue amount
     */
    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /** @return the red amount */
    public float getRed() {
        return red;
    }

    /** @return the green amount */
    public float getGreen() {
        return green;
    }

    /** @return the blue amount */
    public float getBlue() {
        return blue;
    }
}
