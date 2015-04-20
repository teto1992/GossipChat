
import GossipRegistry.ProxiesDB;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefano
 */
public class ProxyRegThread extends Thread {
    
    public ProxyRegThread(ProxiesDB pxs, int p, Object o, InetAddress a) throws UnknownHostException{
        pxs.addProxyServer(InetAddress.getLocalHost(), p);
        
    }
    
    
}
