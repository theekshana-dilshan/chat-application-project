package org.example.Controller;

import com.mysql.cj.xdevapi.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.dto.UserDTO;
import org.example.model.UserModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientFormController  {

    @FXML
    private AnchorPane emojiPane;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtMessage;

    @FXML
    private Pane supRoot;

    @FXML
    private ImageView imgWallpaper;

    @FXML
    private VBox vBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label lblUserName;

    @FXML
    private ImageView imageButton;

    @FXML
    private ImageView imgUserImage;



    public ImageView userImage;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    Socket socket;


    String name;
    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        emojiPane.setVisible(false);
        supRoot.setVisible(false);

        name = LoginFormController.name;
        lblUserName.setText(name);

        UserDTO userDTO = UserModel.getImage(name);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userDTO.getImage());

        // Use the input stream to create an Image
        Image image = new Image(byteArrayInputStream);
        imgUserImage.setImage(image);

        new Thread(() -> {
            try {
                socket = new Socket("localhost",5000);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(name);
                dataOutputStream.flush();

                while (socket.isConnected()) {
                    String type = dataInputStream.readUTF();
                    if (type.equals("Message")) {
                        //  System.out.println("sms");
                        String sms = dataInputStream.readUTF();
                        setReciveMessage(sms);
                    }
                    if (type.equals("image")) {
                        //imge ek ganna
                        String size = dataInputStream.readUTF();
                        String user_name = dataInputStream.readUTF();
                        System.out.println(user_name+":img send awa");
                        byte[] blob = new byte[Integer.parseInt(size)];
                        dataInputStream.readFully(blob);

                        setImg(blob,user_name);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    private void setImg(byte[] blob, String user_name) {
        Platform.runLater(() -> {
            Label newLabel = new Label("  "+user_name+"  ");
            newLabel.setStyle(
                    "-fx-background-color: #da7a7a;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-font-size: 15;" +
                            "-fx-font: bold;" +
                            "  font-weight: 900;"
            );


            Image image = new Image(new ByteArrayInputStream(blob));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setLayoutX(5);
            // Create a StackPane to hold the image and label
            StackPane stackPane = new StackPane(imageView, newLabel);
            stackPane.setAlignment(Pos.TOP_CENTER);

            // Create a new HBox to hold the StackPane
            HBox hBox = new HBox(stackPane);
            hBox.setStyle("-fx-padding:20;");
            hBox.setAlignment(Pos.CENTER_LEFT);  // Align to the right

            // Assuming VBox is your target VBox and is accessible within the scope
            vBox.getChildren().add(hBox);
            scrollPane.setVvalue(1.0);
        });
    }

    private void setReciveMessage(String message) {
        Platform.runLater(() -> {
            Label newLabel = new Label("  " + message + "     ");
            newLabel.setStyle(
                    "-fx-background-color: #da7a7a;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-font-size: 15;" +
                            "-fx-font: bold;" +
                            "  font-weight: 900;"
            );

            HBox hBox = new HBox(newLabel);
            hBox.setStyle("-fx-padding:20;");
            hBox.setAlignment(Pos.CENTER_LEFT);  // Align to the right for the user's messages
            vBox.getChildren().add(hBox);
        });
    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        String message = txtMessage.getText();
        try {
            dataOutputStream.writeUTF("Message");
            dataOutputStream.writeUTF(name+" : "+message); // x:hi
            dataOutputStream.flush();

            Label label = new Label("  "+message+"  ");
            label.setStyle(
                    "-fx-background-color:  #fa5252;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-font-size: 15;" +
                            "-fx-font: bold;" +
                            "  font-weight: 900;"
            );
            HBox hBox = new HBox(label);
            hBox.setStyle("-fx-padding:20;");
            hBox.setAlignment(Pos.CENTER_RIGHT);  // Align to the right for the user's messages
            vBox.getChildren().add(hBox);
            txtMessage.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void imageButton(ActionEvent actionEvent) {

        FileChooser chooser = new FileChooser();
        File file =chooser.showOpenDialog(root.getScene().getWindow());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            if (fileInputStream!=null) {
                Image image = new Image(fileInputStream);

                byte[] blob = imagenToByte(image);
                String path = file.getPath();
                sendImg(blob);
                System.out.println(path);
                setMyImg(image);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMyImg(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Create a new HBox to hold the image
        HBox hBox = new HBox(imageView);
        hBox.setStyle("-fx-padding:20;");
        hBox.setAlignment(Pos.CENTER_RIGHT);  // Align to the right

        // Assuming VBox is your target VBox and is accessible within the scope
        vBox.getChildren().add(hBox);
        scrollPane.setVvalue(1.0);
    }

    private void sendImg(byte[] blob) {
        try {
            dataOutputStream.writeUTF("image");

            dataOutputStream.writeUTF(blob.length+"");
            dataOutputStream.writeUTF(name);
            dataOutputStream.write(blob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] imagenToByte(Image image) {
        BufferedImage bufferimage = SwingFXUtils.fromFXImage(image, null);
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

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    void btnOptionOnAction(javafx.event.ActionEvent actionEvent) {
        supRoot.setVisible(!supRoot.isVisible());
    }

    @FXML
    void btnImojiOnAction(ActionEvent actionEvent) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    public void thumbsUpOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    public void hartOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\u2600\u26FF");
        emojiPane.setVisible(false);
    }

    public void sunglassOnFaceOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    public void angryMoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    public void hartFaceOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    public void kissOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE1A");
        emojiPane.setVisible(false);
    }

    public void smileMoodOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }


    public void laughFaceOnAction(MouseEvent mouseEvent) {
        txtMessage.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    public void setWallpaperOnAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(imgWallpaper.getScene().getWindow());

        if (file != null) {
            imgWallpaper.setImage(new Image(file.toURI().toString()));
            supRoot.setVisible(!supRoot.isVisible());
        }
    }

    public void setUserImage(){
        String username = lblUserName.getText();

        try {
            UserDTO userDTO = UserModel.getImage(username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}