package lk.ijse.controller;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class getStart {
    public AnchorPane navigationPane;

//    public void btnstartOnAction(ActionEvent actionEvent) throws IOException {
//        Button button = (Button) actionEvent.getSource();
//            navigationPane.getChildren().clear();
//            navigationPane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/login.fxml")));
//        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), button);
//        scaleT.setToX(1.2);
//        scaleT.setToY(1.2);
//        scaleT.play();
//        }
public void btnstartOnAction(ActionEvent actionEvent) {
    Button button = (Button) actionEvent.getSource();
    try {
        // Load the login view
        Pane loginView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

        // Clear the navigation pane and add the login view
        navigationPane.getChildren().clear();
        navigationPane.getChildren().add(loginView);

        // Apply scale transition to the button
        applyScaleTransition(button);
    } catch (IOException e) {
        e.printStackTrace(); // Handle the exception appropriately
    }
}

    private void applyScaleTransition(Button button) {
        // Create a scale transition for the button
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(10200), button);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.play();
    }
}
