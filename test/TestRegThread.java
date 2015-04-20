




import GossipRegistry.GossipRegistryIF;
import UserAgent.GossipUserAgent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefano
 */
public class TestRegThread extends Thread{
    int res;
    
    TestRegThread(String name) throws NotBoundException{
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1111);
            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
            res = stub.register(name);
            System.out.println(name + " "+this.getId()+" "+res);
        } catch (RemoteException ex) {
            Logger.getLogger(TestRegThread.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    public void run(){
            
    }
}
