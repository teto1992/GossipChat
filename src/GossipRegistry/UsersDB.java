/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package GossipRegistry;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Stefano Forti
 */
public class UsersDB {

    /**
     * Result constants.
     */
    public static final int REGISTERED = 0;
    public static final int NICKNAMENOTAVAILABLE = 1;
    public static final int USERDELETED = 2;
    public static final int NOTINDB = 3;
    public static final int NOTALLOWED = 4;
    public static final int LOGGEDIN = 5;
    public static final int LOGGEDOUT = 7;

    ConcurrentHashMap<String, Nickname> m = null;

    public UsersDB() {
        m = new ConcurrentHashMap<>();
    }

    /**
     * Return the Nickname associated with the userID n in the Users DB.
     *
     * @param n the userID to be looked for.
     * @return the Nickname associated with the string n, or null.
     */
    public synchronized Nickname getUser(String n) {
        return m.get(n);
    }

    /**
     * If the userID of n is available, adds the Nickname n to the DB.
     *
     * @param n The Nickname to be added to the DB.
     * @return NICKNAMENOTAVAILABLE if the userID is already in use REGISTERED
     * if the Nickname is added to the DB
     */
    public synchronized int addUser(String n) {
        if (m.containsKey(n) || n.equals("Gossip")) {
            return NICKNAMENOTAVAILABLE;
        } else {
            m.put(n, new Nickname(n));
        }
        return REGISTERED;
    }

    /**
     * If the userID is in the DB, removes it from the DB and from all the
     * lists.
     *
     * @param n The Nickname to be removed from the DB.
     * @return NOTINDB if the userID is not in use USERDELETED if the Nickname
     * is removed from the DB
     */
    public synchronized int removeUser(String n) {
        if (!m.containsKey(n)) {
            return NOTINDB;
        } else {

            for (String tmp : m.keySet()) {
                this.unfriend(tmp, n);
                this.disallow(tmp, n);
            }
            m.remove(n, m.get(n));
            return USERDELETED;
        }
    }

    /**
     * Prints all the users in the DB on the standard output.
     */
    public synchronized void printUsers() {
        for (String tmp : m.keySet()) {
            System.out.println(tmp);
        }
    }

    /**
     * The Nickname associated with b is added to the FriendList of the Nickname
     * associated with a.
     *
     * @param a
     * @param b
     * @return 0 if everything is OK, NOTALLOWED otherwise.
     */
    public synchronized int friend(String a, String b) {
        Nickname A = m.get(a);
        Nickname B = m.get(b);

        if (A == null || B == null) {
            return NOTINDB;
        }

        if (B.isAllowed(A)) {
            return A.friend(B);
        } else {
            return NOTALLOWED;
        }

    }

    /**
     * The Nickname associated with b is removed from the FriendList of the
     * Nickname associated with a.
     *
     * @param a
     * @param b
     * @return 0 if everything is OK, 1 otherwise.
     */
    public synchronized int unfriend(String a, String b) {
        Nickname A = m.get(a);
        Nickname B = m.get(b);

        if (A == null || B == null) {
            return NOTINDB;
        }

        return A.unfriend(B);
    }

    /**
     * The Nickname associated with b is added to the AllowedList of the
     * Nickname associated with a.
     *
     * @param a
     * @param b
     * @return 0 if everything is OK, NOTALLOWED otherwise.
     */
    public synchronized int allow(String a, String b) {
        int retVal;
        Nickname A = m.get(a);
        Nickname B = m.get(b);

        if (A == null || B == null) {
            return NOTINDB;
        }
        retVal = A.allow(B);

        A.printAllowedList();

        return retVal;
    }

    /**
     * The Nickname associated with b is removed from the AllowedList of the
     * Nickname associated with a.
     *
     * @param a
     * @param b
     * @return 0 if everything is OK, 1 otherwise.
     */
    public synchronized int disallow(String a, String b) {
        Nickname A = m.get(a);
        Nickname B = m.get(b);
        int retVal;

        if (A == null || B == null) {
            return NOTINDB;
        }

        if (B.isFriend(A)) {
            B.unfriend(A);
        }

        retVal = A.disallow(B);

        A.printAllowedList();
        A.printFriendList();

        return retVal;

    }
/**
 * Logs in the user and updates the associated data within the system.
 * @param n userID of the user to be logged in
 * @param ip InetAddress associated to the user
 * @param port port chosen by the user to communicate
 * @param callback object used for RMI callbacks by the Registry
 * @return NOTINDB if the user is not registered or LOGGEDIN in case of success
 */
    public synchronized int login(String n, InetAddress ip, int port, Object callback) {
        if (!m.containsKey(n)) {
            return NOTINDB;
        } else {
            Nickname user = m.get(n);
            user.setStatus(Nickname.ONLINE);
            user.setCallback(callback);
            user.setInet(ip);
            user.setPort(port);
            return LOGGEDIN;
        }
    }
/**
 * Logs out the user and updates the associated data within the system.
 * @param n userID of the user to be logged out
 * @param ip InetAddress associated to the user's proxy
 * @param port port chosen by the user's proxy to communicate
 * @return NOTINDB if the user is not registered or LOGGEDIN in case of success
 */
    public synchronized int logout(String n, InetAddress ip, int port) {
        if (!m.containsKey(n)) {
            return NOTINDB;
        } else {
            Nickname user = m.get(n);
            user.setStatus(Nickname.OFFLINE);
            user.setInet(ip);
            user.setPort(port);
            return LOGGEDOUT;
        }
    }

}
