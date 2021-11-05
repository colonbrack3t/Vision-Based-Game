package sabrewulf.networking.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import sabrewulf.networking.NetworkConstants;
import sabrewulf.networking.Utils;

public class Client {

    private Server server;

    private DatagramSocket udpSocket;
    public InetAddress clientAddress;
    private static final int CLIENT_INBOUND_PORT = 9999;

    private Socket tcpSocket;
    private InputStream is;

    private boolean listening;
    private Thread listener;

    public Client(Server server, DatagramSocket udpSocket, InetAddress clientAddress, Socket tcpSocket) {
        this.server = server;
        this.udpSocket = udpSocket;
        this.clientAddress = clientAddress;
        this.tcpSocket = tcpSocket;
    }

    private String lastSent;
    private int size = 0;

    public void send(Object event) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(NetworkConstants.MAX_BUFFER_SIZE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(event);
            byte[] buffer = baos.toByteArray();
            if (lastSent == null || !lastSent.equals(event.getClass().getName()) || size != buffer.length) {
                lastSent = event.getClass().getName();
                size = buffer.length;
                Utils.server("sending different obj [ " + lastSent + " ], size [ " + size + " ]");
            }
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress, CLIENT_INBOUND_PORT);
            udpSocket.send(packet);
            oos.close();
            baos.close();
        } catch (Exception e) {
            Utils.error("Couldn't send object [ " + event.getClass() + " ]");
            e.printStackTrace();
        }
    }

    protected void listen() {
        try {
            is = tcpSocket.getInputStream();
            listener = new Thread(new Listener());
            listener.start();
        } catch (Exception e) {
            Utils.error("Couldn't get the input stream to listen to client - stop unplugging that wire!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void close() {
        try {
            listening = false;
            is.close();
            tcpSocket.close();
            Utils.server("Client [ " + clientAddress + " ] closed");
        } catch (Exception e) {
            Utils.error("Couldn't close connection to client? Is something in the wrong order..?");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "Address [" + clientAddress.getHostAddress() + "]";
    }

    private void removeThis() {
        server.removeClient(this, clientAddress.getHostAddress());
    }

    class Listener implements Runnable {
        @Override
        public void run() {
            listening = true;
            while (listening) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(is);
                    Object obj = ois.readObject();
                    server.handleEvent(obj, clientAddress.getHostAddress());
                } catch (IOException | ClassNotFoundException e) {
                    Utils.error(e.toString());
                    Utils.server("likely client has disconnected [ " + clientAddress.getHostName() + " ]");
                    removeThis();
                    return;
                }
            }
        }
    }
}
