package sabrewulf.game;

import static java.util.stream.Collectors.toList;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sabrewulf.database.Database;

public class ServerLevel {

    private Vector startPosition = new Vector(400, 200);

    private Map<String, GameObject> gameObjects;

    private long levelStart;

    public ServerLevel() {
        this.gameObjects = new HashMap<>();
    }

    public void destroy() {
        this.gameObjects = null;
    }

    public void update() {
        for (GameObject gameObject : gameObjects.values()) {
            if (gameObject instanceof Character) {
                // if levelStart is not initialized it's the lobby level
                if (levelStart != 0) {
                    ((Character) gameObject).setElapsedTime(System.nanoTime() - levelStart);
                } else {
                    ((Character) gameObject).setElapsedTime(0);
                }
                ((Character) gameObject).updateOnServer(gameObjects.values().stream().collect(toList()));
                continue;
            }
            // TODO: maybe only pass a slice of the gameObjects (from currentIndex to end)
            gameObject.updatePosition(gameObjects.values().stream().collect(toList()));

        }
    }

    public void updateCharacter(Color type, Vector vel, Vector pos) {
        Character character = (Character) this.gameObjects.values().stream().filter(obj -> obj instanceof Character)
                .filter(c -> c.type() == type).findFirst().get();
        character.setVelocity(vel);
        character.setPosition(pos);
    }

    public void add(GameObject gameObject) {
        gameObjects.put(gameObject.id(), gameObject);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects.values().stream().collect(toList());
    }

    public Map<String, Object> generateGameUpdates() {
        Map<String, Object> updates = new HashMap<>();
        for (GameObject object : gameObjects.values()) {
            // TODO: here some caching can take place. If a position has not changed it does
            // not need to be included
            if (object instanceof Character) {
                updates.put(object.id(), object);
            } else if (object instanceof Saw) {
                updates.put(object.id(), object);
            } else if (object instanceof MovingSpike) {
                updates.put(object.id(), object);
            } else {
                updates.put(object.id(), object.position());
            }
        }
        return updates;
    }

    public void remove(GameObject obj) {
        gameObjects.remove(obj.id());
    }

    public void setStartTime() {
        this.levelStart = System.nanoTime();
    }

    /**
     * generates the level based on just the level name, generated from the database
     * 
     * @param levelName
     * @return
     */
    public static ServerLevel genLevel(String levelName) {
        if (levelName.equals("lobbyLevel")) {
            return lobbyLevel();
        }

        try {
            return Database.readLevel(levelName);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * @return the startPosition
     */
    public Vector getStartPosition() {
        return startPosition;
    }

    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(Vector startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * creates the lobby where all the characters wait for other players to join the
     * game
     * 
     * @return
     */
    public static ServerLevel lobbyLevel() {
        ServerLevel level = new ServerLevel();

        level.add(Platform.generate(0, 0, 34, Color.NEUTRAL));
        level.add(Wall.generate(-1, 0, 3, Color.NEUTRAL));
        level.add(Wall.generate(34, 0, 3, Color.NEUTRAL));
        level.add(Text.generate(3, 3, 3, Color.NEUTRAL, "Lobby"));

        return level;
    }
}
