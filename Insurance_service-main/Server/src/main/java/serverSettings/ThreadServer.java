package serverSettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer implements Runnable {
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;

    public ThreadServer(int port) {
        this.serverPort = port;
    }

    @Override
    public void run() {
        openServerSocket();
        while(!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("Client " + clientSocket.getPort() + " connected");
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server stopped") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new ServerWork(clientSocket)).start();
        }
        System.out.println("Server stopped") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        System.out.println("Opening server socket...");
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Error opening port " + this.serverPort, e);
        }
    }

}