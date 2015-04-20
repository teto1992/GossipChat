/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Stefano Forti
 */
public class TCPClientProtocol {

    private static final int WAITINGSTART = 1;
    private static final int NAMESENT = 2;


    int state = WAITINGSTART;

    public String processInput(String input, String n) throws ParseException {
        String output = null;
        switch (state) {
            case WAITINGSTART:
                System.out.println(input);
                if (input.equalsIgnoreCase("While offline you received the following messages")) {
                    output = n;
                }
                state = NAMESENT;
                break;
            case NAMESENT:
                JSONObject o = (JSONObject) new JSONParser().parse(input);
                System.out.println(o.get("messages"));
                output = "end";
                break;
        }
        return output;
    }
}
