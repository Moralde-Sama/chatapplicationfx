/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.controller;

import static chatapplicationfx.ChatApplicationFX.getStage;
import chatapplicationfx.Models.UserDetails;
import static chatapplicationfx.controller.SigninController.getrootpane;
import chatapplicationfx.server.Server_Interface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author Moralde-Sama
 */
public class SignupController implements Initializable {

    private Registry reg;
    
    @FXML
    private JFXTextField txtusername;
    @FXML
    private JFXPasswordField txtpassword;
    @FXML
    private JFXTextField txtfname;
    @FXML
    private JFXTextField txtmname;
    @FXML
    private JFXTextField txtlname;
    @FXML
    private JFXComboBox<?> cbxGender;
    @FXML
    private JFXButton btnsignin;
    @FXML
    private JFXButton btnsignup;
    @FXML
    private StackPane stockpane;

    @FXML
    private void signUp(){
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text("Sign Up"));
        layout.setBody(new Text("are you sure you want to sign up?"));
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
                try {
                    Server_Interface srver = (Server_Interface) reg.lookup("Server");
                    UserDetails user = new UserDetails();
                    user.fname = txtfname.getText();
                    user.mname = txtmname.getText();
                    user.lname = txtlname.getText();
                    user.username = txtusername.getText();
                    user.password = txtpassword.getText();
                    if(srver.signUp(user)){
                        layout.setBody(new Text("Save Successfully!"));
                        layout.setActions(ok);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stockpane.setVisible(false);
                signIn();
            }
        });
        dialog.show();
    }
    
    @FXML
    private void signIn(){
        try {
            URL location = new File("src/chatapplicationfx/client/signin.fxml").toURI().toURL();
            BorderPane pane = FXMLLoader.load(location);
            getStage.setWidth(451);
            getStage.setHeight(532);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            getStage.setX((primScreenBounds.getWidth() - getStage.getWidth()) / 2);
            getStage.setY((primScreenBounds.getHeight() - getStage.getHeight()) / 2);
            getrootpane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            reg = LocateRegistry.getRegistry(6666);
        } catch (RemoteException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
