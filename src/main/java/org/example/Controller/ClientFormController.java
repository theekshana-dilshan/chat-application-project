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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {

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

    private Client client;

    private static String name;

    private File file;

    public String msg;

    public Socket socket;

    public DataInputStream dis;

    public DataOutputStream dos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //emojiPane hide
        emojiPane.setVisible(false);


        //Detecting the height changes in the Vbox
        ChangeListener<Number> heightListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //Check if the height has changed
                if (newValue.doubleValue() != oldValue.doubleValue()) {
                    //Scroll to the last point
                    scrollPane.setVvalue(1.0);
                }
            }
        };

        // Bind the height of the VBox to the height of the ScrollPane's viewport
        vBox.heightProperty().addListener(heightListener);
        lblUserName.setText(LoginFormController.username);
        setLabelWidth(lblUserName, lblUserName.getText());

        new Thread(() -> {

            try {
                socket = new Socket("localhost", 3000);
                System.out.println("Client started");

                //adding name of client which join the chat
                String joinMessage = "You have joined the chat";
                Label textjoin = new Label(joinMessage);
                textjoin.getStyleClass().add("join-text");
                HBox hBoxJoin = new HBox();
                hBoxJoin.getChildren().add(textjoin);
                hBoxJoin.setAlignment(Pos.CENTER);

                Platform.runLater(() -> {
                    vBox.getChildren().add(hBoxJoin);

                    HBox hBox1 = new HBox();
                    hBox1.setPadding(new Insets(5, 5, 5, 10));
                    vBox.getChildren().add(hBox1);

                    // Schedule a task to hide the HBox after 5 seconds
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                        vBox.getChildren().remove(hBoxJoin);
                    }));
                    timeline.play();
                });


                while (true) {
                    dis = new DataInputStream(socket.getInputStream());
                    msg = dis.readUTF();

                    //If the message was an image
                    if (msg.equals("img")) {
                        handleReceivedImage(dis);
                    } else {
                        //If it was a normal msg

                        String serverMessage=msg;
                        String[] parts = serverMessage.split(": ", 2);

                        String senderName = null;
                        String messageContent=null;

                        if (parts.length == 2) {
                            senderName = parts[0];
                            messageContent = parts[1];
                        }

                        Text senderText = new Text(senderName+": ");
                        senderText.setFill(Color.BLACK);

                        HBox hBox = new HBox();
                        Text text = new Text(messageContent);
                        text.setFill(Color.color(0.934, 0.945, 0.996));
                        text.setStyle("-fx-font-size: 14px;");
                        text.setText(messageContent);

                        TextFlow textFlow = new TextFlow(senderText,text);
                        textFlow.setStyle("-fx-background-color: linear-gradient(to right, #a8adad, #858c8c);" +
                                "-fx-font-size: 14px;"+
                                "-fx-background-radius: 8px;");
                        textFlow.setPadding(new Insets(5, 10, 8, 10));

                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.setPadding(new Insets(5, 5, 5, 10));
                        hBox.getChildren().add(textFlow);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                vBox.getChildren().add(hBox);
                            }
                        });


                    }
                }

            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while connecting to the server ! : " + e.getLocalizedMessage());
                    /*Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();*/
                });
            }

        }).start();
    }

    public void setLabelWidth(Label label, String text) {
        Text textNode = new Text("Welcome : " + LoginFormController.username + " !");
        textNode.setFont(label.getFont());
        double textWidth = textNode.getLayoutBounds().getWidth();
        label.setPrefWidth(textWidth);
    }

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
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

    public void btnFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtMessage.getScene().getWindow());
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        if (txtMessage.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot send empty messages !  ");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
            alert.showAndWait();

        } else {
            new Thread(() -> {

                HBox hBox = new HBox();

                Text text = new Text(txtMessage.getText());
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font-size: 14px;");
                text.setText(txtMessage.getText());

                Text senderText = new Text("me :  ");
                senderText.setFill(Color.BLACK);

                TextFlow textFlow = new TextFlow(senderText,text);
                textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
                        "-fx-background-color: linear-gradient(to right, #83e7eb, #3948ed);" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 6px;");
                textFlow.setPadding(new Insets(5, 10, 8, 10));

                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                hBox.getChildren().add(textFlow);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vBox.getChildren().add(hBox);
                    }
                });
                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(txtMessage.getText());
                    dos.flush();
                    txtMessage.clear();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while sending the message ! : " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                }
            }).start();
        }
    }

    private void handleReceivedImage(DataInputStream dis) {
        try {
            //The dis.read() method reads the length of the image data
            int imageDataLength = dis.readInt();
            //Creating a byte array using the length of the image data
            byte[] imageData = new byte[imageDataLength];
            dis.readFully(imageData);

            //Converting the byte array to a buffered image object
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(bais);

            //Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            // Create an ImageView with the Image
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);

            //ADD A scroll pane to the image container
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(imageView);

            //Append the ImageView to the imageContainer
            Platform.runLater(() -> vBox.getChildren().add(imageView));
        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error while handling the received image: " + e.getLocalizedMessage());

                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
                alert.showAndWait();
            });
        }
    }

    public static void addLabel(String sender,String messageFromServer, VBox vBox) {

    }
}