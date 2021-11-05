package sabrewulf.events;

public class SetNameEvent implements Event<String> {

    /**
     *
     */
    private static final long serialVersionUID = 8079933710807416323L;
    private String name;

    public SetNameEvent(String name) {
        this.name = name;
    }

    @Override
    public String get() {
        return name;
    }

    public String getName() {
        return get();
    }

}