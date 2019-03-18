/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx;

import chatapplicationfx.Models.UserDetails;
import chatapplicationfx.controller.Chat_interfaceController;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Moralde-Sama
 */
public class ChatApplicationFX extends Application {
    
    public static Stage getStage;
    
    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        getStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.resizableProperty().setValue(Boolean.TRUE);
        URL loc = new File("src/chatapplicationfx/images/MESSAGINGLOGO.png").toURI().toURL();
        primaryStage.getIcons().add(new Image(loc.toString()));
        
        BorderPane root = new BorderPane();
        try {
            
            URL location = new File("src/chatapplicationfx/client/signin.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(location);
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ChatApplicationFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene scene = new Scene(root, 451, 532);
//        Scene scene = new Scene(root, 852, 575);
        scene.setFill(Color.TRANSPARENT);
        
        primaryStage.setTitle("Messaging");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
