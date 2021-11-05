package sabrewulf.utils;

public class Utils {

    private static final boolean LOG = true;
    private static final boolean INFO = true;
    private static final boolean SERVER = true;
    private static final boolean CLIENT = true;

    public static boolean log(Object obj) {
        if (LOG) System.out.println("LOG: " + obj);
        return true;
    }

    public static boolean info(Object obj) {
        if (INFO) System.out.println("INFO: " + obj);
        return true;
    }

    public static boolean server(Object obj) {
        if (SERVER) System.out.println("SERVER: " + obj);
        return true;
    }

    public static boolean client(Object obj) {
        if (CLIENT) System.out.println("CLIENT: " + obj);
        return true;
    }
}
