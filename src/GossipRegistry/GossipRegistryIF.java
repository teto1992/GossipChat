/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete Laurea
 * Triennale in Informatica (L-31) 
 * Stefano Forti - 481183
 */
package GossipRegistry;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author Stefano Forti
 */
public interface GossipRegistryIF extends Remote {

    int register(String n) throws RemoteException;

    int unregister(String n) throws RemoteException;

    int allow(String a, String b) throws RemoteException;

    int disallow(String a, String b) throws RemoteException;

    int friend(String a, String b) throws RemoteException;

    int unfriend(String a, String b) throws RemoteException;

    int login(String n, InetAddress ip, int port, Object callback) throws RemoteException;

    int logout(String n) throws RemoteException;

    int proxyReg(InetAddress a, int p) throws RemoteException;

    public InetAddress getProxyAddress(String n) throws RemoteException;
}