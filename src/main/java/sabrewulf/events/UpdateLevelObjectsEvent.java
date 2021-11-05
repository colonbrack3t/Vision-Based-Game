package sabrewulf.events;

import java.util.List;
import sabrewulf.game.GameObject;

public class UpdateLevelObjectsEvent implements Event<List<GameObject>> {

    /** */
    private static final long serialVersionUID = 5621514349994263275L;

    private List<GameObject> gameObjects;

    public UpdateLevelObjectsEvent(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    @Override
    public List<GameObject> get() {
        return gameObjects;
    }

    public List<GameObject> getGameObjects() {
        return get();
    }
}
