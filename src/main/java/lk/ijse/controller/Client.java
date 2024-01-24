package lk.ijse.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    public ImageView btnSend;
    private String message = "";
    private Socket remoteSocket;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        try {
            remoteSocket = new Socket("localhost", 3100);
            dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
            System.out.println("Connected to server");
            dataOutputStream.writeUTF("Hello!");

            DataInputStream inputStream = new DataInputStream(remoteSocket.getInputStream());

            // Start a new thread to handle incoming messages from the server
            new Thread(() -> {
                try {
                    while (true) {
                        String message = inputStream.readUTF();
                        Platform.runLater(() -> loadMessage("Server", message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Add event filter for Enter key press
            textFld.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    event.consume(); // Consume the event to prevent a new line in the text field
                    btnSendOnAction(null); // Call the send action method
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSendOnAction(MouseEvent event) {
        try {
            String clientMessage = textFld.getText();
            dataOutputStream.writeUTF(clientMessage);
            dataOutputStream.flush();
            // Load client's message into the vBoxMessages with styling
            loadMessage("Me", clientMessage);
            textFld.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMessage(String sender, String message) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox();
            messageBox.getStyleClass().add("message-box");

            TextFlow textFlow;
            if (sender.equals("Me")) {
                // If the sender is the client, align to the right
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                messageBox.setPadding(new Insets(5, 5, 5, 10));

                textFlow = createTextFlow("Me: " + message);
                textFlow.setStyle("-fx-font-size: 12px");
                textFlow.setStyle("-fx-color:rgb(239,242,255);"
                        + "-fx-background-color: rgb(30,129,246);"
                        + "-fx-background-radius: 10px");
            } else {
                // If the sender is another client, align to the left
                messageBox.setAlignment(Pos.CENTER_LEFT);
                messageBox.setPadding(new Insets(5, 5, 5, 10));

                textFlow = createTextFlow(sender + ": " + message);
                textFlow.setStyle("-fx-font-size: 12px");
                textFlow.setStyle("-fx-color:rgb(239,242,255);"
                        + "-fx-background-color: rgb(189,193,199);"
                        + "-fx-background-radius: 10px");
            }

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

    // Handle emoji clicks
    @FXML
    void handleEmojiClick(MouseEvent mouseEvent) {
        String emoji = "\uD83D\uDE42"; // Replace with the appropriate emoji
        sendEmoji(emoji);
    }

    private void sendEmoji(String emoji) {
        String msg = "Me: " + emoji;
        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text("Me: " + emoji);
            text.setStyle("-fx-font-size: 15px");

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color:rgb(239,242,255);"
                    + "-fx-background-color: rgb(15,125,242);" +
                    "-fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);
            vBoxMessages.getChildren().add(hBox);

            textFld.clear();
        });

        if (emoji.equalsIgnoreCase("BYE") || emoji.equalsIgnoreCase("logout")) {
            System.exit(0);
        }
    }

    public void btnAddClientOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        stage2.setTitle("Client");
        stage2.show();
    }
}
