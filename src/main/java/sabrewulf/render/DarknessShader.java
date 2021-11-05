package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.lwjgl.opengl.*;
import sabrewulf.game.Vector;
import sabrewulf.game.Character;

/**
 * This class is responsible for drawing the darkness effect in the game. The
 * darkness effect is implemented as a shader, and therefore requires two
 * external shader files (vertex shader and fragment shader) written in C.
 * <p>
 * This class is a bridge between the shader files and the game. It is
 * responsible for loading the shader files, and controlling the various
 * parameters in the shader files according to the current game state.
 * <p>
 * The darkness effect consists of two distinct parts: the vision circle, and
 * the overall tint. The vision circle corresponds to the players health. As the
 * player takes damage, the vision circle gets smaller in size. The overall tint
 * affects the entire screen. If players take too long to complete a level, the
 * screen gets pitch black.
 */

public class DarknessShader {

    private static final float MAX_RADIUS = 1.3f;
    private static final float MIN_RADIUS = 0f;

    private static final float MAX_FALLOFF = 3.0f;
    private static final float MIN_FALLOFF = .36f;

    private static final float MIN_TINT = .4f;
    private static final float MAX_TINT = 1f;

    private static long TINT_TIMEOUT = 180 * 1000 * 1000 * 1000L; // 180 seconds = 3 minutes

    private static final long TINT_BASELINE_OBJECT_COUNT = 30;
    private static final long TINT_BASELINE_TIMEOUT = 180 * 1000 * 1000 * 1000L; // 180 seconds = 3 minutes
    private static final long TINT_BASELINE_INCREMENT = 4 * 1000 * 1000 * 1000L; // 4 seconds

    private static final int POSITION_OFFSET_X = 24;
    private static final int POSITION_OFFSET_Y = 52;

    // the shader itself is stored in openGL, we only store the id to access it
    private int program = 0;

    private int windowWidth;
    private int windowHeight;

    /**
     * Creates the darkness effect including both the vision circle and the
     * overall tint. Window dimensions are required to display the effect
     * full screen.
     * @param windowWidth the width of the window
     * @param windowHeight the height of the window
     * @return the initialised darkness effect
     */
    public static DarknessShader create(int windowWidth, int windowHeight) {
        DarknessShader darknessShader = new DarknessShader(windowWidth, windowHeight);
        darknessShader.initialise();
        return darknessShader;
    }

    // The constructor is private, create should be used instead
    private DarknessShader(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    /**
     * Initialises both the vertex and the fragment shaders required to
     * accomplish the darkness effect. If the loading failed, this method
     * fails silently. This is useful in systems where shaders are not
     * supported, or only an older version is supported. In such cases, the
     * darkness effect is simply not displayed.
     *
     * On successful loading of the shader files, they are linked and
     * validated as any regular C source file.
     */
    private void initialise() {
        int vertexShader = createShader("./assets/shaders/screen.vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragmentShader = createShader("./assets/shaders/screen.frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

        if (vertexShader == 0 || fragmentShader == 0)
            return;

        program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == 0)
            return;

        // shaders got loaded correctly, attach to openGL and link
        ARBShaderObjects.glAttachObjectARB(program, vertexShader);
        ARBShaderObjects.glAttachObjectARB(program, fragmentShader);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
            System.err.println(getLogInfo(program));
    }

    /**
     * Loads and creates a shader. Should be used to load the vertex and the
     * fragment shaders. If unsuccessful, returns 0.
     *
     * @param filename the file name of the shader
     * @param shaderType vertex or fragment shader
     * @return the loaded shader
     */
    private int createShader(String filename, int shaderType) {

        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
        if (shader == 0)
            return 0;

        try {
            ARBShaderObjects.glShaderSourceARB(shader, fileToString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader,
                    ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Cant compile shader: " + getLogInfo(shader));

            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Reads a file and returns it as a string
     *
     * @param filename path to file from the project root directory
     * @return file as a string
     * @throws IOException exception if any occurred during reading
     */
    private String fileToString(String filename) throws IOException {
        StringBuilder output = new StringBuilder();

        try (
                FileInputStream input = new FileInputStream(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))
        ) {
            String line;
            while ((line = reader.readLine()) != null)
                output.append(line).append('\n');
        }

        return output.toString();
    }

    /**
     * This method is a convenience method to update all the parameters
     * required to draw the darkness effect correctly. It should be called
     * every frame to update the darkness effect (including the vision circle
     * and the overall tint).
     *
     * @param owner the character that the player is controlling
     * @param camera the camera that is responsible for drawing the level
     * @param renderedObjectCount number of objects in the level
     */
    public void update(RenderInfo owner, Camera camera, int renderedObjectCount) {
        setPosition(owner, camera);
        setVision(owner);
        updateTintTimeout(renderedObjectCount);
        updateTint(owner);
    }

    /**
     * If players take too long to complete a level, the screen gets pitch
     * black. This method calculates the amount of time it takes for the
     * screen to get pitch black according to the number of objects in the
     * level.
     * @param renderedObjectCount the number of objects in the level
     */
    public void updateTintTimeout(int renderedObjectCount) {
        if (renderedObjectCount > TINT_BASELINE_OBJECT_COUNT) {
            long extraTime = (renderedObjectCount - TINT_BASELINE_OBJECT_COUNT) * TINT_BASELINE_INCREMENT;
            TINT_TIMEOUT = TINT_BASELINE_TIMEOUT + extraTime;
        } else {
            TINT_TIMEOUT = TINT_BASELINE_TIMEOUT;
        }
    }

    /**
     * Draws the darkness effect on the screen including both the vision
     * circle, and the overall tint.
     */
    public void draw() {
        // activate the shader
        ARBShaderObjects.glUseProgramObjectARB(program);

        // save the current translation matrix, and load a new matrix without
        // translation applied
        // this way the darkness shader ignores the translation that all the other
        // objects have
        // and the darkness shader stays in one place
        Projection.temporary();

        // We draw a full screen sized rectangle with the shader applied. In this case,
        // the color doesn't matter as it's completely controlled inside the shader
        Rectangle.useWhite();
        Rectangle.draw(new Vector(0, 0), new Vector(windowWidth, windowHeight));

        // reload the saved translation matrix, so further objects do get the
        // translation applied
        Projection.dispose();

        // release the shader so objects can be drawn normally again
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    /**
     * Sets the vision circle according to the players health. This field is
     * accessed via the character that the user is currently controlling. If
     * no such character exists, the vision circle is set to maximum possible.
     * @param owner the character that the user is currently controlling
     */
    public void setVision(RenderInfo owner) {
        float vision = (owner != null) ? owner.getCharacterMeta().getVision() : Character.VISION_MAX;
        setVision(vision);
    }

    /**
     * Updates the overall tint of the screen based on the time since the
     * level started. This field is accessed via the character that the user
     * is currently controlling. If no such character exists, no tint gets
     * applied.
     * @param owner the character that the user is currently controlling
     */
    public void updateTint(RenderInfo owner) {
        long elapsedTime = (owner != null) ? owner.getCharacterMeta().getRunningTime() : 0L;
        updateTint(elapsedTime);
    }

    /**
     * Set the position of the vision circle. The position is set to the
     * characters that the user is currently controlling position. If no such
     * character is present (owner == null) then the position is set to the
     * center of the screen.
     *
     * If the character is present, the vision circle follows the character,
     * however since camera is designed to stop moving at the edges of the
     * level, the position must be calculated to comply with the camera.
     *
     * @param owner the character that the user is currently controlling
     * @param camera the camera that is used to render the level
     */
    public void setPosition(RenderInfo owner, Camera camera) {
        if (owner == null) {
            Vector centerOfScreen = new Vector(windowWidth / 2, windowHeight / 2);
            setPosition(centerOfScreen);
            return;
        }

        Vector shaderPosition;
        if (camera.isLeftLocked()) {
            Vector cameraOffset = new Vector(camera.getLeftBreakpoint(), 0);
            shaderPosition = Vector.subtract(owner.getPosition(), cameraOffset);
        } else if (camera.isRightLocked()) {
            Vector cameraOffset = new Vector(camera.getRightBreakpoint() - windowWidth, 0);
            shaderPosition = Vector.subtract(owner.getPosition(), cameraOffset);
        } else {
            shaderPosition = new Vector(Camera.CHARACTER_OFFSET_X, owner.getPosition().y());
        }

        setPosition(shaderPosition);
    }

    /**
     * Update the vision circle based on the players health.
     * @param vision the players health (vision)
     */
    public void setVision(float vision) {
        // radius 1.3f -> .02f
        // falloff 3.0f -> .32f

        float percentage = vision / 100f;

        float radius = lerp(MIN_RADIUS, MAX_RADIUS, percentage);
        float falloff = lerp(MIN_FALLOFF, MAX_FALLOFF, percentage);

        setRadius(radius);
        setFalloff(falloff);
    }

    /**
     * Update the overall tint based on elapsedTime.
     * @param elapsedTime the time elapsed since level start
     */
    public void updateTint(long elapsedTime) {
        // tint 0.4f -> 1f
        double percentage = (double) elapsedTime / (double) TINT_TIMEOUT;

        float tint = lerp(MIN_TINT, MAX_TINT, (float) percentage);

        setTint(tint);
    }

    /**
     * Set the position of the vision circle (an offset is applied so the
     * circle is centered on the character)
     * @param position the position of the character
     */
    public void setPosition(Vector position) {
        setCharacterX(position.x() + POSITION_OFFSET_X);
        setCharacterY(position.y() + POSITION_OFFSET_Y);
    }

    /**
     * Linearly interpolate between two values (min and max) based on percentage
     *
     * @param min        the minimum value
     * @param max        the maximum value
     * @param percentage the amount of interpolation, in range [0, 1]
     * @return the interpolated value
     */
    private float lerp(float min, float max, float percentage) {
        return min + (max - min) * percentage;
    }

    /**
     * Change overall tint of the screen
     * @param tint in range [0, 1]
     */
    public void setTint(float tint) {
        setUniform1f("tint", tint);
    }

    /**
     * Change the vision circle radius
     * @param radius the radius of the vision circle
     */
    public void setRadius(float radius) {
        setUniform1f("radius", radius);
    }

    /**
     * Change the vision circle falloff (change how blurry the circle edge is)
     * @param falloff the falloff of the vision circle
     */
    public void setFalloff(float falloff) {
        setUniform1f("falloff", falloff);
    }

    /**
     * Set the characters x position for the vision circle
     * @param characterX the characters x position
     */
    public void setCharacterX(int characterX) {
        setUniform1f("characterX", characterX);
    }

    /**
     * Set the characters y position for the vision circle
     * @param characterY the characters y position
     */
    public void setCharacterY(int characterY) {
        setUniform1f("characterY", characterY);
    }

    /**
     * Sets a uniform variable inside the shader
     *
     * @param name  name of the variable
     * @param value the new value for the variable
     */
    private void setUniform1f(String name, float value) {
        // activate the shader so we can access uniform variables
        ARBShaderObjects.glUseProgramObjectARB(program);

        // get the address of the variable in memory
        int address = GL20.glGetUniformLocation(program, name);

        // now that we know the address we can change the actual variable
        GL20.glUniform1f(address, value);

        // deactivate the shader, otherwise it will be in use for next object drawn
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    /**
     * Gets the log info for an object. The object can either be a shader, or
     * a shader program consisting of multiple shaders. Typically used for
     * debugging and in case of errors.
     *
     * @param obj either a shader, or a shader program consisting of multiple
     *           shaders
     * @return the log info associated with the object
     */
    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj,
                ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
}
