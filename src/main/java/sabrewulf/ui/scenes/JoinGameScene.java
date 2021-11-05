package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.AnimatedButton;
import sabrewulf.ui.Button;
import sabrewulf.ui.ButtonAnimation;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.TextField;
import sabrewulf.ui.UI;

public class JoinGameScene extends Scene {

    private Button enterButton;
    private static TextField inputField;
    private String enterCodeTexture, invalidIPTexture;
    public boolean invalid = false;

    public JoinGameScene(UI ui) {
        super(ui);

        enterCodeTexture = "Enter Access Code";
        enterButton =
                new AnimatedButton(
                        new Vector(775, 250),
                        new Vector(195, 50),
                        new ButtonAnimation("Enter", "Enter hover", "Enter press"));

        inputField = new TextField(new Vector(470, 463), new Vector(500, 50), this, false);
        invalidIPTexture = "Invalid IP";
    }

    public static void setKeyBindingsToTextField() {
        inputField.setInputKeyBindings();
    }

    @Override
    public void draw() {
        ui.drawSoundButtons();

        ui.backButton.draw();

        Sprite.draw(new Vector(545, 563), Texture.create(enterCodeTexture));
        inputField.draw();

        enterButton.draw();
        if (invalid) {
            Sprite.draw(new Vector(545, 100), Texture.create(invalidIPTexture));
        }

        if (ui.backButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            inputField.clear();
            invalid = false;
            ui.setMenuKeybindings();
            changeScene(SceneType.MENU);
        }

        if (enterButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();

            ui.ip = inputField.text();
            try {
                ui.startClient(ui.ip);
                invalid = false;
                inputField.clear();
                EnterNameScene.setKeyBindingsToTextField();
                changeScene(SceneType.ENTER_NAME);
            } catch (Throwable e) {
                if (e == new Exception("Game full, cannot join!")) {
                    // show string saying game full else say invalid ip
                }
                System.out.println(e);
                invalid = true;
            }
        }
    }
}
