package chatapplicationfx.controller;

import static chatapplicationfx.ChatApplicationFX.getStage;
import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.server.Server_Interface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

public class SigninController implements Initializable  {

    private Registry reg;
    public static BorderPane getrootpane;
    
    @FXML
    private BorderPane rootpane;
    @FXML
    private JFXPasswordField txtpassword;
    @FXML
    private JFXTextField txtusername;
    @FXML
    private JFXButton btnsignin;
    @FXML
    private JFXButton btnsignup;
    @FXML
    private AnchorPane anchorsignin;
     @FXML
    private void signIn() {
        try {
            Server_Interface srver = (Server_Interface) reg.lookup("Server");
            UserDetails user = srver.signIn(txtusername.getText(), txtpassword.getText());
            if(user == null){
                System.out.println("Null");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void signUp(){
            anchorsignin.setVisible(false);
            getrootpane = rootpane;
            Timer animTimer = new Timer();
            animTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (getStage.getHeight() <= 640) {
                            getStage.setWidth(getStage.getWidth()+4);
                            getStage.setHeight(getStage.getHeight()+6);
                        } else {
                            this.cancel();
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    try {
                                        URL location = new File("src/chatapplicationfx/client/signup.fxml").toURI().toURL();
                                        BorderPane pane = FXMLLoader.load(location);
                            //            getStage.setWidth(525);
                            //            getStage.setHeight(640);
                                        rootpane.getChildren().setAll(pane);
                                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                        getStage.setX((primScreenBounds.getWidth() - getStage.getWidth()) / 2);
                                        getStage.setY((primScreenBounds.getHeight() - getStage.getHeight()) / 2);
                                    } catch (IOException ex) {
                                        Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                        }
                    }

            }, 0, 3);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize");
        try {
            reg = LocateRegistry.getRegistry(6666);
        } catch (RemoteException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
