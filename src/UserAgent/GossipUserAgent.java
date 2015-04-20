/**
 * Gossip - INSTANT_MESSAGING 
 * Laboratorio di Programmazione di Rete 
 * Laurea Triennale in Informatica (L-31)
 * Stefano Forti - 481183
 */
package UserAgent;

import GossipRegistry.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Stefano Forti
 */
public class GossipUserAgent extends UnicastRemoteObject implements UserAgentIF {

    private static final long serialVersionUID = 1L;
    private static GossipUser user;
    private static boolean loggedIn = false;
    private static int port = 4446;
    private static final String ADDRESS = "127.0.0.1";
    //private static String ADDRESS = "172.241.0.1" ;

    public GossipUserAgent(int p) throws RemoteException {
        port = p;
    }

    public GossipUserAgent() throws RemoteException {
    }

    @Override
    public void updateLists(NicknameList a, NicknameList f) throws RemoteException {
        user.updateLists(a, f);

    }

    @Override
    public void notify(String s) throws RemoteException {
        System.out.println(s);
    }

    public static void main(String[] args) throws RemoteException, IOException {
        BufferedReader cIn;
        String command;
        int res = -1;

        InetAddress ip = InetAddress.getLocalHost();

        Executor myPool = Executors.newCachedThreadPool();
        myPool.execute(new GossipUDPReceiverThread(port));

        cIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("GossipChat - Welcome");

        while (true) {
            System.out.println("Please, enter a command:");
            command = cIn.readLine();
            command = command.trim();
            command = command.replaceAll("\\s+", " ");
            String[] tokens = command.split("\\s");

            switch (tokens[0]) {
                case "send":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length < 3) {
                        System.out.println("Invalid command " + tokens[0] + ": No argument found.");
                        break;
                    }
                    if (!user.isFriend(tokens[1])) {
                        System.out.println(tokens[1] + " is not a friend!");
                        break;
                    }

                    String message = new String();
                    for (int j = 2; j < tokens.length; j++) {
                        message = message + " " + tokens[j];
                    }
                    message = message.trim();
                    Message m = new Message(user.getUserID(), tokens[1], message);
                    JSONObject msg = m.toJSON();
                    myPool.execute(new GossipUDPSenderThread(msg, user.getFriendIP(tokens[1]), user.getFriendPort(tokens[1])));
                    System.out.println(msg.toJSONString());
                    break;
                case "register":
                    if (loggedIn) {
                        System.out.println("Logout before registering another nickname...");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command " + tokens[0] + ": No argument found.");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            res = stub.register(tokens[1]);
                            System.out.println(printError(res));
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "login":
                    if (loggedIn) {
                        System.out.println("Logout before logging in again...");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command");
                    } else {
                        try {

                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Logging in ");
                            user = new GossipUser(tokens[1]);
                            res = stub.login(user.getUserID(), ip, port, new GossipUserAgent());
                            if (res == GossipRegistry.UsersDB.LOGGEDIN && (stub.getProxyAddress(user.getUserID())) != null) {
                                System.out.println(stub.getProxyAddress(user.getUserID()).toString() + user.getUserID());
                                myPool.execute(new GossipTCPReceiverThread(stub.getProxyAddress(user.getUserID()), user.getUserID()));
                            }
                            System.out.println(printError(res));
                            if (res == GossipRegistry.UsersDB.LOGGEDIN) {
                                user.printOnline();
                                loggedIn = true;
                            }
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "logout":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length != 1) {
                        System.out.println("Invalid command");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Logging out ");
                            res = stub.logout(user.getUserID());
                            System.out.println(printError(res));
                            if (res == GossipRegistry.UsersDB.LOGGEDOUT) {
                                loggedIn = false;
                            }
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case "allow":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Allowing " + tokens[1]);
                            res = stub.allow(user.getUserID(), tokens[1]);
                            System.out.println(printError(res));
                            user.printAllowedList();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case "disallow":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Disallowing " + tokens[1]);
                            res = stub.disallow(user.getUserID(), tokens[1]);
                            System.out.println(printError(res));
                            user.printAllowedList();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "friend":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Friending " + tokens[1]);
                            res = stub.friend(user.getUserID(), tokens[1]);
                            System.out.println(printError(res));
                            user.printFriendList();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "unfriend":
                    if (!loggedIn) {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    if (tokens.length != 2) {
                        System.out.println("Invalid command");
                    } else {
                        try {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Unfriending " + tokens[1]);
                            res = stub.unfriend(user.getUserID(), tokens[1]);
                            System.out.println(printError(res));
                            user.printFriendList();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "exit":
                    try {
                        if (tokens.length != 1) {
                            System.out.println("Invalid command");
                            break;
                        }
                        if (loggedIn) {
                            Registry registry = LocateRegistry.getRegistry(ADDRESS, 1111);
                            GossipRegistryIF stub = (GossipRegistryIF) registry.lookup("GOSSIPReg");
                            System.out.println("Logging out before exiting ");
                            res = stub.logout(user.getUserID());
                            System.out.println(printError(res));
                        }
                        if (res == GossipRegistry.UsersDB.LOGGEDOUT) {
                            loggedIn = false;
                        }
                    } catch (RemoteException | NotBoundException ex) {
                        Logger.getLogger(GossipUserAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.exit(0);
                case "online":
                    if (loggedIn) {
                        user.printOnline();
                    }
                    break;
                default:
                    System.out.println(printError(100));
                    break;
            }
        }
    }

    public static String printError(int r) {
        switch (r) {
            case 0:
                return "User registered! :D";
            case 1:
                return "Nickname already in use :(";
            case 2:
                return "User successfully deleted";
            case 3:
                return "User not registered, retype nickname";
            case 4:
                return "You haven't been allowed...";
            case 5:
                return "You are logged in :D";
            case 7:
                return "Logged out. Bye Bye! :D";
            default:
                return "Type the command again...";

        }

    }

}
