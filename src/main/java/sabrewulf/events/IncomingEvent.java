package sabrewulf.events;

public class IncomingEvent implements Event<Event> {
    private static final long serialVersionUID = 5472397856714787030L;

    private Event event;
    private String clientIp;

    public IncomingEvent(Event event, String clientIp) {
        this.event = event;
        this.clientIp = clientIp;
    }

    public String getClientIP() {
        return clientIp;
    }

    @Override
    public Event get() {
        return event;
    }
}
