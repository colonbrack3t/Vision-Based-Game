package sabrewulf.networking.client;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import sabrewulf.networking.NetworkConstants;
import sabrewulf.networking.Utils;

public class ServerListener implements Runnable {

    private static final int LISTENING_ON_PORT = 9999;

    private Client client;

    private DatagramSocket socket;

    private Thread listener;
    public boolean listening;

    public ServerListener(Client client) {
        this.client = client;
    }

    public void listen() {
        try {
            socket = new DatagramSocket(LISTENING_ON_PORT);
            listener = new Thread(this);
            listener.start();
        } catch (Exception e) {
            Utils.error("Couldn't open UDP Socket to listen.. [ Client ]");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handlePacket(byte[] buffer) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            client.handleEvent(obj);
        } catch (EOFException e) {
            Utils.error("YOU NEED TO INCREASE THE BUFFER SIZE");
            System.exit(1);
        } catch (Exception e) {
            Utils.error("Couldn't handle packet..");
            e.printStackTrace();
        }
    }

    public void close() {
        socket.close();
        System.out.println("ServerListener.close()");
    }

    @Override
    public void run() {
        listening = true;
        Utils.client("listening for comms from the server");
        while (listening) {
            try {
                byte[] buffer = new byte[NetworkConstants.MAX_BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // this method is blocking while waiting to receive a packet
                handlePacket(buffer);
            } catch (IOException e) {
                Utils.error(e.toString());
                e.printStackTrace();
            }
            // System.out.println(listening);
        }
        close();
    }
}
