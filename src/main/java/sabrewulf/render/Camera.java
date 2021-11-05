package sabrewulf.render;

import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * This class attempts to mimic a real world camera. It moves in the world and
 * focuses on the character that the user is currently controlling (also called
 * the owner).
 * <p>
 * The position of the camera is determined by the characters that the user is
 * controlling position. If no such character is present, the camera stays at
 * the world origin without throwing any errors.
 * <p>
 * The camera movement is restricted to horizontal only. The camera is not
 * affected by the characters y position. The character is always positioned at
 * a constant offset from the left of the screen, except for when the camera
 * reaches the edges of the level. Such cases result in no camera movement,
 * and the camera is locked until the character moves away from the edges of
 * the level.
 */

public class Camera {

    // Controls how much the character is shifted away from the left of the screen
    public static final int CHARACTER_OFFSET_X = 320;

    private static final int DEFAULT_LEFT_BREAKPOINT = -70;

    private int windowWidth;
    private int windowHeight;

    private int leftBreakpoint;
    private int rightBreakpoint;

    private boolean leftLocked;
    private boolean rightLocked;

    public Camera(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.leftBreakpoint = DEFAULT_LEFT_BREAKPOINT;
    }

    /**
     * This method should be called at the start of the frame, before any
     * objects are drawn on the screen. It moves the camera so it focuses on
     * the character that the user is controlling (owner), except for when
     * the character reaches the edges of the level. In such case, the camera
     * does not move past the edge of the level, and the {@link #leftLocked} and
     * {@link #rightLocked} are set accordingly.
     *
     * @param owner the character that the user is controlling
     * @return the absolute horizontal movement applied
     */
    public int beginFrame(RenderInfo owner) {
        Projection.temporary();

        int translateBy = 0;

        leftLocked = false;
        rightLocked = false;

        if (owner != null) {
            int ownerX = owner.getPosition().x();

            int leftScreenCorner = ownerX - (CHARACTER_OFFSET_X);
            int rightScreenCorner = ownerX + (windowWidth - CHARACTER_OFFSET_X);

            if (leftScreenCorner < leftBreakpoint) {
                translateBy = leftBreakpoint;
                leftLocked = true;
            } else if (rightScreenCorner > rightBreakpoint) {
                translateBy = rightBreakpoint - windowWidth;
                rightLocked = true;
            } else {
                translateBy = owner.getPosition().x() - CHARACTER_OFFSET_X;
            }
        }

        translate(translateBy);
        return translateBy;
    }

    /**
     * This method should be called at the end of the frame, after everything
     * has been drawn. It disposes of the camera so UI menus can be drawn
     * without being affected by the cameras position.
     */
    public void endFrame() {
        Projection.dispose();
    }

    /**
     * Sets the right edge of the level, past which the camera should not move.
     * @param rightBreakpoint the right edge of the level
     */
    public void setRightBreakpoint(int rightBreakpoint) {
        this.rightBreakpoint = rightBreakpoint;
    }

    /**
     * Moves the camera along x axis
     * @param deltaX the amount to move the camera along x axis
     */
    private void translate(int deltaX) {
        glTranslatef(-deltaX, 0, 0);
    }

    /**
     * Whether the camera has reached the left edge of the level. If true,
     * the camera is no longer being moved until the character moves away
     * from the edge.
     *
     * @return true if the camera has reached the left edge of the level
     */
    public boolean isLeftLocked() {
        return leftLocked;
    }

    /**
     * Whether the camera has reached the right edge of the level. If true,
     * the camera is no longer being moved until the character moves away
     * from the edge.
     *
     * @return true if the camera has reached the right edge of the level
     */
    public boolean isRightLocked() {
        return rightLocked;
    }

    /**
     * The x coordinate of the left edge of the level.
     * @return x coordinate of the left edge of the level.
     */
    public int getLeftBreakpoint() {
        return leftBreakpoint;
    }

    /**
     * The x coordinate of the right edge of the level.
     * @return x coordinate of the right edge of the level.
     */
    public int getRightBreakpoint() {
        return rightBreakpoint;
    }

}
