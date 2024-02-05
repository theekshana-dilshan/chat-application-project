package org.example.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Controller.Client.ClientServer;
import org.example.Server.Server;
import org.example.model.UserModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserName;

    public static String username;

    public static int count = 0;

    private ServerSocket serverSocket;

    static String name;
    public void initialize(){
        count = SignUpFormController.count2;

        if(count == 0) {
            try {
                serverSocket = new ServerSocket(5000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (!serverSocket.isClosed()){

                        Socket socket = serverSocket.accept();
                        System.out.println("new user connected");

                        ClientServer client = new ClientServer(socket);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

    }

    @FXML
    void btnLoginOnAction(ActionEvent actionEventevent) throws IOException, SQLException {
        name = txtUserName.getText();

        boolean b = UserModel.searchUser(name);
        if(b){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/ClientForm.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(name);
            stage.centerOnScreen();
            stage.show();

            txtUserName.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User name not found");
            alert.showAndWait();
        }
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