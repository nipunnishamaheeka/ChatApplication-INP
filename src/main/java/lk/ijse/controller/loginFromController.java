package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class loginFromController {

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        stage2.setTitle("Client");
        stage2.show();
    }
}
