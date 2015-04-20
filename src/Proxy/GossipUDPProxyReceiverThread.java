/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import UserAgent.GossipUDPReceiverThread;
import UserAgent.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Stefano Forti
 */
public class GossipUDPProxyReceiverThread extends Thread {

    private DatagramSocket socket;
    private ConcurrentLinkedQueue<Message> M;

    public GossipUDPProxyReceiverThread(int port, ConcurrentLinkedQueue<Message> msgs) {
        M = msgs;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            System.err.println("Could not use the specified port.");
        }
    }

    @Override
    public void run() {
        System.out.println("Server UDP - Ready");
        while (true) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String s = new String(buf);
                s = s.trim();
                JSONObject o = (JSONObject) JSONValue.parse(s);
                Message m = new Message((JSONObject) o);
                System.out.println(m.toString());

                M.add(m);

            } catch (IOException ex) {
                Logger.getLogger(GossipUDPReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
