package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import org.lwjgl.glfw.GLFW;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.AnimatedButton;
import sabrewulf.ui.Button;
import sabrewulf.ui.ButtonAnimation;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.SoundPlayer;
import sabrewulf.ui.UI;

public class ConfirmScene extends Scene {
    private Button yesButton, noButton;
    private String confirm;
    private boolean inGame, inLobby;

    /**
     * If this scene is called from the game the yes button quits to the menu <br>
     * If it's called from the menu the yes button closes the window
     * 
     * @param ui
     * @param ig true if the confirm scene is being called in game. false otherwise
     *           so can know what to do when yes button is pressed
     */
    public ConfirmScene(UI ui, boolean ig, boolean il) {
        super(ui);
        inGame = ig;
        inLobby = il;

        yesButton = new AnimatedButton(new Vector(666, 375), new Vector(108, 108),
                new ButtonAnimation("Yes", "Yes hover", "Yes press"));

        noButton = new AnimatedButton(new Vector(666, 181), new Vector(108, 108),
                new ButtonAnimation("No", "No hover", "No press"));

        confirm = "Confirm";

    }

    public void draw() {

        ui.drawSoundButtons();

        yesButton.draw();
        noButton.draw();
        Sprite.draw(new Vector(545, 563), Texture.create(confirm));

        if (yesButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            // changeScene(SceneType.NEW_GAME);
            if (inGame || inLobby) {
                ui.reset();
                ui.soundPlayer.stopRunSound(true);
                ui.soundPlayer.stopSawSound(true);
                if (UI.isHost) {
                    // close server
                    ui.client.close();
                    UI.isHost = false;
                    try {
                        ui.gameServerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ui.gameServerThread = null;
                } else {
                    // send event to remove client from server
                    ui.client.close();
                }
                changeScene(SceneType.MENU);
            } else {
                GLFW.glfwSetWindowShouldClose(UI.window, true);
                ui.exit = true;
            }
        }

        if (noButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            if (inGame) {
                ui.getGame().loop = true;
                ui.setLevelKeybindings();
                changeScene(SceneType.IN_GAME);
            } else if (inLobby) {
                ui.getGame().loop = true;
                if (UI.isHost) {
                    ui.setLobbyKeybindings();
                } else {
                    ui.setLevelKeybindings();
                }
                changeScene(SceneType.IN_GAME);
            } else {
                changeScene(SceneType.MENU);
            }
        }

    }
}
