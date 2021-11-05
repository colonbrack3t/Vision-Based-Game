package sabrewulf.events;

public class ClientJoinEvent implements Event<String> {
    /** */
    private static final long serialVersionUID = 4264198332855105618L;

    private String clientIp;

    public ClientJoinEvent(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String get() {
        return clientIp;
    }

    public String getClientIP() {
        return get();
    }
}
