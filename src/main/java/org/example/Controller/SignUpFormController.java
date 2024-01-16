package org.example.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.example.dto.UserDTO;
import org.example.model.UserModel;

import java.io.*;
import java.sql.SQLException;

public class SignUpFormController {

    @FXML
    private ImageView imgUser;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserID;

    @FXML
    private JFXTextField txtUserName;

    private File file;

    @FXML
    void btnAddPhotoOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtUserID.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                imgUser.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSignUpOnAction(MouseEvent event) {
        /*if (!(txtUserID.getText().isEmpty() || txtUserName.getText().isEmpty())) {
            try {
                String employeeId = txtUserID.getText();
                String username = txtUserName.getText();

                boolean isExists = UserModel.existsUser(employeeId, username);
                if (!isExists) {
                    boolean isSaved;
                    if (file != null) {
                        InputStream inputStream = new FileInputStream(file);
                        isSaved = UserModel.saveUser(new UserDTO(employeeId, username, inputStream));
                    }else {
                        isSaved = UserModel.saveUser(new UserDTO(employeeId, username, null));
                    }
                    if (isSaved) {
                        *//*new SystemAlert(Alert.AlertType.CONFIRMATION,"Confirmation","Signup successfully completed!", ButtonType.OK).show();*//*
                        System.out.println("Done");
                        lblLogInOnAction(event);
                    }
                } else {
                    *//*new SystemAlert(Alert.AlertType.WARNING,"Warning","Account is already exists",ButtonType.OK).show();*//*
                    System.out.println("Error");
                }
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            *//*new SystemAlert(Alert.AlertType.WARNING,"Warning","Please fill all details",ButtonType.OK).show();*//*
            System.out.println("Error 2");
        }*/
    }

    @FXML
    void lblLogInOnAction(MouseEvent event) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }

}