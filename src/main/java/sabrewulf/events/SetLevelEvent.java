package sabrewulf.events;

public class SetLevelEvent implements Event<String> {

    /** */
    private static final long serialVersionUID = 2747371236228532363L;

    private String level;

    public SetLevelEvent(String level) {
        this.level = level;
    }

    public String get() {
        return level;
    }

    public String getLevel() {
        return get();
    }
}
