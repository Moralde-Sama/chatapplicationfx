/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.client;

import chatapplicationfx.Models.UsersMessages;
import chatapplicationfx.client.Client_Interface;
import chatapplicationfx.controller.Chat_interfaceController;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
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
    private JFXListView<Label> lvchats;
    
    public client_remote(VBox vboxmessage, JFXListView<Label> lvchats) throws RemoteException {
        super();
        this.vboxmessage = vboxmessage;
        this.lvchats = lvchats;
    }

    @Override
    public void receiveMessage(String message, String sender, int senderId) throws RemoteException {
        
        Platform.runLater(() -> {
            if(lvchats.getItems().size() > 0){
                for(int i = 0; i < lvchats.getItems().size(); i++){
                    if(lvchats.getItems().get(i).getText().equals(sender)){
                        break;
                    }
                    if(i == (lvchats.getItems().size() - 1)){
                        lvchats.getItems().add(getLabel(sender, ContentDisplay.LEFT, Pos.CENTER_LEFT));
                    }
                }
            }
            else{
                lvchats.getItems().add(getLabel(sender, ContentDisplay.LEFT, Pos.CENTER_LEFT));
            }
            
            try{
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
            }catch(Exception e){
                System.out.println("Invoked error");
            }
        });
    }
    
    
    private Label getLabel(String labelmess, ContentDisplay cdisplay, Pos pos){
        Label label = new Label(labelmess);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: white;");
        label.setFont(new Font("Arial", 15));
        label.setContentDisplay(cdisplay);
        label.setAlignment(pos);
        try {
                URL location = new File("src/chatapplicationfx/images/user.png").toURI().toURL();
                Image image = new Image(location.toString(), 40, 40, false, false);
                label.setGraphic(new ImageView(image));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        return label;
    }
}