package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ClientFormController {

    @FXML
    private AnchorPane root;


    @FXML
    private Pane supRoot;

    public void initialize(){
        supRoot.setVisible(false);
    }

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }

    @FXML
    void lblCloseOnAction(MouseEvent event) {
        supRoot.setVisible(false);
    }

    @FXML
    void btnOptionOnAction(javafx.event.ActionEvent actionEvent) {
        supRoot.setVisible(true);
    }
}