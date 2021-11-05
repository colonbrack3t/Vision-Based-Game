package sabrewulf.ui;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;

public class ButtonAnimation {

    private String normalTexture, hoverTexture, pressedTexture;

    public ButtonAnimation(String normalTexture, String hoverTexture, String pressedTexture) {
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.pressedTexture = pressedTexture;
    }

    public void drawNormal(Vector position) {
        Sprite.draw(position, Texture.create(normalTexture));
    }

    public void drawHover(Vector position) {
        Sprite.draw(position, Texture.create(hoverTexture));
    }

    public void drawPressed(Vector position) {
        Sprite.draw(position, Texture.create(pressedTexture));
    }
}
