package sabrewulf.events;

/**
 * When a server receives this event it sets the level to the lobby and sends this event to all clients
 * When a client receives this event it finds the time taken for the level and stops the game loop
 * @author Matt
 *
 */
public class LevelOverEvent implements Event<String> {

    private static final long serialVersionUID = -7728760086304913479L;

    public String get() {
        return "level over";
    }
}
