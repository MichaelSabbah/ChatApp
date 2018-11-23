// place this file the path such ends with: ChatServer/server/ClientThread.java

package com.chatapp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.ChatMessage;
import com.chatapp.utils.ChatappUtils;
import com.chatapp.utils.MessageQueueUtil;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private ChatServer server;
    private MessageQueueUtil messageQueueUtil;
    
    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        //this.messageQueueUtil = new MessageQueueUtil(ChatappUtils.converFromStringToRegions(region));
    }

    public void setMessageQueueUtil(MessageQueueUtil messageQueueUtil) {
		this.messageQueueUtil = messageQueueUtil;
	}

	private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());
            
            //Read user region
            String region = in.nextLine();
            System.out.println("Region: " + region);
            this.messageQueueUtil = new MessageQueueUtil(ChatappUtils.converFromStringToRegions(region));
            
            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    System.out.println(input);
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                    
                	//Send message to SQS
                    String senderUserName = input.split(">")[0];
                	ChatMessage message = new ChatMessage(senderUserName,input);
                	backupMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void backupMessage(ChatMessage message){
    	messageQueueUtil.sendMessage(message);
    }
}
