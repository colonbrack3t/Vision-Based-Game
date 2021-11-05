package sabrewulf.game;

import java.util.HashSet;
import java.util.Set;

import sabrewulf.App;
import sabrewulf.events.LevelOverEvent;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.ui.Sound;
import sabrewulf.ui.SoundPlayer;
import sabrewulf.ui.UI;

public class Exit extends GameObject {
    /**
     *
     */
    private static final long serialVersionUID = -5813841823849718468L;
    private static final String texture = "exit";
    private Set<Character> playersFinished = new HashSet<Character>();
    private static Boolean first = true;

    /**
     * Constructor. This is the Exit of the level, when a Character touches this
     * gameobject they have successfully finished the level
     *
     * @param position
     * @param dimensions
     */
    public Exit(Vector position, Vector dimensions) {
        super(position, dimensions, Color.NEUTRAL, texture);
    }

    public static Exit generate(int x, int level) {
        first = true;
        int PLATFORM_HEIGHT = 70;
        int PLATFORM_WIDTH = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540 };
        return new Exit(new Vector(x * PLATFORM_WIDTH, levelCoords[level] + PLATFORM_HEIGHT), new Vector(20, 20));
    }

    /**
     * If collision is with character: Stops game, loads end screen once all players have collided
     * 
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        // TODO: load end screen
        Character c = (Character) obj;
        if (!playersFinished.contains(c)) {
            playersFinished.add(c);
        }

        // checks the number of players who have reached the end is the same as the number of clients on the server
        // only checks on the host machine as they are running the GameServer
        if (UI.isHost && playersFinished.size() == GameServer.server.clients.size() && first) {
            System.out.println("end");
            App.ui.eventBus().trigger(new OutgoingEvent(new LevelOverEvent()));
            playersFinished.clear();
            first = false;
        }

    }
}
