/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31) 
 * Stefano Forti - 481183
 */
package GossipRegistry;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Stefano Forti
 */
public class GossipRegistryProxy implements Serializable {

    private static final long serialVersionUID = 1L;

    private int port;

    public int getPort() {
        return port;
    }
    private InetAddress ip;

    public InetAddress getInetAddress() {
        return ip;
    }
    private int usersNumber;

    public void incUsersNumber() {
        usersNumber++;
    }

    public void decUsersNumber() {
        usersNumber--;
    }

    public int getUsersNumber() {
        return usersNumber;
    }

    public GossipRegistryProxy(InetAddress a, int p) {
        ip = a;
        port = p;
        usersNumber = 0;
    }
}
