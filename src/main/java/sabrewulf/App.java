package sabrewulf;

import java.net.SocketException;
import java.net.UnknownHostException;

import sabrewulf.events.EventBus;
import sabrewulf.game.Game;
import sabrewulf.game.GameServer;
import sabrewulf.render.GameScreen;
import sabrewulf.ui.SceneType;
import sabrewulf.ui.Sound;
import sabrewulf.ui.UI;

public class App {
    public static UI ui;

    public static void main(String[] args) throws SocketException, UnknownHostException {
        if (args.length > 0) {
            runShortcut(args[0]);
        } else {
            run();
        }
    }

    private static void run() {
        EventBus eventBus = new EventBus();

        GameScreen screen = GameScreen.create();
        Sound.init();
        ui = new UI(screen, eventBus);
        ui.uiLoop();
    }

    public static boolean serverMode = false;

    private static void runShortcut(String mode) {
        if (mode.equals("single")) {
            Sound.init();
            Thread gameServer = GameServer.createThread();
            gameServer.start();

            EventBus eventBus = new EventBus();
            GameScreen gameScreen = GameScreen.create();
            Game game = new Game(gameScreen, eventBus, "127.0.0.1");
            try {
                game.initializeClient();
            } catch (Exception e) {
                System.exit(1);
            }

            ui = new UI(gameScreen, eventBus);
            ui.setScene(SceneType.IN_GAME);
            game.run(ui);

        } else if (mode.equals("server")) {
            serverMode = true;
            GameServer.createThread().start();
        } else {
            System.out.println("Cannot understand " + mode);
        }
    }
}
