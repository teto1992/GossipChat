/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import GossipRegistry.GossipRegistryIF;
import UserAgent.Message;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Forti
 */
public class GossipProxyServer extends UnicastRemoteObject {

    private static final long serialVersionUID = 1L;

    private static int port = 5025;
    private static InetAddress ip;
    private static ConcurrentLinkedQueue<Message> msgs;
    private static final String ADDRESS = "127.0.0.1";

    public GossipProxyServer() throws RemoteException {

    }
    
    public GossipProxyServer(int p) throws RemoteException {
        port = p;
    }

    public static void main(String[] args) throws RemoteException, IOException {

        ip = InetAddress.getLocalHost();
        msgs = new ConcurrentLinkedQueue<>();

        try {
            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
            int res = stub.proxyReg(ip, port);
            Executor myPool = Executors.newCachedThreadPool();

            myPool.execute(new GossipUDPProxyReceiverThread(port, msgs));
            myPool.execute(new GossipTCPProxySenderServer(msgs));

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(GossipProxyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
