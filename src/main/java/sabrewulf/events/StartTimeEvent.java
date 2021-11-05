package sabrewulf.events;

public class StartTimeEvent implements Event<String> {

    /**
     * This event tells the clients to start recording the time taken for the level 
     */
    private static final long serialVersionUID = -7728761176304913479L;

    public String get() {
        return "time";
    }
}
