package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerForAndroidClient {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static String message;

    public ServerForAndroidClient() throws RemoteException {
    }

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(8000);

        } catch (IOException e) {
            System.out.println("Could not listen on port: 8000");
        }
        System.out.println("Server started. Listening to the port 8000");

        while (true) {
            try {
                RemoteInterfaceImpl remoteInterface = new RemoteInterfaceImpl();
                clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                message = dataInputStream.readUTF();
                if(message.equals("perform")){
                   String email= remoteInterface.getFirstEmail();
                    dataOutputStream.writeUTF(email);
                }
                System.out.println(message);
                dataInputStream.close();
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            }
        }

    }
}
