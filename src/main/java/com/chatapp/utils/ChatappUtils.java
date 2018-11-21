package com.chatapp.utils;

import com.amazonaws.regions.Regions;
import com.chatapp.logic.ChatMessage;

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
			case "US_WEST_3":
				region = Regions.EU_WEST_3;
				break;
		}
		return region;
	}
}
