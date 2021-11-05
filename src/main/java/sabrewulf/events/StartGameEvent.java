package sabrewulf.events;

public class StartGameEvent implements Event<String> {

    /** */
    private static final long serialVersionUID = -7728760076304913479L;

    public String get() {
        return "start";
    }
}
