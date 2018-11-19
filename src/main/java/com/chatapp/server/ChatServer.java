// place this file the path such ends with: ChatServer/server/ChatServer.java

package com.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {

    private static final int portNumber = 1234;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"
    private Map<Integer,ClientThread> connectedClients;
    
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ClientThread>();
        connectedClients = new HashMap<Integer,ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                
                //Login process
                
                
                //System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
                //connectedClients.put(,socket);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}
