// place this file the path such ends with: ChatServer/client/Client.java

package com.chatapp.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.ChatMessage;
import com.chatapp.logic.User;
import com.chatapp.utils.AppConsts;
import com.chatapp.utils.ChatappUtils;
import com.chatapp.utils.MessageQueueUtil;
import com.chatapp.utils.SQSEventUtil;

public class Client {

    private static final String host = "localhost";
    private static final int portNumber = 1234;
    private static final String WRITING_SIGN = ">";
    private static final String EXIT_CHAT = "~exit";
    private static User user;
    
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;
    private MessageQueueUtil messageQueueUtil;
    private Map<Integer,String> friends;

    /*public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }


    }*/

    private Client(User user, String host, int portNumber){
        Client.user = user;
        this.serverHost = host;
        this.serverPort = portNumber;
        this.messageQueueUtil = new MessageQueueUtil(user.getRegion());
    }

    private void startClient(){
        try{
        	Scanner scan = new Scanner(System.in);
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.

            ServerThread serverThread = new ServerThread(socket, user.getUsername());
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                	
                	String messageContent = scan.nextLine();
                	serverThread.addNextMessage(messageContent);
                	
                	if(EXIT_CHAT.equals(messageContent))
                		break;
                	
                	//Send message to SQS
                	ChatMessage message = new ChatMessage(user.getUsername(),messageContent);
                	backupMessage(message);
                }
                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: If you use buffered reader or something else not waiting way,
                // NOTE: I recommends write waiting short time like following.
//                 else {
//                    Thread.sleep(200);
//                 }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
    
    public static void connectToChatServer(User userToConnect,ChatType chatType) {

    	String host = "localhost";
    	
    	/*if(chatType.equals(ChatType.LOCAL)) {
    		host = "public-ip";
    	}else {
    		host = getServerByLocation(user.getRegion());
    	}*/
    	user = userToConnect;
        Client client = new Client(userToConnect, host, portNumber);
        client.startClient();
    }
    
    private static String getServerByLocation(Regions region) {
    	String serverIp = null;
    	switch(region) {
    		case US_EAST_2:
    			serverIp = AppConsts.US_EAST_SERVER_IP;
    			break;
    		case US_WEST_2:
    			serverIp = AppConsts.US_WEST_SERVER_IP;
    			break;
    		case EU_WEST_3:
    			serverIp = AppConsts.EUORPE_PARIS_SERVER_IP;
    			break;
    		default:
    			break;
    	}
    	return serverIp;
    }
    
    private void backupMessage(ChatMessage message){
    	messageQueueUtil.sendMessage(message);
    }
}
