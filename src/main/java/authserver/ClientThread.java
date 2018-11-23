// place this file the path such ends with: ChatServer/server/ClientThread.java

package authserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.User;
import com.chatapp.utils.ChatappUtils;
import com.chatapp.utils.DynamoDBUtil;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private AuthChatServer server;
    private boolean isRegister;
    private DynamoDBUtil dynamoDbUtil;

    public ClientThread(AuthChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        this.dynamoDbUtil = DynamoDBUtil.getInstance();
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
    	
    	Scanner in = null;
    	
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
             in = new Scanner(socket.getInputStream());
            
        	boolean isRegister = Boolean.valueOf(in.nextLine());
            String userName = in.nextLine();
            String password = in.nextLine();
            String regionStr = "";
            if(isRegister)
            	regionStr = in.nextLine();
            // NOTE: if you want to check server can read input, uncomment next line and check server file console.
            System.out.println("Username: " + userName);
            System.out.println("Password: " + password);
            System.out.println("Region: " + regionStr);
            
            Regions region = null;
            if(isRegister)
            	region = ChatappUtils.converFromStringToRegions(regionStr);
            User user = new User(userName,password,region);
            boolean success = false;
            
            String regionReturned = null;
            if(isRegister) {
            	
            	success = dynamoDbUtil.register(user);
            	if(success)
            		regionReturned = user.getRegion().toString();
            }else {
            	
            	User connectedUser = dynamoDbUtil.login(user.getUsername(), user.getPassword());
            	if(connectedUser != null)
            		regionReturned = connectedUser.getRegion().toString();
            }
            
            System.out.println("start write to client");
            
            clientOut.println(regionReturned);
            
            System.out.println("finsih write to client");
            
            clientOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	in.close();
        }
    }
}
