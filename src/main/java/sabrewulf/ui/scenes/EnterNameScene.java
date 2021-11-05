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

public class EnterNameScene extends Scene {

    private Button enterButton, menuButton;
    private static TextField inputField;
    private String enterNameTexture, yourIPTexture, welcomeTexture;

    public EnterNameScene(UI ui) {
        super(ui);

        enterNameTexture = "Set Name";
        yourIPTexture = "Your IP";
        welcomeTexture = "Welcome";
        enterButton =
                new AnimatedButton(
                        new Vector(775, 250),
                        new Vector(195, 50),
                        new ButtonAnimation("Enter", "Enter hover", "Enter press"));

        menuButton =
                new AnimatedButton(
                        new Vector(25, 730),
                        new Vector(195, 50),
                        new ButtonAnimation("Menu", "Menu hover", "Menu press"));

        inputField = new TextField(new Vector(470, 443), new Vector(500, 50), this, true);
    }

    public static void setKeyBindingsToTextField() {
        inputField.setInputKeyBindings();
    }

    @Override
    public void draw() {
        ui.drawSoundButtons();

        menuButton.draw();

        Sprite.draw(new Vector(545, 533), Texture.create(enterNameTexture));
        inputField.draw();

        enterButton.draw();

        if (UI.isHost) {
            Sprite.draw(new Vector(0, 638), Texture.create(yourIPTexture));
            ui.printText(ui.ip, 88.0f, 176.25f, 4.0f, 0);
        } else {
            Sprite.draw(new Vector(545, 638), Texture.create(welcomeTexture));
        }

        if (menuButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            inputField.clear();
            ui.setMenuKeybindings();
            ui.client.close();
        	if (UI.isHost) {
        		//close server
        		UI.isHost = false;
        	}
            changeScene(SceneType.MENU);
        }

        if (enterButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            ui.name = inputField.text();
            inputField.clear();
            ui.setMenuKeybindings();
            changeScene(SceneType.SELECT_PLAYER);
        }
    }
}
