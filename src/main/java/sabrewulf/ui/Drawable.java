package sabrewulf.ui;

public interface Drawable {
    /**
     * States which sprites should be drawn on the window. <br>
     * glfwSwapBuffers(UI.window) and glfwPollEvents() are called to allow animated buttons to be
     * shown
     */
    public void draw();
}
