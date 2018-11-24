// place this file the path such ends with: ChatServer/client/Client.java

package com.chatapp.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.User;
import com.chatapp.utils.AppConsts;

public class Client {

    private static final int PORT_NUMBER = 1234;
    private static final String EXIT_CHAT = "~exit";
    private static User user;
    
    private String serverHost;
    private int serverPort;

    private Client(User user, String host, int portNumber){
        Client.user = user;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(){
    	Scanner scan = null;
    	try{
        	scan = new Scanner(System.in);
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.
            System.out.println("Before open thread: " + user.getRegion().toString());
            ServerThread serverThread = new ServerThread(socket, user.getUsername(),user.getRegion().toString());
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
           
            while(serverAccessThread.isAlive()){

            	if(scan.hasNextLine()){
                	
                	String messageContent = scan.nextLine();
                	System.out.println("Message before sent to serverThread: " + messageContent);
                	serverThread.addNextMessage(messageContent);
                	
                	if(EXIT_CHAT.equals(messageContent))
                		break;
                }

                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: If you use buffered reader or something else not waiting way,
                // NOTE: I recommends write waiting short time like following.
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }finally {
        	scan.close();
        }
    }
    
    public static void connectToChatServer(User user,ChatType chatType) {
    	String host = "";
    	if(chatType.equals(ChatType.LOCAL)) {
    		host = "public-ip";
    	}else {
    		host = getServerByLocation(user.getRegion());
    	}
    	Client.user = user;
        Client client = new Client(Client.user, host, PORT_NUMBER);
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
    		case EU_CENTRAL_1:
    			serverIp = AppConsts.GLOBAL_SERVER_IP;
    		default:
    			break;
    	}
    	return serverIp;
    }
}
