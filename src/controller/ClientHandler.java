package controller;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMassage("SERVER" + clientUserName + "has entered the chat!");
        }catch (IOException e){
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String massageFromClient;

        while(socket.isConnected()){
            try {
                massageFromClient = bufferedReader.readLine();
                broadcastMassage(massageFromClient);
            }catch (IOException e){
                closeEveryThing(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMassage(String massageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try {
                if(!clientHandler.clientUserName.equals(clientUserName)){
                    clientHandler.bufferedWriter.write(massageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEveryThing(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMassage("SERVER" + clientUserName + "has left the chat!");
    }

    public void closeEveryThing(Socket socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
}
