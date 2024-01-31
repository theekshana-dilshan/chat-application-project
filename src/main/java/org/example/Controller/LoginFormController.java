package org.example.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserName;

    public static String username;

    public static ArrayList<String> clientsNames = new ArrayList<>();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        setUsername();

        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ClientForm.fxml"))));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while loading the client UI : " + e.getLocalizedMessage());
//            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        }
//        primaryStage.getIcons().add(new Image("/image/chat.png"));
        primaryStage.setTitle(username+" chat");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @FXML
    void lblSignupOnAction(MouseEvent event) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/SignUpForm.fxml")));
    }

    public void setUsername(){
        username=txtUserName.getText();
    }

}