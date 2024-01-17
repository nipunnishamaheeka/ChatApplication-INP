package lk.ijse.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class ServerFromController {
    public TextArea txtMessageView;
    public TextField txtMgs;
    public ImageView btnSent;
    private ServerSocket serverSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String message = "";

    private Socket[] clientSockets = new Socket[3];
    private DataOutputStream[] outputStreams = new DataOutputStream[3];
    private DataInputStream[] inputStreams = new DataInputStream[3];

    private String[] messages = new String[3];

    public void initialize() {
        try {
            ServerSocket serverSocket = new ServerSocket(3100);
            txtMessageView.appendText("Server Start.!");

            for (int i = 0; i < 3; i++) {
                txtMessageView.appendText("\nWaiting for Client-" + (i + 1) + " to connect...!");
                clientSockets[i] = serverSocket.accept();
                txtMessageView.appendText("\nClient-" + (i + 1) + " Connected..!");
                txtMessageView.appendText("\n.............................................\n");

                outputStreams[i] = new DataOutputStream(clientSockets[i].getOutputStream());
                inputStreams[i] = new DataInputStream(clientSockets[i].getInputStream());

                final int clientIndex = i;
                new Thread(() -> handleClientCommunication(clientIndex)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientCommunication(int clientIndex) {
        try {
            while (true) {
                messages[clientIndex] = inputStreams[clientIndex].readUTF();
                txtMessageView.appendText("\nClient-" + (clientIndex + 1) + " : " + messages[clientIndex]);

                for (int i = 0; i < 3; i++) {
                    if (i != clientIndex) {
                        outputStreams[i].writeUTF("Client-" + (clientIndex + 1) + " : " + messages[clientIndex].trim());
                        outputStreams[i].flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void btnSendOnAction(MouseEvent event) throws IOException {
        String serverMessage = "Server : " + txtMgs.getText().trim();

        for (int i = 0; i < 3; i++) {
            outputStreams[i].writeUTF(serverMessage);
            outputStreams[i].flush();
        }
    }
}
