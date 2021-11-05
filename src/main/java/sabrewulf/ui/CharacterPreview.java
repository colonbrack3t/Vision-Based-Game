package sabrewulf.ui;

import sabrewulf.game.Vector;
import sabrewulf.render.Sprite;
import sabrewulf.render.Texture;

/** Field that shows */
public class CharacterPreview extends Button {
    private String characterTexture;
    private AnimatedButton characterFrame;
    private Vector characterPosition;

    public CharacterPreview(String characterTexture, AnimatedButton characterFrame) {
        super(characterFrame.position(), characterFrame.dimensions());

        this.characterTexture = characterTexture;
        this.characterPosition = characterFrame.position().add(102, 252); // character sprite is offset in its frame
        this.characterFrame = characterFrame;
    }

    public void draw(boolean taken) {
        if (taken) {
            Sprite.draw(characterPosition, Texture.create(characterTexture));
        } else {
            handleMouse();
            Sprite.draw(characterPosition, Texture.create(characterTexture));
            characterFrame.draw();
        }
    }
}
