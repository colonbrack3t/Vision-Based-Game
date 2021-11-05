package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class is responsible for controlling projections, the main backbone
 * behind the {@link Camera}. It does low level OpenGL calls.
 * <p>
 * Ultimately, a projection controls where the objects are positioned on the
 * screen. A temporary projection can be spawned via {@link #temporary()} to
 * avoid suffering from projection side effects, such as incorrect translation.
 */

public class Projection {

    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 800;

    // Constructor is private, this class contains static methods only
    private Projection() {}

    /**
     * Initialise the projection. Should be called after application is
     * started, before any rendering is done to ensure the correct
     * positioning of the objects in the world.
     */
    public static void initialise() {
        glMatrixMode(GL_PROJECTION);
        reset();
    }

    /**
     * Reset the projection. It maps the OpenGL coordinates to pixel
     * coordinates. The origin is bottom left (coordinates 0, 0).
     */
    public static void reset() {
        glLoadIdentity();
        glOrtho(0f, WINDOW_WIDTH, 0f, WINDOW_HEIGHT, 0f, 1f);
    }

    /** Save the current projection onto the internal OpenGL stack */
    public static void push() {
        glPushMatrix();
    }

    /** Load the saved projection from the internal OpenGL stack */
    public static void pop() {
        glPopMatrix();
    }

    /**
     * Spawn a temporary projection to render individual components.
     * One projection can be spawned to render the parallax background,
     * while a second one can be spawned to render the ambient weather effect,
     * and neither one will suffer from projection side effects, such as
     * incorrect translation.
     */
    public static void temporary() {
        push();
        reset();
    }

    /**
     * Dispose of the temporary projection created with {@link #temporary()}.
     */
    public static void dispose() {
        pop();
    }
}
