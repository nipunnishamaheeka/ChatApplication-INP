package lk.ijse.controller;

import com.gluonhq.emoji.Emoji;
import com.gluonhq.emoji.EmojiData;
import com.gluonhq.emoji.util.TextUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class Client {

    public TextField textFld;
    public Button sendButton;
    public ScrollPane scrollPane;
    public VBox vBoxMessages;
    public ImageView btnSend;
    public Label lblClientName;
    public GridPane emojiGridPane;
    public ImageView btnEmoji;
    public Pane emojiPane;
    private String message = "";
    private Socket remoteSocket;
    private DataOutputStream dataOutputStream;


    public void initialize() {
        setScrollPaneTransparent();

        setEmojis();
        vBoxMessages.heightProperty().addListener((observableValue, oldValue, newValue) -> scrollPane.setVvalue((Double) newValue));
//            // Add event filter for Enter key press
        textFld.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume(); // Consume the event to prevent a new line in the text field
                btnSendOnAction(null); // Call the send action method
            }
        });
        Runnable runnable = this::socketInitialize;

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setScrollPaneTransparent() {
        Platform.runLater(() -> {
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
            scrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar:vertical").setStyle("-fx-background-color: transparent;");

        });
    }

    private void socketInitialize() {

        try {
            remoteSocket = new Socket("localhost", 3100);
            dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
            System.out.println("Connected to server");
            //dataOutputStream.writeUTF("Hello!");
            DataInputStream inputStream = new DataInputStream(remoteSocket.getInputStream());

            do {
                message = inputStream.readUTF();
                System.out.println(message);
                messageSelector(message);

            } while (!message.equals("end"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void messageSelector(String message) {
        String[] parts = message.split("&");
        System.out.println("above try");
        try {
            String messageType = (String) parts[0];
            String sender = parts[1];
            String content = parts[2];

            switch (messageType) {
                case "msg":
                    loadMessage(sender, content);
                    break;
                case "emoji":
                    //receivedName(sender);
                    receiveEmoji(content);
                    break;
                case "img":
                    //receivedName(sender);
                    receiveImage(content);
                    break;
                default:
                    System.out.println("Unknown message type: " + messageType);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid message format: " + message);
            // Handle the exception
        }
    }


    public void setUsername(String username) {
        lblClientName.setText(username);
    }

    public void btnSendOnAction(MouseEvent event) {
        try {
            String clientMessage = textFld.getText();
            String newMessage = "msg&" + lblClientName.getText() + "&" + clientMessage;
            dataOutputStream.writeUTF(newMessage);
            loadMessage("Me", clientMessage);
            textFld.clear();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadMessage(String sender, String message) {
        Platform.runLater(() -> {
            HBox messageBox = new HBox();
            messageBox.getStyleClass().add("message-box");

            TextFlow textFlow;
            if (sender.equals("Me")) {
                // If the sender is the client, align to the right
                messageBox.setAlignment(Pos.BASELINE_RIGHT);

                messageBox.setPadding(new Insets(5, 5, 5, 10));
                textFlow = createTextFlow("Me", message);
                textFlow.setStyle("-fx-color: rgb(239, 242, 255);" + "-fx-background-color: rgb(15, 125, 242);" + "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5, 20, 5, 10));

                textFlow.setLineSpacing(5);
            } else {
                // If the sender is another client, align to the left
                messageBox.setAlignment(Pos.CENTER_LEFT);
                messageBox.setPadding(new Insets(5, 5, 5, 10));

                textFlow = createTextFlow(sender , message);
                // Add the generated nodes directly to the TextFlow
                List<Node> nodes = TextUtils.convertToTextAndImageNodes(createUnicodeText(message));
                textFlow.getChildren().addAll(nodes);

                textFlow.setStyle("-fx-color: rgb(222,222,222);" + "-fx-background-color: rgb(213,210,210);" + "-fx-background-radius: 20px;");
            }
            messageBox.getChildren().add(textFlow);
            vBoxMessages.getChildren().add(messageBox);
        });
    }

    private TextFlow createTextFlow(String name,String message) {
        Text text=new Text();
        TextFlow textFlow=null;
        if (name.equals("Me")){
            text = new Text("Me : "+message);
            text.setStyle("-fx-font-size: 14px");
            text.setFill(Color.color(1, 1, 1));
            textFlow= new TextFlow(text);
        }else {
            Text nametext=new Text(name);
            nametext.setFill(Color.color(1,0.5019,0));
            text = new Text(" : "+message);
            text.setStyle("-fx-font-size: 14px");
            text.setFill(Color.color(0, 0, 0));
            textFlow= new TextFlow(nametext,text);
        }

        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(182,182,182);" + "-fx-background-radius: 10px");
        textFlow.setPadding(new Insets(5, 0, 5, 5));


        return textFlow;
    }
    // Handle emoji clicks
    private void setEmojis() {

        //Clear the grid
        emojiGridPane.getChildren().clear();

        String text = "grinning grin joy smile smiling_face_with_tear sunglasses middle_finger pinched_fingers wave";
        String[] words = text.split(" ");
        List<Node> nodes = TextUtils.convertToTextAndImageNodes(createUnicodeText(text));

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            MFXButton btn = new MFXButton(words[i], node);
            btn.setPrefHeight(45);
            btn.setPrefWidth(45);
            btn.setOnMouseClicked(event -> {
                sendEmoji(btn.getText());
            });
            btn.setEllipsisString("");
            emojiGridPane.add(btn, i % 3, i / 3);
            GridPane.setHalignment(btn, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(btn, javafx.geometry.VPos.CENTER);
        }
    }

    private void sendEmoji(String text) {
        try {
            dataOutputStream.writeUTF("emoji&" + lblClientName.getText() + "&" + text);
            dataOutputStream.flush();

            List<Node> nodes = TextUtils.convertToTextAndImageNodes(createUnicodeText(text));
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(5, 5, 5, 10));
            hBox.setAlignment(Pos.BASELINE_RIGHT);
            hBox.getChildren().add(nodes.get(0));
            vBoxMessages.getChildren().add(hBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void receiveEmoji(String content) {
        Platform.runLater(() -> {
            List < Node > nodes = TextUtils.convertToTextAndImageNodes(createUnicodeText(content));
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(5, 5, 5, 10));
            hBox.setAlignment(Pos.BASELINE_LEFT);
            hBox.getChildren().add(nodes.get(0));
            vBoxMessages.getChildren().add(hBox);
        });
    }

    @FXML
    void emojiOnAction(MouseEvent event) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    private String createUnicodeText(String nv) {
        StringBuilder unicodeText = new StringBuilder();
        String[] words = nv.split(" ");
        for (String word : words) {
            Optional<Emoji> optionalEmoji = EmojiData.emojiFromShortName(word);
            if (optionalEmoji.isPresent()) {
                unicodeText.append(optionalEmoji.get().character());
            }
        }
        return unicodeText.toString();
    }
    @FXML
    void btnAttachmentOnAction(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);

        Window window = ((Node) event.getTarget()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);


        if (file != null) {
            sendImage(file.toURI().toString());
        }

    }

    private void sendImage(String absolutePath) {
        Image image = new Image(absolutePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBoxMessages.getChildren().add(hBox);

        try {
            dataOutputStream.writeUTF("img&" + "&" + absolutePath);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void receiveImage(String path) {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);

        Platform.runLater(() -> {
            vBoxMessages.getChildren().add(hBox);
        });
    }
    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")
        );
    }

    public void btnLogoutOnAction(MouseEvent event) {
        closeWindow();
    }
    private void closeWindow() {
        Stage stage = (Stage) textFld.getScene().getWindow();
        stage.close();
    }
//
//    public void btnAttachmentOnAction(MouseEvent event) {
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(remoteSocket.getOutputStream());
//            ObjectInputStream ois = new ObjectInputStream(remoteSocket.getInputStream());
//
//            oos.flush();
//            oos.writeObject(new String("Desert.jpg"));
//
//            oos.flush();
//            oos.reset();
//            int sz = (Integer) ois.readObject();
//            System.out.println("Receiving " + (sz / 1024) + " Bytes From Server");
//
//            byte[] b = new byte[sz];
//            int bytesRead = ois.read(b, 0, b.length);
//            for (int i = 0; i < sz; i++) {
//                System.out.print(b[i]);
//            }
//            FileOutputStream fos = new FileOutputStream(new File("demo.jpg"));
//            fos.write(b, 0, b.length);
//            System.out.println("From Server: " + ois.readObject());
//
//            fos.close();
//            oos.close();
//            ois.close();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
}