/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package Proxy;

import UserAgent.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Stefano Forti
 */
public class GossipTCPSenderThread extends Thread {

    ConcurrentLinkedQueue<Message> m;
    Socket socket;

    public GossipTCPSenderThread(ConcurrentLinkedQueue<Message> msgs, Socket accept) {
        socket = accept;
        m = msgs;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            TCPServerProtocol p = new TCPServerProtocol();
            String inputLine = null, outputLine = null;

            outputLine = p.processInput(inputLine, m);
            out.println(outputLine);

            //legge una riga alla volta fino al Bye.
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("end")) {
                    break;
                }
                outputLine = p.processInput(inputLine, m);
                out.println(outputLine);

            }

            out.close();
            in.close();

            socket.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
