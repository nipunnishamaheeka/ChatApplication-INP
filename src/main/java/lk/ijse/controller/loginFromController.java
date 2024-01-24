package lk.ijse.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class loginFromController {

    public MFXTextField txtUsername;
    public MFXButton btnLogin;
    public MFXPasswordField textpassword;

    //    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
//        Stage stage2 = new Stage();
//        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
//        stage2.setTitle("Client");
//        stage2.show();
//    }
    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {

        String userName = txtUsername.getText();
        String password = textpassword.getText();

        System.out.println(txtUsername.getText().isEmpty());
        if (!txtUsername.getText().isEmpty()) {
            try {
                boolean isIn = loginBo.searchUser(new LoginDto(null, userName, password, null));
                if (!isIn) {
                    new Alert(Alert.AlertType.WARNING, "Invalid User Name or Password");
                    textpassword.setStyle("-fx-border-color: red");
                    new animatefx.animation.Shake(textpassword).play();
                    ;
                    return;
                } else {
                    //navigateToMainWindow();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Enter Your UserName");
        }
    }
}
