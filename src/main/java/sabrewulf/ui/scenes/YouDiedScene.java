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
import sabrewulf.ui.UI;

public class YouDiedScene extends Scene {
    private Button restartButton, menuButton, nextLevelButton;
    private String youDiedTexture;

    public YouDiedScene(UI ui) {
        super(ui);

        restartButton =
                new AnimatedButton(
                        new Vector(570, 350),
                        new Vector(300, 300),
                        new ButtonAnimation("Restart", "Restart hover", "Restart press"));

        menuButton =
                new AnimatedButton(
                        new Vector(545, 200),
                        new Vector(350, 120),
                        new ButtonAnimation("Continue", "Continue hover", "Continue press"));

        nextLevelButton =
                new AnimatedButton(
                        new Vector(545, 50),
                        new Vector(350, 120),
                        new ButtonAnimation("Next Level", "Next Level hover", "Next Level press"));

        youDiedTexture = "You Died";
    }

    /**
     * This scene is never drawn. Perhaps it would have been used if we had more time
     */
    public void draw() {

        restartButton.draw();
        menuButton.draw();
        nextLevelButton.draw();
        Sprite.draw(new Vector(545, 660), Texture.create(youDiedTexture));

        if (restartButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            // changeScene(SceneType.NEW_GAME);
            ui.setLevelKeybindings();
            ui.setScene(SceneType.IN_GAME);
        }

        if (menuButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            ui.client.close();
        	if (UI.isHost) {
        		//close server
        		UI.isHost = false;
        	}
            changeScene(SceneType.MENU);
        }

        if (nextLevelButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();

            if (UI.isHost) {
                changeScene(SceneType.SELECT_LEVEL);
            } else {
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }
    }
}
