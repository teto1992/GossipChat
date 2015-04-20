/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import UserAgent.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Forti
 */
public class GossipTCPProxySenderServer extends Thread {

    ConcurrentLinkedQueue<Message> m;
    private ServerSocket serverSocket;

    public GossipTCPProxySenderServer(ConcurrentLinkedQueue<Message> msgs) throws IOException {

        m = msgs;

        try {
            serverSocket = new ServerSocket(5730);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5730.");
            System.exit(-1);
        }

    }

    @Override
    public void run() {
        System.out.println("Server TCP ready");
        System.out.println(m);
        Executor myPool = Executors.newCachedThreadPool();
        while (true) {
            try {
                myPool.execute(new GossipTCPSenderThread(m, serverSocket.accept()));
            } catch (IOException ex) {
                Logger.getLogger(GossipTCPProxySenderServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
