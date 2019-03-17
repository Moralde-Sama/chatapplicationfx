/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Moralde-Sama
 */
public interface Client_Interface extends Remote {
    public void receiveMessage(String messsage, String sender) throws RemoteException;
}
