package sabrewulf.ui.scenes;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.UI;

public class ControlsScene extends Scene {
    private String controlListTexture,
            jumpTexture,
            leftTexture,
            rightTexture,
            exitTexture,
            wTexture,
            aTexture,
            dTexture,
            escTexture,
            mTexture,
            muteTexture,
            enterTexture,
            startTexture;

    public ControlsScene(UI ui) {
        super(ui);

        controlListTexture = "Controls2";
        jumpTexture = "Jump";
        leftTexture = "Left";
        rightTexture = "Right";
        startTexture = "Start from Lobby";
        muteTexture = "Mute Music";
        exitTexture = "Exit Game";

        wTexture = "W";
        aTexture = "A";
        dTexture = "D";
        enterTexture = "Enter Key";
        mTexture = "M";
        escTexture = "Esc";
    }

    public void draw() {

        ui.drawSoundButtons();

        ui.backButton.draw();

        Sprite.draw(new Vector(545, 660), Texture.create(controlListTexture));

        Sprite.draw(new Vector(360, 505), Texture.create(jumpTexture));
        Sprite.draw(new Vector(360, 330), Texture.create(leftTexture));
        Sprite.draw(new Vector(360, 155), Texture.create(rightTexture));

        Sprite.draw(new Vector(210, 505), Texture.create(wTexture));
        Sprite.draw(new Vector(210, 330), Texture.create(aTexture));
        Sprite.draw(new Vector(210, 155), Texture.create(dTexture));

        Sprite.draw(new Vector(880, 505), Texture.create(startTexture));
        Sprite.draw(new Vector(880, 330), Texture.create(muteTexture));
        Sprite.draw(new Vector(880, 155), Texture.create(exitTexture));

        Sprite.draw(new Vector(736, 511), Texture.create(enterTexture));
        Sprite.draw(new Vector(730, 330), Texture.create(mTexture));
        Sprite.draw(new Vector(730, 155), Texture.create(escTexture));

        if (ui.backButton.isClicked()) {
            glfwSwapBuffers(UI.window);
            glfwPollEvents();
            ui.playClickSound();
            changeScene(SceneType.SETTINGS);
        }
    }
}
