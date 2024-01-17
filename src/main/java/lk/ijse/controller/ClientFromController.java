package lk.ijse.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFromController {

    public TextArea txtMessageVIew;
    public TextField txtMgs;
    public ImageView btnsent;
    private String message = "";
    private DataInputStream inputStream;
    private Socket remoteSocket;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                remoteSocket = new Socket("localhost", 3100);
                DataOutputStream dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
                System.out.println("hi server");
                dataOutputStream.writeUTF("hi server!");

                DataInputStream dataInputStream = new DataInputStream(remoteSocket.getInputStream());
                String s = dataInputStream.readUTF();
                System.out.println(s);

                while (!message.equals("end")) {
                    //String text = scanner.nextLine();
                    //dataOutputStream.writeUTF(text);
                    message = dataInputStream.readUTF();
                    txtMessageVIew.appendText("Server : " + message);
                    System.out.println(dataInputStream.readUTF());
                }
//            dataOutputStream.flush();
//            dataOutputStream.close();
//            remoteSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    public void btnSendOnAction(MouseEvent event) {
        try {
            dataOutputStream.writeUTF(txtMessageVIew.getText());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

