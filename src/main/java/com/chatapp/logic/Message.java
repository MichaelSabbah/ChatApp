package com.chatapp.logic;

import com.amazonaws.services.s3.model.Region;

public class Message{
	
	private String senderUsername;
	private String receiverUsername;
	private Region destinationRegion;
	private String content;
	private MessageType messageType;

	public Message(String senderUsername, String receiverUsername, Region destinationRegion, String content, MessageType messageType) {
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
	
	public Region getDestinationRegion() {
		return destinationRegion;
	}

	public void setDestinationRegion(Region destinationRegion) {
		this.destinationRegion = destinationRegion;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
}
