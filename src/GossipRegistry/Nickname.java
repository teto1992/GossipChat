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
public class Nickname implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;

    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;
    public static final int OTHER = 2;
    public static final int OK = 11;
    public static final int ALREADYIN = 12;
    public static final int NOTINLIST = 13;

    private String userID = null;

    public String getUserID() {
        return userID;
    }

    private InetAddress InetAdd = null;

    public InetAddress getInet() {
        return InetAdd;
    }

    public void setInet(InetAddress a) {
        InetAdd = a;
    }

    int port;

    public int getPort() {
        return port;
    }

    public void setPort(int p) {
        port = p;
    }

    private Object callback = null;

    void setCallback(Object callback) {
        this.callback = callback;
    }

    public Object getCallback() {
        return callback;
    }

    private int proxy;

    public int getProxyServer() {
        return proxy;
    }

    private NicknameList allowedList = null;

    public NicknameList getAllowedList() {
        return allowedList;
    }

    private NicknameList friendList = null;

    public NicknameList getFriendList() {
        return friendList;
    }

    private int status = OFFLINE;

    public int getStatus() {
        return status;
    }

    public Nickname(String name) {
        userID = name;
        allowedList = new NicknameList();
        friendList = new NicknameList();
        status = OFFLINE;
        proxy = -1;
    }

    public void setProxyServer(int p) {
        proxy = p;
    }

    public void printAllowedList() {
        System.out.println(allowedList.list.toString());
    }

    public void printFriendList() {
        System.out.println(friendList.list.toString());
    }

    public void setStatus(int s) {
        status = s;
    }

    public boolean isFriend(Nickname n) {
        return friendList.list.contains(n);
    }

    public boolean isAllowed(Nickname n) {
        return allowedList.list.contains(n);
    }

    /**
     * Adds the Nickname associate with the userID n to the friend list.
     *
     * @param n
     * @return an exit code.
     */
    public int friend(Nickname n) {
        int retVal;
        if (friendList.list.add(n)) {
            retVal = OK;
        } else {
            retVal = ALREADYIN;
        }
        return retVal;
    }

    /**
     * Removes the Nickname associate with the userID n to the friend list.
     *
     * @param n
     * @return an exit code.
     */
    public int unfriend(Nickname n) {
        int retVal;
        if (friendList.list.remove(n)) {
            retVal = OK;
        } else {
            retVal = NOTINLIST;
        }
        return retVal;
    }

    /**
     * Adds the Nickname associate with the userID n to the allowed list.
     *
     * @param n
     * @return an exit code.
     */
    public int allow(Nickname n) {
        int retVal;
        if (allowedList.list.add(n)) {
            retVal = OK;
        } else {
            retVal = ALREADYIN;
        }
        return retVal;
    }

    /**
     * Removes the Nickname associate with the userID n to the allowed list.
     *
     * @param n
     * @return an exit code.
     */
    public int disallow(Nickname n) {
        int retVal;
        if (allowedList.list.remove(n)) {
            retVal = OK;
        } else {
            retVal = NOTINLIST;
        }
        return retVal;
    }

    @Override
    /**
     * Useful to keep the Nickname List ordered.
     */
    public int compareTo(Object o) {
        Nickname comparison = (Nickname) o;
        return this.getUserID().compareTo(comparison.getUserID());
    }

}
