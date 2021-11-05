package sabrewulf.render;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sabrewulf.game.Color;

/**
 * This class is a high level abstraction responsible for rendering everything
 * on the screen (platforms, characters, the falling leaves effect, the darkness
 * effect, etc.).
 * <p>
 * The main method in this class is {@link #render(List)} which takes a list of
 * {@link RenderInfo} objects, and renders them on the screen. This method
 * should be called every frame by the game logic.
 * <p>
 * This class heavily relies on {@link Camera} and the camera is responsible for
 * which part of the world is currently visible. Otherwise it results in unknown
 * behaviour.
 */

public class GameScreen {

    private GLWrapper wrapper;

    private Camera camera;
    private DarknessShader darknessShader;
    private ParallaxBackground background;
    private ParticleSystem weatherParticleSystem;

    private Map<Color, Animator> characterAnimators;

    /**
     * The main method, which renders a list of {@link RenderInfo} objects on
     * the screen. Should be called every frame.
     *
     * @param thingsToRender the list of objects to render
     */
    public void render(List<RenderInfo> thingsToRender) {
        wrapper.beginFrame();

        thingsToRender.sort(Comparator.comparing(RenderInfo::getZOrder));

        // Find the character that the player is currently controlling, otherwise null
        RenderInfo owner = thingsToRender.stream()
                .filter(info -> (info.getCharacterMeta() != null))
                .filter(info -> (info.getCharacterMeta().shouldFocus()))
                .findFirst()
                .orElse(null);

        int cameraTranslation = camera.beginFrame(owner);

        background.drawBackgrounds(cameraTranslation);

        for (RenderInfo info : thingsToRender) {
            if (info.getCharacterMeta() != null) {
                CharacterMeta meta = info.getCharacterMeta();

                CharacterMarker.draw(info);
                characterAnimators.get(meta.getType()).draw(info);

            } else if (info.getZOrder() == 6) { // zOrder of 6 is specifically reserved for saws
                Saws.draw(info);
            } else {
                Sprite.draw(info);
            }
        }

        background.drawForegrounds(cameraTranslation);

        weatherParticleSystem.update();

        int renderedObjectCount = thingsToRender.size();
        darknessShader.update(owner, camera, renderedObjectCount);
        darknessShader.draw();

        camera.endFrame();
        wrapper.endFrame();
    }

    /**
     * Set the right edge of the level, the camera will not move past this
     * coordinate. Called by the event system.
     * @param rightBreakpoint x coordinate of the right edge of the level
     */
    public void setCameraRightBreakpoint(int rightBreakpoint) {
        camera.setRightBreakpoint(rightBreakpoint);
    }

    // Constructor is private and you can only initialize by GameScreen.create
    private GameScreen() { }

    /**
     * Creates and initialises GameScreen
     * @return the created GameScreen
     */
    public static GameScreen create() {
        GameScreen gameScreen = new GameScreen();
        gameScreen.init();
        return gameScreen;
    }

    /**
     * Should be called on application start. Initialises GameScreen and sets
     * up all the components necessary for rendering (camera, background,
     * darkness effect, etc.).
     */
    public void init() {
        wrapper = new GLWrapper();
        wrapper.initialise();

        camera = new Camera(GLWrapper.WINDOW_WIDTH, GLWrapper.WINDOW_HEIGHT);

        characterAnimators = new HashMap<>();
        characterAnimators.put(Color.BLUE, Animator.create("./assets/FemaleAdventurer/character_femaleAdventurer_"));
        characterAnimators.put(Color.RED, Animator.create("./assets/FemalePerson/character_femalePerson_"));
        characterAnimators.put(Color.GREEN, Animator.create("./assets/MaleAdventurer/character_maleAdventurer_"));
        characterAnimators.put(Color.YELLOW, Animator.create("./assets/MalePerson/character_malePerson_"));

        darknessShader = DarknessShader.create(GLWrapper.WINDOW_WIDTH, GLWrapper.WINDOW_HEIGHT);
        background = ParallaxBackground.create(GLWrapper.WINDOW_WIDTH, GLWrapper.WINDOW_HEIGHT);
        weatherParticleSystem = ParticleSystem.createWeather();
    }

    /**
     * Should be called before application end. Safely closes the GameScreen.
     */
    public void close() {
        wrapper.destroy();
    }

    /**
     * Can be used to check whether the GameScreen is currently running.
     * @return true if the GameScreen is running
     */
    public boolean running() {
        return wrapper.running();
    }

    /**
     * Return the handle to the window that the GameScreen is running on.
     * This can be used to resize the window and perform other operations on
     * the window.
     * @return handle to the window that the GameScreen is running on
     */
    public long window() {
        return wrapper.window();
    }
}
