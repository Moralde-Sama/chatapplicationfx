/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.controller;

import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.client.client_remote;
import chatapplicationfx.server.Server_Interface;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Moralde-Sama
 */
public class Chat_interfaceController implements Initializable {
    
    private Registry reg;
    private UserDetails user;

    @FXML
    private JFXListView<Label> lvchats;
    @FXML
    public VBox vboxmessage;
    @FXML
    private JFXTextArea txtmessage;

    public Chat_interfaceController(UserDetails user) throws RemoteException {
        super();
        this.user = user;
    }
    
    @FXML
    private void sendMessage(){
        Server_Interface srver;
        try {
            String fullname = user.fname + " " + user.mname.substring(0, 1) + ". " + user.lname;
            srver = (Server_Interface) reg.lookup("Server");
            srver.sendMessage(txtmessage.getText(), user.userId, fullname);
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtmessage.setText("");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxmessage.setSpacing(20);
        
        try {
            client_remote remote = new client_remote(vboxmessage);
            reg = LocateRegistry.getRegistry(6666);
            reg.rebind(user.userId+"", remote);
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        for(int i = 0; i <= 2; i++){
//            Label label = new Label("Emmanuel Paul G. Moralde\n ksjldkfjsdfls");
//            label.setStyle("-fx-text-fill: white;");
//            label.setFont(new Font("Arial", 15));
//            label.setAlignment(Pos.CENTER_LEFT);
//            try {
//                URL location = new File("src/chatapplicationfx/images/messenger.png").toURI().toURL();
//                Image image = new Image(location.toString(), 40, 40, false, false);
//                label.setGraphic(new ImageView(image));
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            lvchats.getItems().add(label);
//        }
    }
    
}
