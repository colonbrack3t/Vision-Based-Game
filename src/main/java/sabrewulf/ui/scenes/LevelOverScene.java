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

public class LevelOverScene extends Scene {
    private Button nextLevelButton, menuButton;
    private String levelOverTexture, yourTimeTexture;

    public LevelOverScene(UI ui) {
        super(ui);

        nextLevelButton =
                new AnimatedButton(
                        new Vector(545, 350),
                        new Vector(350, 120),
                        new ButtonAnimation("Next Level", "Next Level hover", "Next Level press"));

        menuButton =
                new AnimatedButton(
                        new Vector(545, 200),
                        new Vector(350, 120),
                        new ButtonAnimation("Continue", "Continue hover", "Continue press"));

        levelOverTexture = "Level Completed";

        yourTimeTexture = "YOUR TIME";
    }

    /**
     * This scene is shown when all players have reached the exit. 
     */
    public void draw() {

        int minutes = ui.timeTaken / 60;
        int seconds = ui.timeTaken % 60;
        String mins = "";
        String secs = "";

        if (minutes < 10) {
            mins = "0" + minutes;
        } else {
            mins = String.valueOf(minutes);
        }

        if (seconds < 10) {
            secs = "0" + seconds;
        } else {
            secs = String.valueOf(seconds);
        }

        String time = mins + ":" + secs;
        ui.printText(time, 196.0f, 142.0f, 4.0f, 0);
        Sprite.draw(new Vector(480, 500), Texture.create(yourTimeTexture));
        nextLevelButton.draw();
        menuButton.draw();
        Sprite.draw(new Vector(545, 600), Texture.create(levelOverTexture));

        if (nextLevelButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();

            if (UI.isHost) { // the host is taken to the select level scene to choose which level to play next
                changeScene(SceneType.SELECT_LEVEL);
            } else { // everyone is sent back to the lobby to wait for the next level to be chosen
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            }
        }

        if (menuButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            ui.client.close();
        	if (UI.isHost) {
        		// close server
        		UI.isHost = false;
        	}
            changeScene(SceneType.MENU);
        }
    }
}
