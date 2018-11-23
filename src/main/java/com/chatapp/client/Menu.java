package com.chatapp.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.User;
import com.chatapp.utils.ChatappUtils;
import com.chatapp.utils.DynamoDBUtil;

import authclient.AuthClient;

public class Menu {
	
	private static DynamoDBUtil dynamoDBUtil = DynamoDBUtil.getInstance();
	private static User user;
	
	static Scanner s;
	public static void main(String[] args) throws IOException {
		s = new Scanner(System.in);
		
		do {
			//Connect to global server
			
			
			//Menu before connected
			WelcomeMenu();
			
			//Menu after connected 
			chatAppMenu();
		}while(true);
		
		//register();
		//signin();
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
		
		System.out.println("Region: " + region);
		
		user = new User(userName,password,region);
		String regionReturned = null;
		
		try {
			regionReturned = AuthClient.connectToChatServer(user, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(regionReturned != null)
			System.out.println("Congratulation! You are a member.\n");
		else
			System.out.println("Somthing get wrong...");

	}
	public static boolean signin()
	{
		String userName,password;
		System.out.println("Hello, Please enter username:");
		userName = s.nextLine();
		System.out.println("Enter password:");
		password = s.nextLine();
		
		//check validation
		//user = dynamoDBUtil.login(userName, password);
		String regionReturned = null;
		user = new User(userName,password,null);
		try {
			regionReturned = AuthClient.connectToChatServer(user, false);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		if(regionReturned != null) {
			System.out.println("You signin successfully");
			user.setRegion(ChatappUtils.converFromStringToRegions(regionReturned));
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
			viewHistory();
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
	
	private static void viewHistory() {
		URL url = null;
		HttpURLConnection con = null;
		InputStream in = null;
		Scanner s = null;
		try {
			String urlStr = ChatappUtils.getBucketUrlByUserRegion(user.getRegion());
			url = new URL(urlStr + user.getUsername());
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			in = con.getInputStream();
			s = new Scanner(con.getInputStream());
			while(s.hasNextLine()) {
				String line = s.nextLine();
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			s.close();
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
