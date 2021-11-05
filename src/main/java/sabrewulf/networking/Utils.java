package sabrewulf.networking;

public abstract class Utils {
    private static final boolean netService = false;
    private static final boolean server = false;
    private static final boolean client = false;
    private static final boolean error = true;

    private static final String NETWORK_SERVICE = "NETWORK SERVICE";
    private static final String SERVER = "SERVER";
    private static final String CLIENT = "CLIENT";
    private static final String ERROR = "ERROR";

    public static void error(Object msg) {
        if (error)
            write(ERROR, msg);
    }

    public static void netService(Object msg) {
        if (netService)
            write(NETWORK_SERVICE, msg);
    }

    public static void server(Object msg) {
        if (server)
            write(SERVER, msg);
    }

    public static void client(Object msg) {
        if (client)
            write(CLIENT, msg);
    }

    private static void write(String type, Object msg) {
        System.out.println(type + ": " + msg.toString());
    }
}
