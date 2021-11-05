package sabrewulf.ui;

import static org.lwjgl.glfw.GLFW.*;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.scenes.EnterNameScene;
import sabrewulf.ui.scenes.JoinGameScene;
import sabrewulf.ui.scenes.Scene;

public class TextField extends Button {
    private static String input = "";
    private Scene parentScene;
    private boolean name = false;

    public TextField(Vector position, Vector dimension, Scene parent, boolean type) {
        super(position, dimension, "Text Input");
        this.parentScene = parent;
        this.name = type;
        // textHover = new Sprite("Text Input hover");
        // the hover animation is not used right now, could be used to signify to long
        // input (like blinking when a key is pressed and input > 15)
        setInputKeyBindings();
    }

    public void draw() {
        // explicity not calling handleMouse here
        Sprite.draw(position, Texture.create(spriteTexture));

        if (name) {
            parentScene.ui().printText(input, 122.0f, 120.0f, 4.0f, 0);
        } else {
            parentScene.ui().printText(input, 122.0f, 125.0f, 4.0f, 0);
        }
    }

    public void setInputKeyBindings() {
        // allows you to check if caps lock is on
        glfwSetInputMode(UI.window, GLFW_LOCK_KEY_MODS, GLFW_TRUE);
        glfwSetKeyCallback(
                UI.window,
                (window, key, scancode, action, mods) -> {
                    if (action != GLFW_RELEASE) {
                        return;
                    }
                    if (name) { // depending on which textfield this is different keys are needed
                        addKeyToName(key, mods);
                    } else {
                        addKeyToIP(key, mods);
                    }
                });
    }

    /**
     * when entering the ip only numbers and full stops are needed
     *
     * @param key
     * @param mods
     */
    private void addKeyToIP(int key, int mods) {
        switch (key) {
            case GLFW_KEY_BACKSPACE:
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                }
                break;
            case GLFW_KEY_ENTER:
                // notify parent scene
                // can be reached as ui.currentScene
                // could be same as clicking enter button with mouse. don't really need
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                parentScene.ui().playClickSound();

                String ip = input;
                try {
                    parentScene.ui().startClient(ip);
                    ((JoinGameScene) parentScene).invalid = false;
                    input = "";
                    EnterNameScene.setKeyBindingsToTextField();
                    parentScene.ui().setScene(SceneType.ENTER_NAME);
                } catch (Exception e) {
                    if (e == new Exception("Game full, cannot join!")) {
                        // show string saying game full else say invalid ip
                    }
                    System.out.println(e);
                    ((JoinGameScene) parentScene).invalid = true;
                }
                break;
            case GLFW_KEY_ESCAPE:
                // glfwSetWindowShouldClose(UI.window, true);
                parentScene.ui().setScene(SceneType.CONFIRM);
                break;
            case GLFW_KEY_PERIOD: // checking for special characters
                if (input.length() < 19) {
                    input += ".";
                }
                return;

            default:
                if (input.length() < 19) {
                    input += Character.toLowerCase((char) key);
                }
        }
    }

    /**
     * when entering a name numbers and capital letters can be entered
     *
     * @param key
     * @param mods
     */
    private void addKeyToName(int key, int mods) {
        switch (key) {
            case GLFW_KEY_BACKSPACE:
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                }
                break;
            case GLFW_KEY_ENTER:
                // notify parent scene
                // can be reached as ui.currentScene
                // could be same as clicking enter button with mouse. don't really need
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                parentScene.ui().playClickSound();
                parentScene.ui().name = input;
                input = "";
                parentScene.ui().setMenuKeybindings();
                parentScene.ui().setScene(SceneType.SELECT_PLAYER);
                break;
            case GLFW_KEY_ESCAPE:
                // glfwSetWindowShouldClose(UI.window, true);
                parentScene.ui().setScene(SceneType.CONFIRM);
                break;
            case GLFW_KEY_PERIOD: // checking for special characters
                if (input.length() < 19) {
                    input += ".";
                }
                return;

            default:
                if (input.length() >= 19
                        || (key == GLFW_KEY_LEFT_SHIFT
                                || key == GLFW_KEY_RIGHT_SHIFT
                                || key == GLFW_KEY_CAPS_LOCK)) {
                    return;
                } else if (Character.isDigit(key)
                        || (Character.getType(key) == Character.LOWERCASE_LETTER)
                        || (Character.getType(key) == Character.UPPERCASE_LETTER)) {
                    input += (char) key;
                } else {
                    return;
                }
        }
    }

    public void clear() {
        input = "";
    }

    public String text() {
        return input;
    }

    // there is no need to handle the mouse in a text field
    // maybe when the textfield needs to be selected first...
    @Override
    public void handleMouse() {}
}
