/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import GossipRegistry.Nickname;
import GossipRegistry.NicknameList;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Stefano Forti
 */
public class GossipUser implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;
    public static final int OTHER = 2;
    public static final int OK = 11;
    public static final int ALREADYIN = 12;
    public static final int NOTINLIST = 13;

    private static int port;

    public void setPort(int p) {
        port = p;
    }

    public int getPort() {
        return port;
    }

    private String userID = null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String n) {
        userID = n;
    }

    private NicknameList allowedList = null;
    private NicknameList friendList = null;

    public synchronized void updateLists(NicknameList a, NicknameList f) {
        allowedList.list.clear();
        friendList.list.clear();
        allowedList = a;
        friendList = f;
    }

    private int status = OFFLINE;

    public int getStatus() {
        return status;
    }

    public GossipUser(String name) {
        userID = name;
        allowedList = new NicknameList();
        friendList = new NicknameList();
        status = OFFLINE;
    }

    public void printOnline() {
        System.out.println(this.getUserID() + "'s online friends: ");
        System.out.print("[ ");

        for (Nickname tmp : friendList.list) {
            if (tmp.getStatus() == Nickname.ONLINE) {
                System.out.print(tmp.getUserID() + " ");
            }
        }

        System.out.print("]");
        System.out.println();
    }

    public void printAllowedList() {
        System.out.println(this.getUserID() + "'s allowed: ");
        System.out.print("[ ");

        for (Nickname tmp : allowedList.list) {
            System.out.print(tmp.getUserID() + " ");
        }

        System.out.print("]");
        System.out.println();
    }

    public void printFriendList() {
        System.out.println(this.getUserID() + "'s friends: ");
        System.out.print("[ ");

        for (Nickname tmp : friendList.list) {
            System.out.print(tmp.getUserID() + " ");
        }

        System.out.print("]");
        System.out.println();
    }

    public void setStatus(int s) {
        status = s;
    }

    public boolean isFriend(Nickname n) {
        return friendList.list.contains(n);
    }

    public boolean isFriend(String n) {
        boolean result = false;

        for (Nickname tmp : friendList.list) {
            if (n.equals(tmp.getUserID())) {
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean isAllowed(Nickname n) {
        return allowedList.list.contains(n);
    }

    @Override
    public int compareTo(Object o) {
        Nickname comparison = (Nickname) o;
        return this.getUserID().compareTo(comparison.getUserID());
    }

    public InetAddress getFriendIP(String n) {
        return friendList.get(n).getInet();
    }

    public int getFriendPort(String n) {
        return friendList.get(n).getPort();
    }

}
