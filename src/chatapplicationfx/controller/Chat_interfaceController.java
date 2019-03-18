/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.controller;

import chatapplicationfx.Models.Friends;
import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.client.client_remote;
import chatapplicationfx.server.Server_Interface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Moralde-Sama
 */
public class Chat_interfaceController implements Initializable {
    
    private Registry reg;
    private UserDetails user;
    private Server_Interface server;
    private boolean is_btnusersActive = false;
    private ArrayList<Friends> friends;

    @FXML
    private JFXListView<Label> lvchats;
    @FXML
    public VBox vboxmessage;
    @FXML
    private JFXTextArea txtmessage;
    @FXML
    private JFXSpinner spinnerLeft;
    @FXML
    private JFXSpinner spinnerchat;
    @FXML
    private JFXButton btnexit;
    @FXML
    private JFXButton btnusers;
    @FXML
    private JFXButton btnmessage;
    @FXML
    private JFXTextField txtsearch;

    public Chat_interfaceController(UserDetails user) throws RemoteException {
        super();
        this.user = user;
    }
    
    @FXML
    private void sendMessage(){
            Label label2 = new Label(txtmessage.getText());
            label2.setPrefWidth(559);
            label2.setWrapText(true);
            label2.setStyle("-fx-text-fill: white;");
            label2.setFont(new Font("Arial", 15));
            label2.setContentDisplay(ContentDisplay.RIGHT);
            label2.setAlignment(Pos.CENTER_RIGHT);
            try {
                URL location = new File("src/chatapplicationfx/images/messenger.png").toURI().toURL();
                Image image = new Image(location.toString(), 40, 40, false, false);
                label2.setGraphic(new ImageView(image));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            vboxmessage.getChildren().add(label2);
                
        try {
            String fullname = user.fname + " " + user.mname.substring(0, 1) + ". " + user.lname;
            server.sendMessage(txtmessage.getText(), friends.get(lvchats.getSelectionModel().getSelectedIndex()).userId , fullname);
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtmessage.setText("");
    }
    
    @FXML
    private void closeApp(){
        System.exit(0);
    }
    
    @FXML
    private void viewusers(){
        changeButton(0);
        lvchats.getItems().clear();
    }
    
    @FXML
    private void viewMessages(){
        changeButton(1);
    }
    
    @FXML
    private void clicklvchats(){
        if(is_btnusersActive){
            JFXButton button = new JFXButton("Add Friend");
            button.setStyle("-jfx-button-type: FLAT; -fx-background-color: #664797; -fx-text-fill: white;"
                    + " -fx-background-radius: 0");
            button.getStyleClass().add("popupbtn");
            button.setCursor(Cursor.HAND);
            JFXPopup popup = new JFXPopup(button);
            button.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    popup.hide();
                }
            });
            lvchats.getSelectionModel().getSelectedItem();
            popup.show(lvchats.getSelectionModel().getSelectedItem(), PopupVPosition.TOP, PopupHPosition.RIGHT);
        }
        else{
            
        }
    }
    
    private void changeButton(int type){
        if(type == 0){
            is_btnusersActive = true;
            btnusers.getStyleClass().remove("btnuser");
            btnusers.getStyleClass().add("btnuser-selected");
            btnmessage.getStyleClass().remove("btnmessage-selected");
            btnmessage.getStyleClass().add("btnmessage");
        }
        else{
            is_btnusersActive = false;
            btnusers.getStyleClass().remove("btnuser-selected");
            btnusers.getStyleClass().add("btnuser");
            btnmessage.getStyleClass().remove("btnmessage");
            btnmessage.getStyleClass().add("btnmessage-selected");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxmessage.setSpacing(20);
        lvchats.setCursor(Cursor.HAND);
        
        try {
            client_remote remote = new client_remote(vboxmessage);
            reg = LocateRegistry.getRegistry(6666);
            server = (Server_Interface) reg.lookup("Server");
            reg.rebind(user.userId+"", remote);
        

            friends = server.getFriends(user.userId);
            spinnerLeft.setVisible(false);
            spinnerchat.setVisible(false);
            for(int i = 0; i < friends.size(); i++){
                String fullname = friends.get(i).fname + " " + friends.get(i).mname.substring(0, 1) + ". " + friends.get(i).lname;
                Label label = new Label(fullname);
                label.setStyle("-fx-text-fill: white;");
                label.setFont(new Font("Arial", 15));
                label.setAlignment(Pos.CENTER_LEFT);
                try {
                    URL location = new File("src/chatapplicationfx/images/sdf.jpg").toURI().toURL();
                    Image image = new Image(location.toString(), 40, 40, false, false);
                    Circle clip = new Circle();
                    clip.setRadius(100.0);
                    ImageView img = new ImageView(image);
                    img.setClip(clip);
                    label.setGraphic(img);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
                }
                lvchats.getItems().add(label);
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        txtsearch.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try {
                    if(is_btnusersActive){
                        ArrayList<UserDetails> users = server.findUser(txtsearch.getText());
                        for(int i = 0; i < users.size(); i++) {
                            String fullname = users.get(i).fname + " " + users.get(i).mname.substring(0,1) + ". " + users.get(i).lname;
                            Label label = new Label(fullname);
                            label.setStyle("-fx-text-fill: white;");
                            label.setFont(new Font("Arial", 15));
                            label.setAlignment(Pos.CENTER_LEFT);
                            label.setCursor(Cursor.HAND);
                            try {
                                URL location = new File("src/chatapplicationfx/images/messenger.png").toURI().toURL();
                                Image image = new Image(location.toString(), 40, 40, false, false);
                                label.setGraphic(new ImageView(image));
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            lvchats.getItems().add(label);
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
