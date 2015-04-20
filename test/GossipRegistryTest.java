

import GossipRegistry.*;
import UserAgent.GossipUserAgent;


/**
 *
 * @author Stefano
 */
public class GossipRegistryTest {
    
    
    public static void main(String[] args) {
        UsersDB users = new UsersDB();
        
        System.out.println(users.addUser("caio"));
        System.out.println(users.addUser("tizio"));
        System.out.println(users.addUser("sempronio"));
        System.out.println(users.addUser("tizio"));
        
        users.allow("tizio", "caio");
        users.friend("caio", "tizio");
        users.allow("caio", "tizio");
        users.friend("tizio", "caio");
        users.friend("caio", "sempronio");
        System.out.println(users.allow("tizio", "harry"));
        
        users.getUser("tizio").printAllowedList();
        users.getUser("caio").printAllowedList();
        
        //users.disallow("tizio", "caio");
        System.out.println(users.removeUser("caio"));
        
        users.getUser("tizio").printAllowedList();
        //users.getUser("caio").printAllowedList();
        
        users.printUsers();
        
        
    }
    
}
