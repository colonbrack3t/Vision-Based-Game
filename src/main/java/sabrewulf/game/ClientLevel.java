package sabrewulf.game;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.EventListener;
import sabrewulf.events.GameUpdateEvent;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.LevelOverEvent;
import sabrewulf.events.SetCameraRightBreakpointEvent;
import sabrewulf.events.MoveEvent;
import sabrewulf.events.SetClientColorEvent;
import sabrewulf.events.UpdateLevelObjectsEvent;
import sabrewulf.render.RenderInfo;

public class ClientLevel implements EventListener {

    private EventBus eventBus;
    private Map<String, GameObject> gameObjects;
    private static Color playerColor;
    private Character playerCharacter;

    public ClientLevel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.subscribe(IncomingEvent.class, this);
        this.eventBus.subscribe(SetClientColorEvent.class, this);
        this.eventBus.subscribe(MoveEvent.class, this);
        this.eventBus.subscribe(LevelOverEvent.class, this);
        this.gameObjects = new HashMap<>();
    }

    public void destroy() {
        this.eventBus.unsubscribe(IncomingEvent.class, this);
        this.eventBus.unsubscribe(SetClientColorEvent.class, this);
        this.eventBus.unsubscribe(MoveEvent.class, this);
        this.eventBus.unsubscribe(LevelOverEvent.class, this);
        this.gameObjects = new HashMap<>();
    }

    @Override
    public void notify(Event<?> event) {
        if (event instanceof IncomingEvent) {
            handleIncomingEvent((IncomingEvent) event);
        } else if (event instanceof SetClientColorEvent) {
            playerColor = ((SetClientColorEvent) event).getColor();
        } else if (event instanceof MoveEvent) {
            movePlayer(((MoveEvent) event).get());
        } else if (event instanceof LevelOverEvent) {
            playerCharacter.resetVision();
        }
    }

    public boolean update() {
        if (playerCharacter != null)
            playerCharacter.updatePosition(gameObjects.values().stream().collect(toList()));
        return playerCharacter != null;
    }

    private void movePlayer(Direction d) {
        if (playerCharacter != null)
            playerCharacter.move(d);
    }

    private void handleIncomingEvent(IncomingEvent event) {
        Event unwrapped = event.get();
        if (unwrapped instanceof GameUpdateEvent) {
            integrateGameUpdates(((GameUpdateEvent) unwrapped).get());
        } else if (unwrapped instanceof UpdateLevelObjectsEvent) {
            setLevelObjects(((UpdateLevelObjectsEvent) unwrapped).getGameObjects());
        }
    }

    private void setLevelObjects(List<GameObject> gameObjects) {
        this.gameObjects = new HashMap<>();

        for (GameObject gameObject : gameObjects) {
            this.gameObjects.put(gameObject.id(), gameObject);
            if (gameObject instanceof Character) {
                if (gameObject.type() == playerColor) {
                    playerCharacter = (Character) gameObject;
                }
            }
        }

        // find the GameObject which is the furthest from (0,0) on the x axis
        gameObjects.stream().max(Comparator.comparing(obj -> (obj.position().x() + obj.dimensions.x()))).ifPresent(
                obj -> eventBus.trigger(new SetCameraRightBreakpointEvent(obj.position().x() + obj.dimensions.x())));
    }

    public void integrateGameUpdates(Map<String, Object> updates) {
        for (String id : gameObjects.keySet()) {
            GameObject object = gameObjects.get(id);
            if (object instanceof Character) {
                Character newChar = (Character) updates.getOrDefault(object.id(), (Character) object);
                if (newChar.type() == playerColor) {
                    if (playerCharacter.getVision() > newChar.getVision())
                        playerCharacter.setVision(newChar.getVision());
                    continue;
                } else {
                    object = newChar;
                }
            } else if (object instanceof Saw) {
                object = (Saw) updates.getOrDefault(object.id(), (Saw) object);
            } else if (object instanceof MovingSpike) {
                object = (MovingSpike) updates.getOrDefault(object.id(), (MovingSpike) object);
            } else {
                Vector newPosition = (Vector) updates.getOrDefault(object.id(), object.position());
                object.setPosition(newPosition);
            }
            gameObjects.put(object.id(), object);
        }
    }

    public boolean ifBarred(Color ObjectColor) {
        switch (ObjectColor) {
            case INVISIBLE:
                return true;
            case REDBAR:
                return playerColor == Color.RED;
            case GREENBAR:
                return playerColor == Color.GREEN;
            case BLUEBAR:
                return playerColor == Color.BLUE;
            case YELLOWBAR:
                return playerColor == Color.YELLOW;
            default:
                return true;
        }
    }

    public List<RenderInfo> getRenderList() {
        return gameObjects.values().stream().filter(object -> object instanceof Character
                || object.type() == playerColor || !ifBarred(object.type()) || object.type() == Color.NEUTRAL)
                .map(object -> {
                    if (object instanceof Checkpoint) {
                        if (playerCharacter.getCheckpointHistory().contains((Checkpoint) object)) {
                            object.setTextureName("checkpoint");
                        }
                        return object;
                    } else
                        return object;
                }).map(GameObject::renderInfo).collect(toList());
    }

    public Vector getPlayerVelocity() {
        return this.playerCharacter.velocity();
    }

    public Vector getPlayerPosition() {
        return this.playerCharacter.position();
    }

    public static Color getPlayerColor() {
        return playerColor;
    }
}
