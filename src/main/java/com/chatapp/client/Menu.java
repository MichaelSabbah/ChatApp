package com.chatapp.client;

import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.User;
import com.chatapp.utils.DynamoDBUtil;

public class Menu {
	
	private static DynamoDBUtil dynamoDBUtil = DynamoDBUtil.getInstance();
	private static User user;
	
	static Scanner s;
	public static void main(String[] args) {
		s = new Scanner(System.in);
		
		do {		
			//Menu before connected
			WelcomeMenu();
			
			//Menu after connected 
			chatAppMenu();
		}while(true);
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
		
		user = new User(userName,password,region);
		
		//Register
		dynamoDBUtil.register(user);
		System.out.println("Congratulation! You are a member.\n");
	}
	public static boolean signin()
	{
		String userName,password;
		System.out.println("Hello, Please enter username:");
		userName = s.nextLine();
		System.out.println("Enter password:");
		password = s.nextLine();
		
		//check validation
		user = dynamoDBUtil.login(userName, password);
		
		if(user != null) {
			return true;
		}else {
			System.out.println("Username or Password are inccorect");
			return false;
		}
	}

	public static boolean appManu()
	{
		int choice = 0;
		
		System.out.println("*1* Press 1 to connect to the local chat");
		System.out.println("*2* Press 2 to connect to the global chat");
		System.out.println("*2* Press 3 to view history");
		System.out.println("*0* Press 0 to signout");
		
		choice = s.nextInt();
		
		switch(choice)
		{
		case 0 :
			System.out.println("ByeBye...\n");
			return false;
		case 1:
			Client.connectToChatServer(user,ChatType.LOCAL);
			return true;
		case 2:
			Client.connectToChatServer(user,ChatType.GLOBAL);
			//Connect to global server
			return true;
		case 3:
			//Retrieve all messages from bucket
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
		choice = s.nextInt();
		s.nextLine();
		switch(choice)
		{
		case 1:
			register();
			return false;
		case 2:
			return !signin();
		case 3:
			System.out.println("ByeBye...\n");
			System.exit(0);
			return true;
		}
		return true;
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
			return Regions.US_EAST_2;
		case 3:
			return Regions.EU_WEST_3;
		default:
			return Regions.US_WEST_2;
		}
	}
	
	private static void WelcomeMenu(){
		//Menu before connected 
		boolean fContinue = true;
		do {
			fContinue = sign();
		}while(fContinue);
	}
	
	private static void chatAppMenu() {
		
		do
		{
			
		}while(appManu());
	}
}
