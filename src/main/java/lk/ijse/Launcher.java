package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Launcher extends Application {
    public static void main(String []args){
        launch(args);
    }

    public void start(Stage stage) throws Exception{
        // Create and configure the first stage
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Server.fxml"))));
        stage1.setTitle("Server");

        // Create and configure the second stage
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        stage2.setTitle("Client");

        // Set the positions for the stages
        stage1.setX(800);
        stage1.setY(0);

        stage2.setX(stage1.getWidth()); // Set the x-coordinate to the right of the first stage
        stage2.setY(0);

        // Show both stages
        stage1.show();
        stage2.show();

    }
}
