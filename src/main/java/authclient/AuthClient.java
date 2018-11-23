// place this file the path such ends with: ChatServer/client/Client.java

package authclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.chatapp.logic.User;
import com.chatapp.utils.AppConsts;

public class AuthClient {

    private static final int portNumber = 5555;
    private static User user;  
    private static boolean isRegister;
    
    private String serverHost;
    private int serverPort;

    private AuthClient(User user, String host, int portNumber){
        AuthClient.user = user;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private String startClient() throws IOException{
    	String regionReturned = null;
    	Socket socket = null;
    	Scanner serverIn = null;
        try{
             socket = new Socket(serverHost, serverPort);
             Thread.sleep(1000); // waiting for network communicating.

	    	 PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
	         InputStream serverInStream = socket.getInputStream();
	         serverIn = new Scanner(serverInStream);
	         
	         if(isRegister)
	        	 serverOut.println(true);
	         else
	        	 serverOut.println(false);
	         
	         serverOut.println(user.getUsername());
	         serverOut.println(user.getPassword());
	         if(isRegister)
	        	 serverOut.println(user.getRegion());
	         serverOut.flush();
	         
	         regionReturned = serverIn.nextLine();
	         
	         System.out.println("Region returned: " + regionReturned);
	         
	        }catch(IOException ex){
	            System.err.println("Fatal Connection error!");
	            ex.printStackTrace();
	        }catch(InterruptedException ex){
	            System.out.println("Interrupted");
	        }finally {
	        	socket.close();
	        	serverIn.close();
	        }
        
        return regionReturned;
    }
    
    public static String connectToChatServer(User user, boolean isRegister) throws IOException {
    	AuthClient.user = user;
    	String host = AppConsts.GLOBAL_SERVER_IP;
    	AuthClient.isRegister = isRegister;
        AuthClient client = new AuthClient(AuthClient.user, host, portNumber);
        return client.startClient();
    }
}
