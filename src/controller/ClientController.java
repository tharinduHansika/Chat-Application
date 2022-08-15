package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.input.InputMethodTextRun;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    public JFXTextArea txtArea;
    public JFXTextField txtMsg;
    public JFXButton btnSend;
    public JFXTextField txtUserName;
    public JFXButton btnLogin;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public ClientController(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMassage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            //Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                //String messageToSend = scanner.nextLine();
                String messageToSend = txtMsg.getText();
                bufferedWriter.write(userName + ":" + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroup;

                while (socket.isConnected()) {
                    try {
                        msgFromGroup = bufferedReader.readLine();
                        System.out.println(msgFromGroup);
                        txtArea.appendText(msgFromGroup);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedReader.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat :");
        String username = scanner.nextLine();
        //String username = txtUserName.getText();
        Socket socket = new Socket("localhost", 1234);
        ClientController clientController = new ClientController(socket, username);
        clientController.listenForMessage();
        clientController.sendMassage();
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMassage();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
    }
}
