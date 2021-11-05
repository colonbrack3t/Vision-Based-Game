package sabrewulf.events;

public class ClientLeaveEvent implements Event<String> {

    /**
     *
     */
    private static final long serialVersionUID = -900521229299933017L;
    private String ip;

    public ClientLeaveEvent(String ip) {
        this.ip = ip;
    }

    public String get() {
        return this.ip;
    }

    public String getIp() {
        return get();
    }
}