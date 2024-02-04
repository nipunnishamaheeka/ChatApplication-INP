package lk.ijse.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class loginFromController {


    public MFXButton btnLogin;
    public TextField txtUserName;
    public Label error;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userName = txtUserName.getText();

        // Check exactly 3 characters
        if (userName.isEmpty() || userName.length() <= 3 ) {
            //Alert alert = new Alert(Alert.AlertType.WARNING);
            txtUserName.setStyle("-fx-border-color: red");
         new animatefx.animation.Shake(txtUserName).play();
//            alert.setHeaderText("Warning");
//            alert.setContentText("Username must have exactly 3 characters.");
            error.setText("Username must have exactly 3 characters.");
            //error.setStyle("-fx-border-color: red");
            error.setVisible(true);
            //alert.showAndWait();
            txtUserName.addEventFilter(KeyEvent.KEY_PRESSED, Revent -> {
                if (Revent.getCode() == KeyCode.ENTER) {
                    Revent.consume(); // Consume the event to prevent a new line in the text field
                    try {
                        btnLoginOnAction(null); // Call the send action method
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return; // Exit the method if validation fails
        }

        // Check if the first letter is not capitalized
        if (!Character.isUpperCase(userName.charAt(0))) {
            //Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setHeaderText("Warning");
//            alert.setContentText("Username must start with a capital letter.");
            new animatefx.animation.Shake(txtUserName).play();
            error.setText("Username must start with a capital letter.");
            //error.setStyle("-fx-border-color: red");
            error.setVisible(true);
            //alert.showAndWait();
            return; // Exit the method if validation fails
        }

        // If validation passes, open the client stage
       error.setVisible(false);
        openClientStage(userName);
    }
//
//
    private void openClientStage(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Client.fxml"));
        Parent root = loader.load();

        // Access the controller and call a method to set the username
        Client clientController = loader.getController();
        clientController.setUsername(username);

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root));
        stage2.setTitle("Client");
        stage2.show();
    }

//    private void openClientStage(String username) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Client.fxml"));
//        Parent root = loader.load();
//
////        // Access the controller and call a method to set the username
////        ChatFormController clientController = loader.getController();
////        clientController.setUsername(username);
//
//        Stage stage2 = new Stage();
//        stage2.setScene(new Scene(root));
//        stage2.setTitle("Client");
//        stage2.show();
//    }

}
