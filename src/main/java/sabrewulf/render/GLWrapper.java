package sabrewulf.render;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * This class is the wrapper around {@link GameScreen} to abstract the low
 * level calls to GLFW and OpenGL. It handles the window creation, sets various
 * attributes on the window (eg. the size and the title of the window), and
 * finally hands over control to OpenGL so that objects can be rendered.
 */

public class GLWrapper {

    // the window pointer, so we can refer to the window after it has been created
    private long window;

    // window dimensions
    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 800;

    /**
     * Nothing to be done in the constructor, {@link #initialise()} should be
     * called manually.
     */
    public GLWrapper() {
    }

    /**
     * The convenience method to initialise everything and prepare the
     * window for rendering (initialise both GLFW and OpenGL).
     */
    public void initialise() {
        initialiseGLFW();
        initialiseGL();
    }

    /**
     * Initialise GLFW. This opens a new window, and sets various attributes
     * on the window (eg. the size and the title of the window)
     */
    private void initialiseGLFW() {
        setupErrorCallback();

        // Initialise GLFW, throw exception if it failed
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        disableResizing();
        displayWindow();
        setIcon();
        setContext();
    }

    /** Set up an error callback so errors get sent to System.err */
    private void setupErrorCallback() {
        GLFWErrorCallback.createPrint(System.err).set();
    }

    /** Prevent the user from resizing the window */
    private void disableResizing() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    }

    /** Open and display the actual window that will be used for rendering */
    private void displayWindow() {
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Curfew", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
    }

    /** Load and set the icon of the window */
    private void setIcon() {
        WindowIcon icon = WindowIcon.loadIcon("./assets/icon.png");

        GLFWImage image = GLFWImage.malloc();
        image.set(icon.getWidth(), icon.getHeight(), icon.getImage());

        GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
        imageBuffer.put(0, image);

        glfwSetWindowIcon(window, imageBuffer);
    }

    /**
     * This method finalises the GLFW initialisation, and hands over control
     * to OpenGL. Should be called after all the various attributes have been
     * set on the window (eg. the size and the title of the window)
     */
    private void setContext() {
        // Make the specified window "current" on the calling thread.
        glfwMakeContextCurrent(window);

        // Hand over control to OpenGL.
        // Enable GL functions inside the [org.lwjgl.opengl] package.
        // Calling GL functions will affect the "current" window
        GL.createCapabilities();
    }

    /** Initialise OpenGL and prepare for object rendering */
    private void initialiseGL() {
        Projection.initialise();
        enableAlphaBlending();
        enableTextures();
    }

    /** Enable alpha blending so both images and shapes can have transparency */
    private void enableAlphaBlending() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /** Should be invoked at the START of the frame by the renderer */
    public void beginFrame() {
        clearFrame();
    }

    /** Should be invoked at the END of the frame by the renderer */
    public void endFrame() {
        swapBuffers();
        pollEvents();
    }

    /** Clear what was drawn last frame using a solid black color */
    public void clearFrame() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * There are two buffers: the one we are drawing to, and the one that is
     * currently being displayed. After we're done drawing, we need to swap the
     * buffers, so the buffer that we just drawn to will be displayed. Called
     * in {@link #endFrame()}.
     */
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    /** Poll for keyboard and mouse events */
    public void pollEvents() {
        glfwPollEvents();
    }

    /** Free allocated resources and safely exit to prevent memory leaks */
    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Returns whether the window is currently open and running.
     * @return true if window is open
     */
    public boolean running() {
        return !glfwWindowShouldClose(window);
    }

    /**
     * Return the handle to the window. This can be used to resize the window
     * and perform other operations on the window.
     * @return handle to the window
     */
    public long window() {
        return window;
    }

    /** Enable textures to be drawn on screen */
    public static void enableTextures() {
        glEnable(GL_TEXTURE_2D);
    }

    /** Disable textures to be drawn on screen */
    public static void disableTextures() {
        glDisable(GL_TEXTURE_2D);
    }

}

/**
 * The WindowIcon represents the icon of the window.
 *
 * The {@link #loadIcon(String)} can be used to load the icon of the window.
 */

class WindowIcon {

    private ByteBuffer image;
    private int width, height;

    /**
     * Create a new window icon. Constructor is private,
     * {@link #loadIcon(String)} should be used instead.
     *
     * @param width the width of the icon
     * @param height the height of the icon
     * @param image the {@link ByteBuffer} holding the icon
     */
    private WindowIcon(int width, int height, ByteBuffer image) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Loads the icon from the path, and returns a new WindowIcon containing
     * that icon.
     * @param path the path of the icon
     * @return new WindowIcon containing the loaded icon.
     */
    public static WindowIcon loadIcon(String path) {
        ByteBuffer image;
        IntBuffer width, height, componentCount;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            componentCount = stack.mallocInt(1);
            width = stack.mallocInt(1);
            height = stack.mallocInt(1);

            image = stbi_load(path, width, height, componentCount, 4);
            if (image == null) {
                // throw new resource_error("Could not load image resources.");
            }
        }
        return new WindowIcon(width.get(), height.get(), image);
    }

    /** Get the {@link ByteBuffer} holding the icon */
    public ByteBuffer getImage() {
        return image;
    }

    /** Get the width of the icon */
    public int getWidth() {
        return width;
    }

    /** Get the height of the icon */
    public int getHeight() {
        return height;
    }

}