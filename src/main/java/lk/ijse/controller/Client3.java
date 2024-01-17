package lk.ijse.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client3 {
    public TextArea txtMessageVIew;

    final int PORT = 3100;

    public TextField txtMgs;
    public ImageView btnSent;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    String massage = "", reply = "";

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", PORT);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!massage.equals("Exit")) {
                    massage = dataInputStream.readUTF();
                    txtMessageVIew.appendText("\n"+massage);
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }).start();
    }
    public void btnSendOnAction(MouseEvent event) throws IOException {
        dataOutputStream.writeUTF(txtMgs.getText().trim());
        reply=txtMgs.getText();
        txtMessageVIew.appendText("\nClient-03 : " + reply);
        dataOutputStream.flush();
        txtMessageVIew.clear();
    }
}
