package com.chatapp.logic;

import com.amazonaws.regions.Regions;

public class ChatMessage{
	
	private String senderUsername;
	private String receiverUsername;
	private Regions destinationRegion;
	private String content;
	private MessageType messageType;

	public ChatMessage(String senderUsername, String receiverUsername, Regions destinationRegion, String content, MessageType messageType) {
		super();
		this.senderUsername = senderUsername;
		this.receiverUsername = receiverUsername;
		this.destinationRegion = destinationRegion;
		this.content = content;
		this.messageType = messageType;
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

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
}
