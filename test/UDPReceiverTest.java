
import UserAgent.Message;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefano
 */
public class UDPReceiverTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Message m = new Message ("renzo", "lucia", "Would u like to marry me? :P");
        m.toJSON().toJSONString();
    }
    
}
