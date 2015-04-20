/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Stefano Forti
 */
public class GossipTCPReceiverThread extends Thread {

    InetAddress ip;
    String name;
    TCPClientProtocol p;

    public GossipTCPReceiverThread(InetAddress add, String n) {
        ip = add;
        name = n;
        p = new TCPClientProtocol();
    }

    public void run() {
        try {
            Socket echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                echoSocket = new Socket(ip, 5730);

                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(
                        echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host.");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for "
                        + "the connection.");
                System.exit(1);
            }

            String input;
            String output;

            while ((input = in.readLine()) != null) {
                if (input.equals("end")) {
                    break;
                }
                output = p.processInput(input, name);
                out.println(output);
                if (input.equalsIgnoreCase("bye.")) {
                    break;
                }

            }

            out.close();
            in.close();
            echoSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(GossipTCPReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GossipTCPReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
