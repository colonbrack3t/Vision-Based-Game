package sabrewulf.ui.scenes;

import sabrewulf.ui.Drawable;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.UI;

public abstract class Scene implements Drawable {
    protected UI ui;

    public Scene(UI ui) {
        this.ui = ui;
    }

    protected void changeScene(SceneType type) {
        ui.setScene(type);
    }

    protected void playClickSound() {
        ui.playClickSound();
    }

    public UI ui() {
        return ui;
    }
}
