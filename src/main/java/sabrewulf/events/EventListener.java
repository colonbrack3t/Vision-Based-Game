package sabrewulf.events;

public interface EventListener {

    public void notify(Event<?> event);
}
