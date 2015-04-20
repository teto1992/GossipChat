




import java.rmi.NotBoundException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stefano
 */
public class TestGossipRMI {

    static int j = 0;
    static final String[] names = {"tizio", "caio", "sempronio", "paperino", "minnie", "tizio", "caio"};
   

    /**
     * @param args the command line arguments
     * @throws java.rmi.NotBoundException
     */
    public static void main(String[] args) throws NotBoundException {
        Executor myPool = Executors.newCachedThreadPool();
        System.out.println("register");
        for (int i = 0; i < names.length; i++) {
            myPool.execute(new TestRegThread(names[i]));
        }
        
      /* System.out.println("login");
        for (int i = 0; i < names.length; i++) {
            myPool.execute(new TestLoginThread(names[i]));
        }*/
        
        System.out.println("logout");
        for (int i = 0; i < names.length; i++) {
            myPool.execute(new TestLogoutThread(names[i]));
        }
        
        
        System.out.println("unregister");
        for (int i = 0; i < names.length; i++) {
            myPool.execute(new TestUnRegThread(names[i]));
        }
        
        System.exit(0);
        
        
    }

}
