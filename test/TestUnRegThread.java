

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import GossipRegistry.GossipRegistryIF;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano
 */
public class TestUnRegThread extends Thread {
int res;
    
    TestUnRegThread(String name) throws NotBoundException{
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1111);
            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
            res = stub.unregister(name);
            System.out.println(name + " "+this.getId()+" "+res);
        } catch (RemoteException ex) {
            Logger.getLogger(TestUnRegThread.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
@Override
    public void run(){
            
    }
}
