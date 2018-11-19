package com.chatapp.client;

import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.User;
import com.chatapp.utils.DynamoDBUtil;

public class Menu {
	
	private static DynamoDBUtil dynamoDBUtil = DynamoDBUtil.getInstance();
	private static Client client;
	
	static Scanner s;
	public static void main(String[] args) {
		s = new Scanner(System.in);
		/*boolean fContinue = true;
		do {
			fContinue = sign();
		}while(fContinue);
		do
		{

		}while(appManu());*/
		//register();
		signin();
	}
	public static void register()
	{
		String userName,password;
		Regions region;
		System.out.println("Hello new user, please enter Username");
		userName = s.nextLine();
		System.out.println("Enter password");
		password = s.nextLine();
		region = chooseRegion();
		
		User user = new User(userName,password,region);
		
		//Register
		dynamoDBUtil.register(user);
		System.out.println("Congratulation! You are a member.");
	}
	public static boolean signin()
	{
		String userName,password;
		System.out.println("Hello, Please enter username:");
		userName = s.nextLine();
		System.out.println("Enter password:");
		password = s.nextLine();
		
		//check validation
		User user = dynamoDBUtil.login(userName, password);
		
		if(user != null) {
			Client.connectToChatServer(user,ChatType.PUBLIC);
			return true;
		}else {
			System.out.println("Username or Password are inccorect");
			return false;
		}
	}
	public static boolean appManu()
	{
		int choice = 0;
		String recieverUsername;
		System.out.println("Hello ***, you have  new messages.");
		System.out.println("*1* Press 1 to check out new messages");
		System.out.println("*2* Press 2 to send new message");
		System.out.println("*2* Press 3 to view history");
		System.out.println("*0* Press 0 to exit");
		choice = s.nextInt();
		switch(choice)
		{
		case 0 :
			System.out.println("ByeBye...");
			return false;
		case 1:
			//Retrieve from queue
			return true;
		case 2:
			System.out.println("Enter username");
			recieverUsername = s.nextLine();
			System.out.println("Enter message");
			recieverUsername = s.nextLine();
			//send messgae to receiver queue
			//send message to my queue
			return true;
		case 3:
			//retrieve from bucket
			return true;
		default:
			return true;
		}
	}
	public static boolean sign()
	{
		int choice = 0;
		System.out.println("Welcome to the ChatApp");
		System.out.println("*1* Press 1 to sign up");
		System.out.println("*2* Press 2 to sign in");
		System.out.println("*3* Press 3 to exit");
		s.nextInt();
		switch(choice)
		{
		case 1:
			return true;
			//break;
		case 2:
			if(signin())
				return true;
			//break;
		case 3:
			System.exit(0);
			return false;
		}
		return false;
	}
	
	private static Regions chooseRegion() {
		int choice = 1;
		System.out.println("Please choose your region: ");
		System.out.println("*1* US-WEST");
		System.out.println("*2* US-EAST");
		System.out.println("*3* LONDON");
		choice = s.nextInt();
		switch(choice)
		{
		case 1:
			return Regions.US_WEST_2;
		case 2:
			//if(signin())
			return Regions.US_EAST_2;
		case 3:
			return Regions.EU_WEST_3;
		default:
			return Regions.US_WEST_2;
		}
	}
}
