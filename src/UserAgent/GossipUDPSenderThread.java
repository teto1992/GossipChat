/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Stefano Forti
 */
public class GossipUDPSenderThread extends Thread {

    String m;
    InetAddress ip;
    int port;
    DatagramSocket socket;

    public GossipUDPSenderThread(JSONObject msg, InetAddress add, int p) throws SocketException {
        m = msg.toJSONString();
        ip = add;
        port = p;
        socket = new DatagramSocket();
    }

    public void run() {
        byte[] buf = new byte[256];

        buf = m.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(GossipUDPSenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket.close();

    }

}
