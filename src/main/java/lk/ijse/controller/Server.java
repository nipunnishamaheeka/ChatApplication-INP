package lk.ijse.controller;

import java.io.*;
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

                new Thread(() -> handleClientCommunication(clientSocket,outputStream)).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientCommunication(Socket clientSocket,DataOutputStream dataOutputStream) {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            while (true) {
                String message = inputStream.readUTF();
                System.out.println("\nClient from " + clientSocket.getInetAddress() + " : " + message);

                for (DataOutputStream outputStream : outputStreams) {
                    if (outputStream.equals(dataOutputStream))continue;
                    try {
                        outputStream.writeUTF(message);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            // Handle client disconnection
            System.out.println("Client disconnected from: " + clientSocket.getInetAddress());
            int index = clientSockets.indexOf(clientSocket);
            if (index != -1) {
                clientSockets.remove(index);
                outputStreams.remove(index);
                inputStreams.remove(index);
            }
        }
    }
        public void sim_sock()
        {
            try {
                ServerSocket ss=new ServerSocket(8006);
                System.out.println ("Waiting for request");
                Socket s=ss.accept();
                System.out.println ("Connected With"+s.getInetAddress().toString());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                String req=(String)ois.readObject();
                System.out.println (req);

                File f=new File(req);
                FileInputStream fin=new FileInputStream(f);

                int c;
                int sz=(int)f.length();
                byte b[]=new byte [sz];
                oos.writeObject(new Integer(sz));
                oos.flush();
                int j=0;
                while ((c = fin.read()) != -1) {

                    b[j]=(byte)c;
                    j++;
                }
            /*for (int i = 0; i<sz; i++)
            {
                System.out.print(b[i]);
            }*/



                fin.close();
                oos.flush();
                oos.write(b,0,b.length);
                oos.flush();
                System.out.println ("Size "+sz);
                System.out.println ("buf size"+ss.getReceiveBufferSize());
                oos.writeObject(new String("Ok"));
                oos.flush();
                ss.close();
            }
            catch (Exception ex) {
                System.out.println ("Error"+ex);
            }
        }
//        public static void main (String[] args) {
//            sim_sock ob=new sim_sock();
//        }
    }

