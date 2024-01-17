package lk.ijse.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client2 {
    public TextArea txtMessageVIew;
    public TextField txtMgs;
    public ImageView btnSent;
    final int PORT = 3100;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;

    String massage="", reply="";
    public void initialize() {
        new Thread(()-> {
            try {
                socket = new Socket("localhost", PORT);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!massage.equals("Exit")){
                    massage=dataInputStream.readUTF();
                    txtMessageVIew.appendText("\n"+massage);
                }

            } catch (Exception e) {
//            e.printStackTrace();
            }

        }).start();
    }

    public void btnSendOnAction(MouseEvent event) throws IOException {
        dataOutputStream.writeUTF(txtMgs.getText().trim());
        reply=txtMgs.getText();
        txtMessageVIew.appendText("\nClient-02 : " + reply);
        dataOutputStream.flush();
        txtMgs.clear();
    }
}
