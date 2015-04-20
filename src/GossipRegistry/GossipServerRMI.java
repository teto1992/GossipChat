/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package GossipRegistry;

import UserAgent.Message;
import UserAgent.UserAgentIF;
import java.io.Serializable;
import java.lang.String;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Forti
 */
public class GossipServerRMI extends UnicastRemoteObject implements GossipRegistryIF, Serializable {

    private static final long serialVersionUID = 1L;
    UsersDB users;
    ProxiesDB proxies;
    Executor myPool;

    public GossipServerRMI() throws RemoteException {
        users = new UsersDB();
        proxies = new ProxiesDB();
        myPool = Executors.newCachedThreadPool();
    }

    @Override
    /**
     * Registers the userID n.
     *
     */
    public int register(String n) throws RemoteException {
        int proxy = proxies.getMinProxyIndex();
        GossipRegistryProxy P = proxies.getProxy(proxy);
        //ProxyServerIF cbP = (ProxyServerIF) P.getCallback();
        int retVal = users.addUser(n);

        if (retVal == UsersDB.REGISTERED) {
            P.incUsersNumber();
            users.getUser(n).setProxyServer(proxy);
            users.getUser(n).setInet(P.getInetAddress());
            users.getUser(n).setPort(P.getPort());
        }

        return retVal;

    }

    @Override
    /**
     * Unregisters the userID n.
     *
     */
    public int unregister(String n) throws RemoteException {
        return users.removeUser(n);
    }

    /**
     * Adds b to a's friendlist and callbacks a.
     *
     * @param a
     * @param b
     * @return
     * @throws RemoteException
     */
    @Override
    public int friend(String a, String b) throws RemoteException {
        int retVal = users.friend(a, b);
        UserAgentIF cb = (UserAgentIF) users.getUser(a).getCallback();
        cb.updateLists(users.getUser(a).getAllowedList(), users.getUser(a).getFriendList());
        return retVal;
    }

    /**
     * Removes b from a's friendList and callbacks a.
     *
     * @param a
     * @param b
     * @return
     * @throws RemoteException
     */
    @Override
    public int unfriend(String a, String b) throws RemoteException {
        int retVal = users.unfriend(a, b);
        if (retVal == UsersDB.LOGGEDOUT) {
            UserAgentIF cb = (UserAgentIF) users.getUser(a).getCallback();
            cb.updateLists(users.getUser(a).getAllowedList(), users.getUser(a).getFriendList());
        }
        return retVal;
    }

    /**
     * Logs in n into the system and notifies n's friends calling them back.
     *
     * @param n
     * @param ip
     * @param port
     * @param callback
     * @return
     * @throws RemoteException
     */
    @Override
    public int login(String n, InetAddress ip, int port, Object callback) throws RemoteException {
        int retVal = users.login(n, ip, port, callback);
        if (retVal == UsersDB.LOGGEDIN) {
            UserAgentIF cb = (UserAgentIF) users.getUser(n).getCallback();
            int proxy = users.getUser(n).getProxyServer();
            if (proxy != -1) {
                proxies.getProxy(proxy).decUsersNumber();
            }
            cb.updateLists(users.getUser(n).getAllowedList(), users.getUser(n).getFriendList());
            for (Nickname nick : users.getUser(n).getFriendList().list) {
                if (nick.getStatus() == Nickname.ONLINE) {
                    cb = (UserAgentIF) users.getUser(nick.getUserID()).getCallback();
                    cb.updateLists(users.getUser(nick.getUserID()).getAllowedList(), users.getUser(nick.getUserID()).getFriendList());
                    cb.notify("Gossip: online " + users.getUser(n).getUserID());
                }
            }
        }
        return retVal;
    }

    /**
     * Logs out n from the system and notifies n's friends calling them back.
     *
     * @param n
     * @return
     * @throws RemoteException
     */
    @Override
    public int logout(String n) throws RemoteException {
        int proxy = proxies.getMinProxyIndex();
        GossipRegistryProxy P = proxies.getProxy(proxy);
        InetAddress ip = P.getInetAddress();
        int port = P.getPort();
        //ProxyServerIF cbP = (ProxyServerIF) P.getCallback();
        int retVal = users.logout(n, ip, port);

        if (retVal == UsersDB.LOGGEDOUT) {
            P.incUsersNumber();
            users.getUser(n).setProxyServer(proxy);
            //cbP.registerUser(n, users.getUser(n).getPort(), users.getUser(n).getInet());
            UserAgentIF cb = (UserAgentIF) users.getUser(n).getCallback();
            cb.updateLists(users.getUser(n).getAllowedList(), users.getUser(n).getFriendList());
            for (Nickname nick : users.getUser(n).getFriendList().list) {
                if (nick.getStatus() == Nickname.ONLINE) {
                    cb = (UserAgentIF) users.getUser(nick.getUserID()).getCallback();
                    cb.updateLists(users.getUser(nick.getUserID()).getAllowedList(), users.getUser(nick.getUserID()).getFriendList());
                    cb.notify("Gossip: offline " + users.getUser(n).getUserID());
                }

            }
        }
        return retVal;
    }

    /**
     * Registers the proxy proxy into the system.
     *
     * @param proxy
     * @param a
     * @param p
     * @return
     * @throws RemoteException
     */
    @Override
    public int proxyReg(InetAddress a, int p) throws RemoteException {
        int res = proxies.addProxyServer(a, p);
        return res;
    }

    /**
     * Adds b to a's allowedList and notifies b via callback or via proxy.
     *
     * @param a
     * @param b
     * @return
     * @throws RemoteException
     */
    @Override
    public int allow(String a, String b) throws RemoteException {
        int retVal = users.allow(a, b);
        if (retVal == Nickname.OK) {
            UserAgentIF cb = (UserAgentIF) users.getUser(a).getCallback();
            cb.updateLists(users.getUser(a).getAllowedList(), users.getUser(a).getFriendList());
            if (users.getUser(b).getStatus() == Nickname.ONLINE) {
                UserAgentIF cbB = (UserAgentIF) users.getUser(b).getCallback();
                cbB.notify("Gossip: friend " + a);
            } else {
                Message m = new Message("Gossip", b, "friend " + users.getUser(a).getUserID());
                try {
                    myPool.execute(new GossipUDPSenderThread(m.toJSON(), users.getUser(b).getInet(), users.getUser(b).getPort()));
                } catch (SocketException ex) {
                    Logger.getLogger(GossipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return retVal;
    }

    /**
     * Removes b from a's allowedList and notifies b via callback or via proxy.
     *
     * @param a
     * @param b
     * @return
     * @throws RemoteException
     */
    @Override
    public int disallow(String a, String b) throws RemoteException {
        int retVal = users.disallow(a, b);
        if (retVal == Nickname.OK) {
            UserAgentIF cbA = (UserAgentIF) users.getUser(a).getCallback();
            UserAgentIF cbB = (UserAgentIF) users.getUser(b).getCallback();
            cbA.updateLists(users.getUser(a).getAllowedList(), users.getUser(a).getFriendList());
            if (users.getUser(b).getStatus() == Nickname.ONLINE) {
                cbB.updateLists(users.getUser(b).getAllowedList(), users.getUser(b).getFriendList());
                cbB.notify("Gossip: unfriend " + a);
            } else {
                Message m = new Message("Gossip", b, "unfriend " + a);
                try {
                    myPool.execute(new GossipUDPSenderThread(m.toJSON(), users.getUser(b).getInet(), users.getUser(b).getPort()));
                } catch (SocketException ex) {
                    Logger.getLogger(GossipServerRMI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
            return retVal;
        }

    /**
     * Returns the address of the proxy associated with user n.
     * @param n
     * @return
     * @throws RemoteException 
     */
        @Override
        public InetAddress getProxyAddress (String n) throws RemoteException {
            int index = users.getUser(n).getProxyServer();
            if (index == -1) {
                return null;
            } else {
                return proxies.getProxy(index).getInetAddress();
            }
        }

    

    public static void main(String[] args) throws RemoteException {
        GossipServerRMI objServer = new GossipServerRMI();
        Registry reg = LocateRegistry.createRegistry(1111);
        reg.rebind("GOSSIPReg", objServer);
        System.out.println("GOSSIPReg - ready");
    }

}
