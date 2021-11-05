package sabrewulf.events;

public class OutgoingEvent implements Event<Event> {
    private static final long serialVersionUID = -4999786235781707715L;
    private Event event;

    public OutgoingEvent(Event event) {
        this.event = event;
    }

    @Override
    public Event get() {
        return event;
    }
}
