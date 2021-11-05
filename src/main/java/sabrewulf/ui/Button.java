package sabrewulf.ui;

import org.lwjgl.glfw.GLFW;
import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;

public class Button {
    protected Vector position;
    private Vector dimensions;
    protected String spriteTexture;

    protected boolean isClicked = false;

    public Button(Vector position, Vector dimensions) {
        this.position = position;
        this.dimensions = dimensions;
    }

    public Button(Vector position, Vector dimensions, String spriteTexture) {
        this.position = position;
        this.dimensions = dimensions;

        this.spriteTexture = spriteTexture;
    }

    public Button(
            int newX,
            Vector position,
            Vector dimensions /* , Sprite button */) { // constructor for in-game
        // buttons to allow translation
        this.position = position;
        this.dimensions = dimensions;

        // button.draw(newX, y);
    }

    /** @return true if the mouse is hovered over the button. false otherwise */
    public boolean mouseOver() {
        return Vector.withinSquare(UI.getMousePositionWithInvertedY(), position, dimensions);
    }

    /** @return true if the mouse is being pressed. false otherwise */
    protected boolean isMouseClick() {
        GLFW.glfwPollEvents();
        return GLFW.glfwGetMouseButton(UI.window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
    }

    public boolean isClicked() {
        // TODO: update from scene
        return isClicked;
    }

    /** Checks if the mouse is hovering over the button, clicking the button or neither */
    protected void handleMouse() {
        if (!mouseOver()) {
            return;
        }

        isClicked =
                isMouseClick(); // only checks if the mouse is clicked if the mouse is over the
                                // button
    }

    public void draw() {
        handleMouse();
        Sprite.draw(position, Texture.create(spriteTexture));
    }

    public Vector position() {
        return position;
    }

    public Vector dimensions() {
        return dimensions;
    }
}
