package com.chatapp.utils;

import com.amazonaws.regions.Regions;

public class ChatappUtils {
	
	public static Regions converFromStringToRegions(String regionStr) {
		Regions region = null;
		switch(regionStr) {
			case "US_EAST_2":
				region = Regions.US_EAST_2;
				break;
			case "US_WEST_2":
				region = Regions.US_WEST_2;
				break;
			case "EU_WEST_3":
				region = Regions.EU_WEST_3;
				break;
			case "EU_CENTRAL_1":
				region = Regions.EU_CENTRAL_1;
				break;
		}
		return region;
	}

	public static String getBucketUrlByUserRegion(Regions region) {
		
		switch(region) {
			case US_EAST_2:
				return "https://chatapp-east-messages-bucket.s3.amazonaws.com/";
			case US_WEST_2:
				return "https://chatapp-west-messages-bucket.s3.amazonaws.com/";
			case EU_WEST_3:
				return "https://chatpp-paris-messages-bucket.s3.amazonaws.com/";
			case EU_CENTRAL_1:
				return "https://chatapp-global-messages-bucket.s3.amazonaws.com/";
			default:
				return null;
		}
	}
}
