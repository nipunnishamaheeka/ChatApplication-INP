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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class loginFromController {

    public MFXTextField txtUsername;
    public MFXButton btnLogin;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userName = txtUsername.getText();

        // Check exactly 3 characters
        if (userName.isEmpty() || userName.length() <= 3 ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            txtUsername.setStyle("-fx-border-color: red");
//            new animatefx.animation.Shake(txtUsername).play();
            alert.setHeaderText("Warning");
            alert.setContentText("Username must have exactly 3 characters.");
            alert.showAndWait();
            return; // Exit the method if validation fails
        }

        // Check if the first letter is not capitalized
        if (!Character.isUpperCase(userName.charAt(0))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Warning");
            alert.setContentText("Username must start with a capital letter.");
            alert.showAndWait();
            return; // Exit the method if validation fails
        }

        // If validation passes, open the client stage
        openClientStage(userName);
    }


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



}
