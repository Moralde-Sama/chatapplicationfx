package chatapplicationfx.server;


import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.client.Client_Interface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Moralde-Sama
 */
public class Server extends UnicastRemoteObject implements Server_Interface{
    private static Database db = new Database();
    private static ArrayList<UserDetails> userlist = new ArrayList<>();
    private static Registry reg;

    public Server() throws RemoteException {
        super();
    }
    
    public static void main(String[] args) {
        try {
            reg = LocateRegistry.createRegistry(6666);
            reg.rebind("Server", new Server());
            
            UserDetails ud = new UserDetails();
            ud.userId = 1;
            ud.username = "admin";
            ud.password = "admin";
            userlist.add(ud);
            
            System.out.println("Server is ready");
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String findUser(String search) throws RemoteException {
        
        return "";
    }

    @Override
    public boolean signUp(UserDetails user) throws RemoteException {
        db.signUp(user);
        return true;
    }

    @Override
    public UserDetails signIn(String username, String password) throws RemoteException {
        UserDetails user = db.signIn(username, password);
        return user;
    }

    @Override
    public boolean sendMessage(String message, int userId) throws RemoteException {
        try {
            Client_Interface client = (Client_Interface) reg.lookup("" + userId);
            client.sendMessage(message);
            return true;
        } catch (NotBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        };
        return false;
    }
    
}