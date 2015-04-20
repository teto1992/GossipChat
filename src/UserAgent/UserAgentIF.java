/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import GossipRegistry.NicknameList;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Stefano Forti
 */
public interface UserAgentIF  extends Remote {
    
    public void updateLists(NicknameList a, NicknameList f) throws RemoteException;
    public void notify(String s)throws RemoteException;

}
