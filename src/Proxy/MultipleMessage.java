/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import UserAgent.Message;
import java.io.Serializable;
import java.util.LinkedList;
import org.json.simple.JSONObject;

/**
 *
 * @author Stefano Forti
 */
public class MultipleMessage implements Serializable {

    private int n;
    LinkedList<Message> msgs;

    public MultipleMessage(LinkedList list) {
        n = list.size();
        msgs = list;
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("size", n);
        o.put("messages", msgs.toString());
        return o;
    }

    public void printMultipleMessage() {
        for (Message tmp : msgs) {
            System.out.println(tmp);
        }
    }

}
