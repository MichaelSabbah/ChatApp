package com.chatapp.logic;

public class Message{
	
	private String senderUsername;
	private String receiverUsername;
	private Locations destinationId;
	private String content;
	private MessageType messageType;
	
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Message(String senderUsername, String receiverUsername, Locations destinationId, String content) {
		super();
		this.senderUsername = senderUsername;
		this.receiverUsername = receiverUsername;
		this.destinationId = destinationId;
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
	
	public Locations getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Locations destinationId) {
		this.destinationId = destinationId;
	}

}
