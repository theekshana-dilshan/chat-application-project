package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ClientFormController {

    @FXML
    private AnchorPane emojiPane;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtMessage;

    @FXML
    private Pane supRoot;

    public void initialize(){
        supRoot.setVisible(false);
        emojiPane.setVisible(!emojiPane.isVisible());
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
}