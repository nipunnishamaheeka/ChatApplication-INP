package lk.ijse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {

    public TextField textFld;
    public Button sendButton;
    public ScrollPane scrollPane;
    public VBox vBoxMessages;
    private String message = "";
    private DataInputStream inputStream;
    private Socket remoteSocket;
    private DataOutputStream dataOutputStream;

    public void initialize() throws IOException {
        new Thread(() -> {
            try {
                remoteSocket = new Socket("localhost", 3100);
                dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
                System.out.println("Connected to server");
                dataOutputStream.writeUTF("Hello!");

                DataInputStream dataInputStream = new DataInputStream(remoteSocket.getInputStream());
                String message;

                while (!(message = dataInputStream.readUTF()).equals("end")) {
                    // Load messages into the vBoxMessages with styling
                    loadMessage("Client", message);
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void btnSendOnAction(MouseEvent event) {

        try {
            String clientMessage = textFld.getText();
            dataOutputStream.writeUTF(clientMessage);
            dataOutputStream.flush();
            // Load client's message into the vBoxMessages with styling
            loadMessage("Client", clientMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load messages into the vBoxMessages with styling

//    private void loadMessage(String message) {
//        Platform.runLater(() -> {
//            HBox messageBox = new HBox();
//            messageBox.getStyleClass().add("message-box");
//
//            TextArea messageTextArea = new TextArea(message);
//            messageTextArea.setEditable(false);
//            messageTextArea.getStyleClass().add("message-text");
//
//            messageBox.setAlignment(Pos.CENTER_LEFT);
//            messageBox.setPadding(new Insets(5, 10, 5, 5));
//            Text text = new Text(message);
//            text.setStyle("-fx-font-size: 15px");
//            TextFlow textFlow = new TextFlow(text);
//            textFlow.setStyle("-fx-color:rgb(239,242,255);"
//                    + "-fx-background-color: rgb(182,182,182);" +
//                    "-fx-background-radius: 10px");
//            textFlow.setPadding(new Insets(5, 0, 5, 5));
//            text.setFill(Color.color(0, 0, 0));
//
//            messageBox.getChildren().add(textFlow);
//            vBoxMessages.getChildren().add(messageBox);
//        });
//    }

    private void loadMessage(String sender, String message) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox();
            messageBox.getStyleClass().add("message-box");

            TextArea messageTextArea = new TextArea(message);
            messageTextArea.setEditable(false);
            messageTextArea.getStyleClass().add("message-text");

            TextFlow textFlow;
            if (sender.equals("Client")) {
                // If the sender is the client, align to the left
                messageBox.setAlignment(Pos.CENTER_LEFT);
                textFlow = createTextFlow(message);
            } else {
                // If the sender is another client, align to the right
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                textFlow = createTextFlow(sender + ": " + message);
            }

            messageBox.setPadding(new Insets(5, 10, 5, 5));
            messageBox.getChildren().add(textFlow);
            vBoxMessages.getChildren().add(messageBox);
        });
    }

    private TextFlow createTextFlow(String message) {
        Text text = new Text(message);
        text.setStyle("-fx-font-size: 15px");

        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);"
                + "-fx-background-color: rgb(182,182,182);" +
                "-fx-background-radius: 10px");
        textFlow.setPadding(new Insets(5, 0, 5, 5));
        text.setFill(Color.color(0, 0, 0));

        return textFlow;
    }

    public void btnAddClientOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        stage2.setTitle("Client");
        stage2.show();
    }

    public void btnEmojiOnAction(MouseEvent event) {
        String[] emojiSet = {"😊", "❤️", "😂", "👍", "🎉", "🌟", "🔥", "🙌", "🌈", "🚀"};

        // Display emojis in the TextField
        for (String emoji : emojiSet) {
            textFld.appendText(emoji + " ");
        }
    }
}
