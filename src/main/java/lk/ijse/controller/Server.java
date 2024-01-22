package lk.ijse.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Socket> clientSockets = new ArrayList<>();
    private List<DataOutputStream> outputStreams = new ArrayList<>();
    private List<DataInputStream> inputStreams = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.initialize();
    }

    public void initialize() {
        try {
            ServerSocket serverSocket = new ServerSocket(3100);
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nClient connected from: " + clientSocket.getInetAddress());

                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                clientSockets.add(clientSocket);
                outputStreams.add(outputStream);
                inputStreams.add(inputStream);

                new Thread(() -> handleClientCommunication(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientCommunication(Socket clientSocket) {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            while (true) {
                String message = inputStream.readUTF();
                System.out.println("\nClient from " + clientSocket.getInetAddress() + " : " + message);

                for (DataOutputStream outputStream : outputStreams) {
                    try {
                        outputStream.writeUTF("Client from " + clientSocket.getInetAddress() + " : " + message.trim());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            // Handle client disconnection or any other IOException
            System.out.println("Client disconnected from: " + clientSocket.getInetAddress());
            int index = clientSockets.indexOf(clientSocket);
            if (index != -1) {
                clientSockets.remove(index);
                outputStreams.remove(index);
                inputStreams.remove(index);
            }
        }
    }
}
