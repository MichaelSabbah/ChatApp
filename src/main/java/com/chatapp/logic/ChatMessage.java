package com.chatapp.logic;

import com.amazonaws.regions.Regions;

public class ChatMessage{
	
	private String senderUsername;
	private String receiverUsername;
	private Regions destinationRegion;
	private String content;

	public ChatMessage(String senderUsername, String content) {
		super();
		this.senderUsername = senderUsername;
		//this.destinationRegion = destinationRegion;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderUsername() {
		return senderUsername;
	}
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	public String getReceiverUsername() {
		return receiverUsername;
	}
	public void setReceiverUsername(String receiverUsername) {
		this.receiverUsername = receiverUsername;
	}
	
	public Regions getDestinationRegion() {
		return destinationRegion;
	}

	public void setDestinationRegions(Regions destinationRegion) {
		this.destinationRegion = destinationRegion;
	}
}
