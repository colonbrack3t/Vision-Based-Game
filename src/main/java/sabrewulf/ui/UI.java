package sabrewulf.ui;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static sabrewulf.render.GLWrapper.WINDOW_HEIGHT;
import static sabrewulf.render.GLWrapper.WINDOW_WIDTH;
import static sabrewulf.ui.SceneType.*;

import java.nio.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import sabrewulf.events.AvailableColorsChangeEvent;
import sabrewulf.events.CharacterNamesEvent;
import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.EventListener;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.MoveEvent;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.events.StartGameEvent;
import sabrewulf.game.Color;
import sabrewulf.game.Direction;
import sabrewulf.game.Game;
import sabrewulf.game.Vector;
import sabrewulf.networking.client.Client;
import sabrewulf.render.GameScreen;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.scenes.*;

public class UI implements EventListener {

    public SceneType currentScene = TITLE;
    public static long window;
    public static Sound songSound;
    public static Sound shortSongSound;
    public SoundPlayer soundPlayer = new SoundPlayer();
    public boolean longSong = false;
    private EventBus eventBus;
    private GameScreen gs;
    public Game game;
    public String ip = "123.45.678.90";
    public Client client;

    public static boolean isHost = false;
    public Thread gameServerThread;
    public Set<Color> availableColors;

    public boolean exit = false;

    public String name = "";

    public Map<Color, String> characterNames;
    // public String[] nameList = new String[4];

    public int translation = 0;
    public int games = 0;

    public long startTime = 0;
    public long endTime = 0;
    public int timeTaken = 0;
    public boolean timeEnd = false;
    public List<String> names;

    private Scene selectCharacterScene;

    public Button backButton = new AnimatedButton(new Vector(25, 730), new Vector(195, 50),
            new ButtonAnimation("Back", "Back hover", "Back press"));

    Button menu;
    Button music;

    public String musOffButtonTexture = "Music";
    public String musButtonTexture = "Music2";
    public String musHoverTexture = "Music2 hover";
    public String musOffHoverTexture = "Music hover";

    Button musicButton = new AnimatedButton(new Vector(1224, 730), new Vector(65, 50),
            new ButtonAnimation(musButtonTexture, musHoverTexture, musButtonTexture)); // has no press animation, so
    // third is first sprite

    Button musicOffButton = new AnimatedButton(new Vector(1224, 730), new Vector(65, 50),
            new ButtonAnimation(musOffButtonTexture, musOffHoverTexture, musOffButtonTexture));

    public String sfxButtonTexture = "SFX2";
    public String sfxOffButtonTexture = "SFX";
    public String sfxHoverTexture = "SFX2 hover";
    public String sfxOffHoverTexture = "SFX hover";

    Button soundButton = new AnimatedButton(new Vector(1324, 730), new Vector(65, 50),
            new ButtonAnimation(sfxButtonTexture, sfxHoverTexture, sfxButtonTexture));
    Button soundOffButton = new AnimatedButton(new Vector(1324, 730), new Vector(65, 50),
            new ButtonAnimation(sfxOffButtonTexture, sfxOffHoverTexture, sfxOffButtonTexture));

    public UI(GameScreen gameScreen, EventBus eventBus) {
        window = gameScreen.window();
        this.gs = gameScreen;
        this.eventBus = eventBus;
        this.eventBus.subscribe(IncomingEvent.class, this);

        shortSongSound = new Sound("assets/sounds/shortSong.ogg");
        shortSongSound.playSong();

        new Thread(new Runnable() // the song file is quite large so it is initialised
        // in a separate thread
        // so rest of game can load quickly
        {

            @Override
            public void run() {
                songSound = new Sound("assets/sounds/song.ogg");
                float offset = shortSongSound.getOffset();
                songSound.setOffset(offset);
                longSong = true;
                if (shortSongSound.isPlaying()) { // once the full version of the song has
                    // loaded the short version is
                    // stopped and the new version plays from the exact point the
                    // old
                    // version reached.
                    shortSongSound.playSong();
                    songSound.playSong();
                }
            }
        }).start();
    }

    public void drawSoundButtons() {
        if (Sound.sfx) {
            soundButton.draw();
            if (soundButton.isClicked()) {
                Sound.sfx = false;
                soundPlayer.playClickSound();
            }
        } else {
            soundOffButton.draw();
            if (soundOffButton.isClicked()) {
                Sound.sfx = true;
                soundPlayer.playClickSound();
            }
        }

        if (Sound.music) {
            musicButton.draw();

            if (musicButton.isClicked()) {
                Sound.music = false;
                soundPlayer.playClickSound();

                if (longSong) { // check which version of the song is currently playing so know
                                // which one to
                    // stop
                    songSound.playSong();
                } else {
                    shortSongSound.playSong();
                }
            }
        } else {

            musicOffButton.draw();

            if (musicOffButton.isClicked()) {
                Sound.music = true;
                soundPlayer.playClickSound();

                if (longSong) {
                    songSound.playSong();
                } else {
                    shortSongSound.playSong();
                }
            }
        }
    }

    public void startClient(String ipInput) throws Exception {
        game = new Game(gs, eventBus, ipInput);
        client = game.initializeClient();
    }

    public void uiLoop() {
        GL.createCapabilities();

        // map openGL coordinates to pixel coordinates using orthographic camera
        // bottom left (the origin) is 0 0
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, WINDOW_WIDTH, 0.0f, WINDOW_HEIGHT, 0.0f, 1.0f);

        // Set colour
        glClearColor(0.5f, 0.5f, 0.0f, 0.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        String menuBackgroundTexture = "Menu Background";
        Sprite.draw(new Vector(0, 0), Texture.create(menuBackgroundTexture)); // background image drawn now so appears
        // before anything else is loaded

        Scene titleScene = new TitleScene(this);
        Scene menuScene = new MenuScene(this);
        Scene settingsScene = new SettingsScene(this);
        Scene controlsScene = new ControlsScene(this);
        Scene joinGameScene = new JoinGameScene(this);
        selectCharacterScene = new SelectCharacterScene(this);
        Scene confirmScene = new ConfirmScene(this, false, false);
        Scene youDiedScene = new YouDiedScene(this);
        Scene levelOverScene = new LevelOverScene(this);
        Scene confirmScene2 = new ConfirmScene(this, true, false);
        Scene enterNameScene = new EnterNameScene(this);
        Scene selectLevelScene = new SelectLevelScene(this);
        Scene confirmScene3 = new ConfirmScene(this, false, true);

        setTitleKeybindings();

        // Run the rendering loop until the user has attempted to close or has pressed
        // ESCAPE
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glEnable(GL_TEXTURE_2D);

            // clear the frame buffer
            glClearColor(0.8f, 0.5f, 0.0f, 0.0f);

            Sprite.draw(new Vector(0, 0), Texture.create(menuBackgroundTexture)); // background image

            switch (currentScene) { // chooses which scene method to run based on current scene
                case TITLE:
                    titleScene.draw();
                    break;
                case MENU:
                    menuScene.draw();
                    break;
                case SETTINGS:
                    settingsScene.draw();
                    break;
                case JOIN_GAME:
                    joinGameScene.draw();
                    break;
                case SELECT_PLAYER:
                    selectCharacterScene.draw();
                    break;
                case CONFIRM:
                    confirmScene.draw();
                    break;
                case CONTROLS:
                    controlsScene.draw();
                    break;
                case YOU_DIED: // this scene is never drawn. Perhaps it would have been used if we had more time
                    youDiedScene.draw();
                    break;
                case LEVEL_OVER:
                    levelOverScene.draw();
                    break;
                case IN_GAME:
                    game.run(this);
                    glTranslatef(translation, 0, 0);
                    break;
                case CONFIRM2:
                    confirmScene2.draw();
                    break;
                case ENTER_NAME:
                    enterNameScene.draw();
                    break;
                case SELECT_LEVEL:
                    selectLevelScene.draw();
                    break;
                case CONFIRM3:
                    confirmScene3.draw();
                    break;
            }

            // these two methods clear what is on the screen and set what should be
            // displayed in the next loop
            glfwSwapBuffers(window);

            glfwPollEvents();

            // this checks if the user has tried to close the window. it calls the confirm
            // scene to see if they've changed their mind
            // if the user doesn't change their mind the window closes. if they do then they
            // return to the menu
            if (glfwWindowShouldClose(window) && !exit) {
                currentScene = CONFIRM;
                GLFW.glfwSetWindowShouldClose(window, false);
            }
        }
        endSounds();
        soundPlayer.endSounds();
        gs.close();
        System.exit(0); // this makes sure the client closes
    }

    public void endSounds() {
        if (longSong) {
            songSound.endSound();
        }
        shortSongSound.endSound();
    }
    
    public void playClickSound() {
        soundPlayer.playClickSound();
    }

    /**
     * This method prints text in the window. The colours match the colour of the
     * boxes on the select character scene
     *
     * @param text        The string to be displayed
     * @param x           The x coordinate to be multiplied by the scale factor
     * @param y           The y coordinate to be multiplied by the scale factor
     * @param scaleFactor How scaled the size and position of the string should be
     * @param colour      The colour the string should be. 0 = white, 1 = blue, 2 =
     *                    green, 3 = red, 4 = yellow
     */
    public void printText(String text, float x, float y, float scaleFactor, int colour) {
        switch (colour) { // 0 = white, 1 = blue, 2 = green, 3 = red, 4= yellow
            case 0:
                glColor3f(255f / 255f, 255f / 255f, 255f / 255f);
                break;
            case 1:
                glColor3f(0f / 255f, 0f / 255f, 255f / 255f);
                break;
            case 2:
                glColor3f(0f / 255f, 112f / 255f, 0f / 255f);
                break;
            case 3:
                glColor3f(255f / 255f, 0f / 255f, 0f / 255f);
                break;
            case 4:
                glColor3f(255f / 255f, 255f / 255f, 0f / 255f);
                break;
        }
        ByteBuffer buffer = BufferUtils.createByteBuffer(15000);
        int quads = stb_easy_font_print(0, 0, text, null, buffer);
        glVertexPointer(2, GL_FLOAT, 16, buffer); // basically says that buffer1 is what we should be using for
        // rendering
        glDisable(GL_TEXTURE_2D);
        glEnableClientState(GL_VERTEX_ARRAY);
        glPushMatrix();

        // zoom, and multiply y by -1 so y axis is flipped
        glScalef(scaleFactor, -1 * scaleFactor, 0f);
        // but now y coord when translating is inverted
        glTranslatef(x, -y, 0f);

        glDrawArrays(GL_QUADS, 0, quads * 4);

        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
    }

    public static double getMouseX() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, posX, null);
        return posX.get(0);
    }

    public static double getMouseY() {
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, null, posY);
        return posY.get(0);
    }

    public static Vector getMousePositionWithInvertedY() {
        return new Vector((int) getMouseX(), WINDOW_HEIGHT - (int) getMouseY());
    }

    /**
     * Determines which scene should displayed when the UI loop restarts
     *
     * @param nextScene The scene to switch to
     */
    public void setScene(SceneType nextScene) {
        currentScene = nextScene;
    }

    /**
     * This method sets the callback for the keyboard for the controls when in the
     * game lobby
     */
    public void setLobbyKeybindings() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) { // whenever a button is pressed or held down
                switch (key) {
                    case GLFW_KEY_W:
                        eventBus.trigger(new MoveEvent(Direction.JUMP));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.JUMP)));
                        break;

                    case GLFW_KEY_A:
                        eventBus.trigger(new MoveEvent(Direction.LEFT_PRESSED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.LEFT_PRESSED)));
                        break;

                    case GLFW_KEY_D:
                        eventBus.trigger(new MoveEvent(Direction.RIGHT_PRESSED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.RIGHT_PRESSED)));
                        break;
                    case GLFW_KEY_ENTER:
                        setLevelKeybindings();
                        eventBus.trigger(new OutgoingEvent(new StartGameEvent()));
                }
            }

            if (action == GLFW_RELEASE) { // when the button is let go
                switch (key) {
                    case GLFW_KEY_A:
                        eventBus.trigger(new MoveEvent(Direction.LEFT_RELEASED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.LEFT_RELEASED)));
                        break;

                    case GLFW_KEY_D:
                        eventBus.trigger(new MoveEvent(Direction.RIGHT_RELEASED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.RIGHT_RELEASED)));
                        break;

                    case GLFW_KEY_ESCAPE: // this sets the currentScene variable to CONFIRM2
                                          // so the confirm scene can be
                        // displayed but it'll be the in game version
                        game.loop = false; // the game loop will stop so the ui loop can
                                           // continue however the server
                        // will continue to update the game state
                        setScene(CONFIRM3);
                        break;
                    case GLFW_KEY_M:
                        if (Sound.music) {
                            Sound.music = false;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        } else {
                            Sound.music = true;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        }
                }
            }
        });
    }

    /**
     * This method sets the callback for the keyboard for the in game controls
     */
    public void setLevelKeybindings() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) { // whenever a button is pressed or held down
                switch (key) {
                    case GLFW_KEY_W:
                        eventBus.trigger(new MoveEvent(Direction.JUMP));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.JUMP)));
                        break;

                    case GLFW_KEY_A:
                        eventBus.trigger(new MoveEvent(Direction.LEFT_PRESSED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.LEFT_PRESSED)));
                        break;

                    case GLFW_KEY_D:
                        eventBus.trigger(new MoveEvent(Direction.RIGHT_PRESSED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.RIGHT_PRESSED)));
                        break;
                }
            }

            if (action == GLFW_RELEASE) { // when the button is let go
                switch (key) {
                    case GLFW_KEY_A:
                        eventBus.trigger(new MoveEvent(Direction.LEFT_RELEASED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.LEFT_RELEASED)));
                        break;

                    case GLFW_KEY_D:
                        eventBus.trigger(new MoveEvent(Direction.RIGHT_RELEASED));
                        // eventBus.trigger(new OutgoingEvent(new MoveEvent(Direction.RIGHT_RELEASED)));
                        break;

                    case GLFW_KEY_ESCAPE: // this sets the currentScene variable to CONFIRM2
                                          // so the confirm scene can be
                        // displayed but it'll be the in game version
                        game.loop = false; // the game loop will stop so the ui loop can
                                           // continue however the server
                        // will continue to update the game state
                        setScene(CONFIRM2);
                        break;
                    case GLFW_KEY_M:
                        if (Sound.music) {
                            Sound.music = false;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        } else {
                            Sound.music = true;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        }
                }

            }
        });
    }

    /**
     * key bindings to allow the enter button to be used on the title scene
     */
    public void setTitleKeybindings() {
        // System.out.println("Setting key bindings");
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // System.out.println("key pressed");
            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_ENTER:
                        setScene(MENU);
                        setMenuKeybindings();
                        break;
                    case GLFW_KEY_ESCAPE:
                        // game.loop = false;
                        setScene(CONFIRM);
                        break;
                    case GLFW_KEY_M:
                        if (Sound.music) {
                            Sound.music = false;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        } else {
                            Sound.music = true;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        }
                        break;
                }
            }
        });
    }

    /**
     * keybinding so the escape key can call the confirm scene in order to close the
     * window
     */
    public void setMenuKeybindings() {
        // System.out.println("Setting key bindings");
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // System.out.println("key pressed");
            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_ESCAPE:
                        // game.loop = false;
                        if (!(currentScene == CONFIRM2 || currentScene == CONFIRM3)) {
                            currentScene = CONFIRM;
                        }
                        break;
                    case GLFW_KEY_M:
                        if (Sound.music) {
                            Sound.music = false;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        } else {
                            Sound.music = true;
                            if (longSong) {
                                songSound.playSong();
                            } else {
                                shortSongSound.playSong();
                            }
                        }
                        break;
                }
            }
        });
    }

    public EventBus eventBus() {
        return this.eventBus;
    }

    public void reset() {
        game.destroy();
        this.availableColors = new HashSet<>();
        this.characterNames = new HashMap<>();
        this.selectCharacterScene = new SelectCharacterScene(this);
    }

    @Override
    public void notify(Event<?> event) {
        if (event instanceof IncomingEvent) {
            handleIncomingEvent((IncomingEvent) event);
        }
    }

    private void handleIncomingEvent(IncomingEvent event) {
        Event unwrapped = event.get();
        if (unwrapped instanceof AvailableColorsChangeEvent) {
            this.availableColors = ((AvailableColorsChangeEvent) unwrapped).get();
        } else if (unwrapped instanceof CharacterNamesEvent) {
            this.characterNames = ((CharacterNamesEvent) unwrapped).getNames();
        }
    }

    /**
     * @return the game which has been constructed so its loop can be started or
     *         ended from other classes
     */
    public Game getGame() {
        return game;
    }
}
