/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import UserAgent.Message;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Stefano Forti
 */
public class TCPServerProtocol {

    private static final int WAITING = 0;
    private static final int NAMERECEIVED = 1;
    private static final int SENTMESSAGES = 2;

    private int state = WAITING;

    public String processInput(String theInput, ConcurrentLinkedQueue<Message> m) {
        String theOutput = null;
        if (state == WAITING) {
            theOutput = "While offline you received the following messages";
            state = NAMERECEIVED;
        } else if (state == NAMERECEIVED) {
            LinkedList<Message> l = new LinkedList<>();
            for (Message tmp : m) {
                if (tmp.getReceiver().equals(theInput)) {
                    System.out.println(tmp.toJSON().toJSONString());
                    l.add(tmp);
                    m.remove(tmp);
                }
            }
            MultipleMessage mm = new MultipleMessage(l);
            theOutput = mm.toJSON().toJSONString();
            state = SENTMESSAGES;
        } else if (state == SENTMESSAGES) {
            if (theInput.equals("end")) {
                theOutput = "end";
            }
        }

        return theOutput;
    }
}
