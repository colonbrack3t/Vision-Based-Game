package sabrewulf.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import sabrewulf.App;
import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.EventListener;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.LevelOverEvent;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.events.SetLevelEvent;
import sabrewulf.events.StartTimeEvent;
import sabrewulf.events.UpdateLevelObjectsEvent;
import sabrewulf.networking.Utils;
import sabrewulf.ui.SceneType;

public class Client implements EventListener {

    private static final int SERVER_INBOUD_PORT = 8888;

    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private ServerListener serverListener;

    private EventBus eventBus;

    private Client(EventBus eventBus) {
        this.eventBus = eventBus;
        failed = 0;
        eventBus.subscribe(OutgoingEvent.class, this);
    }

    /**
     * @param ip
     * @return connected client, null if not
     * @throws UnknownHostException
     * @throws IOException
     */
    public static Client createAndConnect(String ip, EventBus eventBus) throws Exception {
        Client client = new Client(eventBus);
        client.connect(ip);
        return client;
    }

    protected void handleEvent(Object event) {
        // Utils.client("new object received from server [ " +
        // event.getClass().getName() + " ]");
        if (event instanceof Event) {
            if (event instanceof LevelOverEvent) {
                App.ui.endTime = System.currentTimeMillis();
                App.ui.timeTaken = (int) (App.ui.endTime - App.ui.startTime) / 1000;
                App.ui.game.loop = false;
                App.ui.setScene(SceneType.LEVEL_OVER);
                App.ui.soundPlayer.playFinishedSound();
                App.ui.soundPlayer.stopRunSound(true);
                App.ui.soundPlayer.stopSawSound(true);
                // App.ui.eventBus().trigger(new OutgoingEvent(new
                // SetLevelEvent("lobbyLevel")));
                // App.ui.timeEnd = true;
            } else if (event instanceof StartTimeEvent) {
                App.ui.startTime = System.currentTimeMillis();
            } else {
                eventBus.trigger(new IncomingEvent((Event) event, this.socket.getInetAddress().getHostAddress()));
            }
        } else {
            Utils.client("Cannot handle object of this type!");
        }
    }

    public void close() {
        try {
            eventBus.unsubscribe(OutgoingEvent.class, this);
            socket.close();
            serverListener.listening = false;
            serverListener.close();
        } catch (Exception e) {
            System.out.println("Failed to close client!");
            e.printStackTrace();
        }
    }

    private void goHome() {
        App.ui.game.loop = false;
        App.ui.setScene(SceneType.MENU);
    }

    private int failed;
    private long startTime;

    public void send(Object event) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(event);
            oos.flush();
            Utils.client("sent object [ " + event.getClass().getName() + " ] to the server !");
        } catch (SocketException e) {
            // e.printStackTrace();
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startTime > 8000 && startTime != 0) {
                System.out.println("closing after " + (System.currentTimeMillis() - startTime) + " millis");
                this.close();
                this.goHome();
            }
        } catch (Exception e) {
            Utils.error("client couldn't send object [ " + event.getClass().getName() + " ] to the server !");
            e.printStackTrace();
        }
    }

    public void connect(String ip) throws Exception {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), SERVER_INBOUD_PORT), 2000);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(is));
            String serverResponse = reader.readLine();
            Utils.client("response from server [ " + serverResponse + " ]");

            if (serverResponse.startsWith("Sorry")) {
                Utils.client("cannot join game right now, it's full!");
                socket.close();

                throw new Exception("Game full, cannot join!");
            } else {
                serverListener = new ServerListener(this);
                serverListener.listen();
            }
        } catch (SocketTimeoutException e) {
            this.close();
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void notify(Event<?> event) {
        if (event instanceof OutgoingEvent) {
            send(event.get());
        }
    }
}
