package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import sabrewulf.game.GameServer;
import sabrewulf.game.Vector;
import sabrewulf.ui.AnimatedButton;
import sabrewulf.ui.Button;
import sabrewulf.ui.ButtonAnimation;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.UI;

public class MenuScene extends Scene {

    private Button createGameButton, joinGameButton, settingsButton, quitButton;

    public MenuScene(UI ui) {
        super(ui);

        createGameButton =
                new AnimatedButton(
                        new Vector(545, 591),
                        new Vector(350, 120),
                        new ButtonAnimation(
                                "Create Game", "Create Game hover", "Create Game press"));

        joinGameButton =
                new AnimatedButton(
                        new Vector(545, 416),
                        new Vector(350, 120),
                        new ButtonAnimation("Join Game", "Join Game hover", "Join Game press"));

        settingsButton =
                new AnimatedButton(
                        new Vector(545, 241),
                        new Vector(350, 120),
                        new ButtonAnimation("Settings", "Settings hover", "Settings press"));

        quitButton =
                new AnimatedButton(
                        new Vector(545, 66),
                        new Vector(350, 120),
                        new ButtonAnimation("Quit", "Quit hover", "Quit press"));
    }

    public void draw() {

        ui.drawSoundButtons();

        ui.backButton.draw();
        createGameButton.draw();
        joinGameButton.draw();
        settingsButton.draw();
        quitButton.draw();

        if (ui.backButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            ui.setTitleKeybindings();
            changeScene(SceneType.TITLE);
        }

        if (createGameButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            UI.isHost = true;
            GameServer gameServer = new GameServer();
            ui.ip = gameServer.initAndGetIp();
            ui.gameServerThread = new Thread(gameServer);
            ui.gameServerThread.start();
            UI.isHost = true;
            try {
                ui.startClient(ui.ip);
            }
            catch (Exception e) {
            	System.exit(1);
            }
            EnterNameScene.setKeyBindingsToTextField();
            changeScene(SceneType.ENTER_NAME);
        }

        if (joinGameButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            playClickSound();
            JoinGameScene.setKeyBindingsToTextField();
            changeScene(SceneType.JOIN_GAME);
        }

        if (settingsButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            changeScene(SceneType.SETTINGS);
        }

        if (quitButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            changeScene(SceneType.CONFIRM);
        }
    }
}
