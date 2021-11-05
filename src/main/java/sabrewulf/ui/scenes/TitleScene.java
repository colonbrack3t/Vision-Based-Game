package sabrewulf.ui.scenes;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;
import sabrewulf.ui.UI;

public class TitleScene extends Scene {

    private String logoTexture, pressEnterTexture;

    public TitleScene(UI ui) {
        super(ui);
        logoTexture = "Logo";
        pressEnterTexture = "Press Enter";
    }

    @Override
    public void draw() {

        Sprite.draw(new Vector(440, 400), Texture.create(logoTexture));
        Sprite.draw(new Vector(545, 165), Texture.create(pressEnterTexture));
    }
}
