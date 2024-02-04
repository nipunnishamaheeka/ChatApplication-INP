package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class getStart {
    public AnchorPane navigationPane;

    public void btnstartOnAction(ActionEvent actionEvent) throws IOException {

            navigationPane.getChildren().clear();
            navigationPane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/login.fxml")));

        }
}
