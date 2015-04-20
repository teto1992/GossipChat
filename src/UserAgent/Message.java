/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import java.io.Serializable;
import org.json.simple.JSONObject;

/**
 *
 * @author Stefano Forti 
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sender;

    public String getSender() {
        return sender;
    }

    private String receiver;

    public String getReceiver() {
        return receiver;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setSender(String s) {
        sender = s;
    }

    public void setReceiver(String r) {
        receiver = r;
    }

    public void setMessage(String m) {
        message = m;
    }

    public Message(String s, String r, String m) {
        sender = s;
        receiver = r;
        message = m;
    }

    public Message(JSONObject o) {
        sender = (String) o.get("sender");
        receiver = (String) o.get("receiver");
        message = (String) o.get("message");
    }

    /**
     * Converts the Message into a JSONObject.
     * @return the JSONObject
     */
    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("sender", sender);
        o.put("receiver", receiver);
        o.put("message", message);
        return o;
    }

    @Override
    public String toString() {
        return sender + ": " + message;
    }

}
