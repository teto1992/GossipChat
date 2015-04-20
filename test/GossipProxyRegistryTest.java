
import GossipRegistry.ProxiesDB;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefano
 */
public class GossipProxyRegistryTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        ProxiesDB pxs = new ProxiesDB();
        Executor myPool = Executors.newCachedThreadPool();
        int port = 4449;
        int i = 0;
        
        for (i = 0; i < 101; i++){
            port = port + 1;
            myPool.execute(new ProxyRegThread(pxs, port, null, InetAddress.getLocalHost()));
        }
        System.exit(0);
    }
    
}
