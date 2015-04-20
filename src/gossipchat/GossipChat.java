/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package gossipchat;

import GossipRegistry.GossipServerRMI;
import Proxy.GossipProxyServer;
import UserAgent.GossipUserAgent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.Executor;

/**
 *
 * @author Stefano
 */
public class GossipChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, IOException {
        if (args.length >= 1) {
            switch (args[0]) {
                case "registry":
                    new GossipServerRMI().main(null);
                    break;
                case "gossip":
                    new GossipUserAgent(Integer.parseInt(args[1])).main(null);
                    break;
                case "proxy":
                    new GossipProxyServer(Integer.parseInt(args[1])).main(null);
                    break;
                default:
                    System.out.println("Not existing command...");
                    break;
            }
        } else {
            System.out.println("Help:\n"
                    + "• java –jar gossipchat.jar registry\n"
                    + "• java –jar gossipchat.jar proxy <port>\n"
                    + "• java –jar gossipchat.jar gossip <port> \n");
        }
    }

}
