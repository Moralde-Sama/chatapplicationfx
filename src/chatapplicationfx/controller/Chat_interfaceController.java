/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.controller;

import chatapplicationfx.ChatApplicationFX;
import chatapplicationfx.Models.UsersMessages;
import chatapplicationfx.Models.Messages;
import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.client.client_remote;
import chatapplicationfx.server.Server_Interface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
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
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Moralde-Sama
 */
public class Chat_interfaceController implements Initializable {
    
    private Registry reg;
    private UserDetails user;
    private Server_Interface server;
    private Stage getChatStage;
    private int currentIndex = -1;
    private double xOffset = 0.0;
    private double yOffset = 0.0;;
    private boolean is_btnusersActive = false;
    private boolean is_chatopen = false;
    public ArrayList<UsersMessages> usersmess;
    private ArrayList<UserDetails> users;
    private ArrayList<Messages> messages;

    @FXML
    public JFXListView<Label> lvchats;
    @FXML
    public VBox vboxmessage;
    @FXML
    private AnchorPane mactionbar;
    @FXML
    private ScrollPane spchat;
    @FXML
    private StackPane stockpane;
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
    @FXML
    private Label lblreceiver;
    @FXML
    private Label lbluser;

    public Chat_interfaceController(UserDetails user, Stage stage) throws RemoteException {
        super();
        this.user = user;
        getChatStage = stage;
    }
    
    @FXML
    private void sendMessage(){
            Label label2 = getLabel(txtmessage.getText(), ContentDisplay.RIGHT, Pos.CENTER_RIGHT);
            label2.setPrefWidth(559);
            vboxmessage.getChildren().add(label2);
        try {
            String fullname = user.fname + " " + user.mname.substring(0, 1) + ". " + user.lname;
            server.sendMessage(txtmessage.getText(), user.userId, is_btnusersActive ? users.get(lvchats.getSelectionModel().getSelectedIndex()).userId : usersmess.get(lvchats.getSelectionModel().getSelectedIndex()).userId  , fullname);
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtmessage.setText("");
    }
    
    @FXML
    private void closeApp(){
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text("Logout"));
        layout.setBody(new Text("are you sure you want to logout?"));
        JFXButton no = new JFXButton("No");
        JFXButton yes = new JFXButton("Yes");
        JFXButton ok = new JFXButton("Ok");
        no.setStyle("-fx-text-fill: #24B8EF");
        yes.setStyle("-fx-text-fill: #24B8EF");
        ok.setStyle("-fx-text-fill: #24B8EF");
        List<JFXButton> actions = new ArrayList<>();
        actions.add(no);
        actions.add(yes);
        layout.setActions(actions);
        stockpane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stockpane, layout, JFXDialog.DialogTransition.CENTER);
        stockpane.setVisible(true);
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               dialog.close();
               stockpane.setVisible(false);
            }
        });
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layout.setBody(new Text("Logout Successfully!"));
                layout.setActions(ok);
            }
        });
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                BorderPane root = new BorderPane();
                Stage primaryStage = new Stage();
                try {
                    getChatStage.getScene().getWindow().hide();
                    stockpane.setVisible(false);
                    primaryStage.initStyle(StageStyle.TRANSPARENT);
                    URL loc = new File("src/chatapplicationfx/images/MESSAGINGLOGO.png").toURI().toURL();
                    primaryStage.getIcons().add(new Image(loc.toString()));
                    URL location = new File("src/chatapplicationfx/client/signin.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(location);
                    root = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(ChatApplicationFX.class.getName()).log(Level.SEVERE, null, ex);
                }

                Scene scene = new Scene(root, 451, 532);
                scene.setFill(Color.TRANSPARENT);

                primaryStage.setTitle("Messaging");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        dialog.show();
    }
    
    @FXML
    private void viewusers(){
        changeButton(0);
        lvchats.getItems().clear();
        txtsearch.setText("");
        txtsearch.requestFocus();
        stageTransition(295, 3, false);
        is_chatopen = false;
    }
    
    @FXML
    private void viewMessages(){
        changeButton(1);
        getusersmessages();
        txtsearch.setText("");
        if(is_chatopen){
            stageTransition(295, 3, false);
            is_chatopen = false;
            currentIndex = -1;
        }
    }
    
    @FXML
    private void clicklvchats(){
            lvchats.getSelectionModel().getSelectedItem().setStyle("-fx-text-fill: #f1d058;");
            Label label;
            if(currentIndex != -1){
                System.out.println(currentIndex);
                label = lvchats.getItems().get(currentIndex);
                label.setStyle("-fx-text-fill: white;");
            }
                
            currentIndex = lvchats.getSelectionModel().getSelectedIndex();
            if(!is_chatopen){
                spinnerchat.setVisible(true);
                is_chatopen = true;
                stageTransition(888, 3, true);
            }
            getMessages();
            String fullname;
            if(is_btnusersActive){
                fullname = users.get(currentIndex).fname + " " + users.get(currentIndex).mname.substring(0,1)
                        + ". " + users.get(currentIndex).lname;
            }
            else{
                fullname = usersmess.get(currentIndex).fname + " " + usersmess.get(currentIndex).mname.substring(0,1) 
                        + ". " + usersmess.get(currentIndex).lname;
            }
            lblreceiver.setText(fullname);
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
    
    private void getusersmessages(){
        try {
            usersmess = server.getUsersMessages(user.userId);
            lvchats.getItems().clear();
            spinnerLeft.setVisible(false);
            int ursid[] = new int[usersmess.size()];
            if(usersmess.size() != 0){
                for(int i = 0; i < usersmess.size(); i++){
                    int count = 0;
                    UsersMessages ur = usersmess.get(i);
                    for(int o = 0; o < usersmess.size(); o++){
                        if(usersmess.get(o).userId == ur.userId){
                            count++;
                        }
                        if(count == 2){
                            usersmess.remove(usersmess.indexOf(ur));
                        }
                    }
                }
                for(UsersMessages getuser : usersmess){
                    String fullname = getuser.fname + " " + getuser.mname.substring(0, 1) + ". " + getuser.lname;
                    Label label = new Label(fullname);
                    label.setStyle("-fx-text-fill: white;");
                    label.setFont(new Font("Arial", 15));
                    label.setAlignment(Pos.CENTER_LEFT);
                    try {
                        URL location = new File("src/chatapplicationfx/images/user.png").toURI().toURL();
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
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void stageTransition(int width, int add, boolean plus){
        Timer animTimer = new Timer();
            animTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        boolean is_plus;
                        if(plus)
                            is_plus = getChatStage.getWidth()<= width;
                        else
                            is_plus = getChatStage.getWidth()>= width;
                        if (is_plus) {
                            getChatStage.setWidth(plus ? getChatStage.getWidth()+add : getChatStage.getWidth()-add);
                            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                            getChatStage.setX((primScreenBounds.getWidth() - getChatStage.getWidth()) / 2);
                        } else {
                            this.cancel();
                        }
                    }

            }, 0, 3);
    }
    
    private void getMessages(){
        vboxmessage.getChildren().clear();
        try {
            if(is_btnusersActive){
                messages = server.getMessages(user.userId, users.get(lvchats.getSelectionModel().getSelectedIndex()).userId);
            }
            else{
                usersmess = server.getUsersMessages(user.userId);
                messages = server.getMessages(user.userId, usersmess.get(lvchats.getSelectionModel().getSelectedIndex()).userId);
            }
            
            if(messages.size() != 0){
                for(int i =0; i < messages.size(); i++){
                    Label label = null;
                    if(messages.get(i).senderId == user.userId){
                        label = getLabel(messages.get(i).message, ContentDisplay.RIGHT, Pos.CENTER_RIGHT);
                        label.setPrefWidth(559);
                        System.out.println("Right");
                    }
                    else{
                        label = getLabel(messages.get(i).fullname + "\n" + messages.get(i).message, 
                                ContentDisplay.LEFT, Pos.CENTER_LEFT);
                        System.out.println("Left");
                    }
                    vboxmessage.getChildren().add(label);
                }
                spinnerchat.setVisible(false);
            }
            else{
                spinnerchat.setVisible(false);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxmessage.setSpacing(20);
        lvchats.setCursor(Cursor.HAND);
        lbluser.setText(user.fname + " " + user.mname.substring(0, 1) + ". " + user.lname);
        
        try {
            client_remote remote = new client_remote(vboxmessage, lvchats);
            reg = LocateRegistry.getRegistry(6666);
            reg.rebind(user.userId+"", remote);
            server = (Server_Interface) reg.lookup("Server");
            
            getusersmessages();
        } catch (RemoteException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Chat_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        spchat.vvalueProperty().bind(vboxmessage.heightProperty());
        
        txtsearch.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try {
                    if(is_btnusersActive){
                        users = server.findUser(txtsearch.getText());
                        lvchats.getItems().clear();
                        for(int i = 0; i < users.size(); i++) {
                            String fullname = users.get(i).fname + " " + users.get(i).mname.substring(0,1) + ". " + users.get(i).lname;
                            Label label = new Label(fullname);
                            label.setStyle("-fx-text-fill: white;");
                            label.setFont(new Font("Arial", 15));
                            label.setAlignment(Pos.CENTER_LEFT);
                            label.setCursor(Cursor.HAND);
                            try {
                                URL location = new File("src/chatapplicationfx/images/user.png").toURI().toURL();
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
        
        mactionbar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getScreenX() - 300 ;
                yOffset = event.getScreenY() - 50;
            }
        });
        mactionbar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getChatStage.setX(event.getScreenX() - xOffset);
                getChatStage.setY(event.getScreenY() - yOffset);
            }
        });
    }
    
}
