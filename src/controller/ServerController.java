package controller;

import com.sun.security.ntlm.NTLMException;
import com.sun.security.ntlm.Server;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    private ServerSocket serverSocket;

    public ServerController(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){

                Socket socket = serverSocket.accept();

                /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("New Client Connected");*/

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e){

        }
    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException{
        ServerSocket serverSocket = new ServerSocket(1234);
        ServerController serverController = new ServerController (serverSocket);
        serverController.startServer();
    }
}
