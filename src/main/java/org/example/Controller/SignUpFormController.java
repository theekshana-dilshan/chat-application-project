package org.example.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.example.dto.UserDTO;
import org.example.model.UserModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
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
        String userID = txtUserID.getText();
        String userName = txtUserName.getText();
        Image imgId = imgUser.getImage();
        byte[] blob = imagenToByte(imgId);


        try {
            UserDTO userDTO = new UserDTO(userID, userName, blob);

            UserModel.saveUser(userDTO);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void lblLogInOnAction(MouseEvent event) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
    }

    public byte[] imagenToByte(Image imgId) {
        BufferedImage bufferimage = SwingFXUtils.fromFXImage(imgId, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferimage, "jpg", output );
            ImageIO.write(bufferimage, "png", output );
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte [] data = output.toByteArray();
        return data;
    }
}