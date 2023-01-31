package clientSettings;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket clientSocket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private String message;

    public ClientConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            outStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        try {
            outStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() {
        try {
            message = (String) inStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Object readObject() {
        Object object = new Object();
        try {
            object = inStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void close() {
        try {
            clientSocket.close();
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
