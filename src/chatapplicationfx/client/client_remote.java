/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.client;

import chatapplicationfx.controller.Chat_interfaceController;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Moralde-Sama
 */
public class client_remote extends UnicastRemoteObject implements Client_Interface {

    private VBox vboxmessage;
    
    public client_remote(VBox vboxmessage) throws RemoteException {
        super();
        this.vboxmessage = vboxmessage;
    }

    @Override
    public void receiveMessage(String message, String sender) throws RemoteException {
        System.out.println("Received");
        Platform.runLater(() -> {
            Label label2 = new Label(sender + " \n " + message);
            label2.setPrefWidth(559);
            label2.setWrapText(true);
            label2.setStyle("-fx-text-fill: white;");
            label2.setFont(new Font("Arial", 15));
            label2.setContentDisplay(ContentDisplay.LEFT);
            try {
                URL location = new File("src/chatapplicationfx/images/user.png").toURI().toURL();
                Image image = new Image(location.toString(), 40, 40, false, false);
                label2.setGraphic(new ImageView(image));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            vboxmessage.getChildren().add(label2);
        });
        
    }
    
}
