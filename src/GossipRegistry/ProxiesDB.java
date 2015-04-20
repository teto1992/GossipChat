/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete Laurea
 * Triennale in Informatica (L-31) 
 * Stefano Forti - 481183
 */
package GossipRegistry;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Stefano Forti
 */
public class ProxiesDB {

    private int i;
    private ConcurrentHashMap<Integer, GossipRegistryProxy> m;

    public ProxiesDB() {
        m = new ConcurrentHashMap();
        i = 0;
    }

    /**
     * Adds a GossipRegistryProxy to the database
     * @param a the address associated with the proxy
     * @param p the port chosen to communicate
     * @return always 0 (up until now)
     */
    public synchronized int addProxyServer(InetAddress a, int p) {
        m.put(i, new GossipRegistryProxy(a, p));
        i++;
        return 0;
    }
    /**
     * Returns the index of the GossipRegistryProxy with the minimum number 
     * of users associated.
     * @return the index as described above.
     */
    public synchronized int getMinProxyIndex() {
        int min;
        int index = 0;

        min = m.get(index).getUsersNumber();

        for (Integer i : m.keySet()) {
            if (m.get(i).getUsersNumber() < min) {
                index = i;
                min = m.get(i).getUsersNumber();
            }
        }

        return index;
    }
/**
 * Increase the number of users registered to the i-th GossipRegistryProxy.
 * @param i 
 */
    public synchronized void incUsersNumber(int i) {
        m.get(i).incUsersNumber();
    }
/**
 * Decrease the number of users registered to the i-th GossipRegistryProxy.
 * @param i 
 */
    public synchronized void decUsersNumber(int i) {
        m.get(i).decUsersNumber();
    }
/**
 * Returns the i-th GossipRegistryProxy.
 * @param i 
 */
    public synchronized GossipRegistryProxy getProxy(int i) {
        return m.get(i);

    }

}
