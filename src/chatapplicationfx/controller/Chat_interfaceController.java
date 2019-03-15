/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.controller;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Moralde-Sama
 */
public class Chat_interfaceController implements Initializable {

    @FXML
    private JFXListView<Label> lvchats;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label label = new Label("Hello");
        lvchats.getItems().add(label);
    }    
    
}
