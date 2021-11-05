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
import sabrewulf.ui.Sound;
import sabrewulf.ui.UI;

public class SettingsScene extends Scene {
    private Button upButton, downButton, controlsButton;
    private String volumeTexture, maxVolTexture, minVolTexture;

    public SettingsScene(UI ui) {
        super(ui);

        upButton =
                new AnimatedButton(
                        new Vector(898, 563),
                        new Vector(120, 120),
                        new ButtonAnimation("Up", "Up hover", "Up press"));

        downButton =
                new AnimatedButton(
                        new Vector(424, 563),
                        new Vector(120, 120),
                        new ButtonAnimation("Down", "Down hover", "Down press"));

        controlsButton =
                new AnimatedButton(
                        new Vector(545, 325),
                        new Vector(350, 120),
                        new ButtonAnimation("Controls", "Controls hover", "Controls press"));

        volumeTexture = "Volume";
        maxVolTexture = "Maximum";
        minVolTexture = "Minimum";
    }

    public void draw() {

        ui.drawSoundButtons();

        ui.backButton.draw();

        controlsButton.draw();
        Sprite.draw(new Vector(546, 563), Texture.create(volumeTexture));

        if (Sound.getVolume() == 1.0f) {
            Sprite.draw(new Vector(898, 563), Texture.create(maxVolTexture));
            downButton.draw();
            if (downButton.isClicked()) {
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                ui.playClickSound();
                Sound.volumeDown();
            }
        } else if (Sound.getVolume() == 0.0f) {
            Sprite.draw(new Vector(424, 563), Texture.create(minVolTexture));
            upButton.draw();
            if (upButton.isClicked()) {
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                ui.playClickSound();
                Sound.volumeUp();
            }
        } else {
            upButton.draw();
            downButton.draw();
            if (upButton.isClicked()) {
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                ui.playClickSound();
                Sound.volumeUp();
            }

            if (downButton.isClicked()) {
                glfwSwapBuffers(UI.window);
                glfwPollEvents();
                ui.playClickSound();
                Sound.volumeDown();
            }
        }

        if (ui.backButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            changeScene(SceneType.MENU);
        }

        if (controlsButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            changeScene(SceneType.CONTROLS);
        }
    }
}
