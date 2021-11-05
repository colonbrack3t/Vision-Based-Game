package sabrewulf.events;

public class GameOverEvent implements Event<String> {

    private static final long serialVersionUID = -7724760086304913479L;

    public String get() {
        return "game over";
    }
}
