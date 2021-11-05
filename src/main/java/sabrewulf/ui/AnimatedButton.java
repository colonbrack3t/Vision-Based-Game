package sabrewulf.ui;

import sabrewulf.game.Vector;

/** Button that changes on hover/press */
public class AnimatedButton extends Button {
    protected ButtonAnimation animation;

    protected boolean isHovering;

    public AnimatedButton(Vector position, Vector dimensions, ButtonAnimation animation) {
        super(position, dimensions);

        this.animation = animation;
    }

    /** Checks if the mouse is hovering over the button, clicking the button or neither */
    @Override
    public void handleMouse() {
        isClicked = false;
        isHovering = false;
        if (!mouseOver()) return;

        if (isMouseClick()) {
            isClicked = true;
        } else {
            isHovering = true;
        }
    }

    @Override
    public void draw() {
        handleMouse();

        if (isClicked) {
            animation.drawPressed(position);
        } else if (isHovering) {
            animation.drawHover(position);
        } else {
            animation.drawNormal(position);
        }
    }
}
