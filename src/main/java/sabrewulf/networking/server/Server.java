package sabrewulf.networking.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import sabrewulf.events.ClientJoinEvent;
import sabrewulf.events.ClientLeaveEvent;
import sabrewulf.events.Event;
import sabrewulf.events.EventBus;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.OutgoingEvent;
import sabrewulf.networking.Utils;

public class Server {

    private DatagramSocket udpSocket;
    private ServerSocket serverSocket;

    public ArrayList<Client> clients;
    private static final int MAX_CLIENTS = 4;

    private static final int INBOUND_PORT = 8888;

    private Thread clientListener;
    private boolean listening;

    private EventBus eventBus;

    private Server(EventBus eventBus) {
        boolean on = false;
        while (!on) {
            try {
                udpSocket = new DatagramSocket();
                serverSocket = new ServerSocket(INBOUND_PORT);
                clients = new ArrayList<>();
                this.eventBus = eventBus;
                on = true;
            } catch (Exception e) {
                Utils.error("failed to create server - that's no good.");
                e.printStackTrace();
            }
        }
    }

    public static Server create(EventBus eventBus) {
        return new Server(eventBus);
    }

    public String start() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            listen();
            return inet.getHostAddress();
        } catch (Exception e) {
            Utils.error("Could not start server - this is bad.");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public void close() {
        try {
            listening = false;
            // sendToAll(new CloseClientEvent(0));
            while (clients.size() != 0) {
                removeClient(clients.get(0), clients.get(0).clientAddress.getHostAddress());
            }
            udpSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error closing server");
            // System.exit(1);
        }
    }

    private void listen() {
        clientListener = new Thread(new ClientListener());
        clientListener.start();
    }

    public void sendToAll(Object event) {
        for (Client client : clients) {
            client.send(event);
        }
    }

    protected void removeClient(Client client, String ip) {
        eventBus.trigger(new ClientLeaveEvent(ip));
        clients.remove(client);
        client.close();
        client = null;
        Utils.server("now hosting [ " + clients.size() + " ] clients");
    }

    protected void handleEvent(Object event, String ipOrigin) {
        Utils.server("received object [ " + event.getClass().getName() + " ] from a client");

        if (event instanceof Event) {
            eventBus.trigger(new IncomingEvent((Event) event, ipOrigin));
        } else {
            Utils.server("Cannot handle object of this type!");
        }
    }

    private void addClient(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(os, true);
            pr.println("Cool, welcome to the party!");
            InetAddress clientAddress = socket.getInetAddress();
            Client client = new Client(this, udpSocket, clientAddress, socket);
            clients.add(client);
            client.listen();
            eventBus.trigger(new ClientJoinEvent(clientAddress.getHostAddress()));
        } catch (Exception e) {
            Utils.error("couldn't add client - this may well be a problem..");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void rejectClient(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(os, true);
            pr.println("Sorry, you can't come in right now.");
            pr.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            Utils.error("Couldn't even reject a client, something is up");
            e.printStackTrace();
            System.exit(1);
        }
    }

    class ClientListener implements Runnable {
        @Override
        public void run() {
            listening = true;
            Socket socket;
            while (listening) {
                socket = null;

                try {
                    Utils.server("waiting on new clients");
                    socket = serverSocket.accept();
                    Utils.server("new client!");
                    if (clients.size() == MAX_CLIENTS) {
                        rejectClient(socket);
                    } else {
                        addClient(socket);
                    }
                } catch (IOException e) {
                    Utils.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }
}
