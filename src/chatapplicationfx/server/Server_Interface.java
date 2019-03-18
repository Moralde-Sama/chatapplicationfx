package chatapplicationfx.server;


import chatapplicationfx.Models.Friends;
import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Moralde-Sama
 */
import chatapplicationfx.Models.UserDetails;
import java.util.ArrayList;
public interface Server_Interface extends Remote {
    public ArrayList<UserDetails> findUser(String search) throws RemoteException;
    public boolean signUp(UserDetails user) throws RemoteException;
    public UserDetails signIn(String username, String password) throws RemoteException;
    public boolean sendMessage(String message, int userId, String fullname) throws RemoteException;
    public ArrayList<Friends> getFriends(int userId) throws RemoteException;
    
}
