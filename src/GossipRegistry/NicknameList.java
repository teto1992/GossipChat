/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31) 
 * Stefano Forti - 481183
 */
package GossipRegistry;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 *
 * @author Stefano Forti
 */
public class NicknameList implements Serializable {

    private static final long serialVersionUID = 1L;

    public LinkedHashSet<Nickname> list;

    public NicknameList() {
        list = new LinkedHashSet<>();
    }

    /**
     * Returns the Nickname associated with the given userID.
     *
     * @param n the userID to look for in the list
     * @return the desired Nickname object if it is contained in the List, null
     * otherwise.
     */
    public Nickname get(String n) {
        for (Nickname tmp : list) {
            if (tmp.getUserID().equals(n)) {
                return tmp;
            }
        }
        return null;
    }
}
