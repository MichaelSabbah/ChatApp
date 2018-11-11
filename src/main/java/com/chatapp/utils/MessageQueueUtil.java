package com.chatapp.utils;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.chatapp.logic.Locations;
import com.chatapp.logic.Message;
import com.chatapp.logic.MessageType;

public class MessageQueueUtil {
	
	
	private static MessageQueueUtil messageQueueUtil;
	private AmazonSQS sqs;
	
	private void init() throws Exception {
	    /*
	     * The ProfileCredentialsProvider will return your [default]
	     * credential profile by reading from the credentials file located at
	     * (C:\\Users\\USER\\.aws\\credentials).
	     */
	    ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
	    try {
	        credentialsProvider.getCredentials();
	    } catch (Exception e) {
	        throw new AmazonClientException(
	                "Cannot load the credentials from the credential profiles file. " +
	                "Please make sure that your credentials file is at the correct " +
	                "location (C:\\Users\\USER\\.aws\\credentials), and is in valid format.",
	                e);
	    }
	
	    sqs = AmazonSQSClientBuilder.standard()
	                           .withCredentials(credentialsProvider)
	                           .withRegion(Regions.US_WEST_2)
	                           .build();
	}
	
	private MessageQueueUtil() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MessageQueueUtil getInstance(){
		
		if(messageQueueUtil == null) {
			messageQueueUtil = new MessageQueueUtil();
		}
		
		return messageQueueUtil;
	}

	public void sendMessage(Message message) {
		String sqsUrl = getSQSUrlByRegion(message.getDestinationRegion());
		
		//Set message attributes
		Map<String,MessageAttributeValue> attributes = new HashMap<String,MessageAttributeValue>();
		MessageAttributeValue receiverUsernameValue = new MessageAttributeValue();
		receiverUsernameValue.setDataType("String");
		receiverUsernameValue.setStringValue(message.getReceiverUsername());
		attributes.put("receiverUsername", receiverUsernameValue);
		
		SendMessageRequest sendMessageRequest = new SendMessageRequest();
		
		if(message.getMessageType().equals(MessageType.IMAGE)) { //Sending image
			
			//add bucket
			
			
			MessageAttributeValue imageIdValue = new MessageAttributeValue();
			imageIdValue.setDataType("String");
			imageIdValue.setStringValue("imageId");
			attributes.put("imageIdValue", imageIdValue);
		}
		
		sendMessageRequest.withQueueUrl(sqsUrl)  
						  .withMessageAttributes(attributes)
						  .withMessageBody(message.getContent());
		
	    sqs.sendMessage(sendMessageRequest);
	}
	
	
	public void receiveMessages(Region region) {
		
	}
	
	private String getSQSUrlByRegion(Region ragion  /*Locations locationId*/){
		
		String sqsUrl = null;
		
		switch(ragion) {
			case US_East_2:
				sqsUrl = AppConsts.US_EAST_SQS_URL;
				break;
			case US_West_2:
				sqsUrl = AppConsts.US_WEST_SQS_URL;
				break;
			case EU_London:
				sqsUrl = AppConsts.EUORPE_LONDON_SQS_URL;
				break;
		}
		
		return sqsUrl;
	}
	
	public void receiveMessage() {
		
	}
	
	
    public static void main(String[] args) {
    	
    	MessageQueueUtil messageQueueUtil = MessageQueueUtil.getInstance();
    	
    	Message message = new Message("Amir","Moshe",Region.US_West_2,"This is the content of the message",MessageType.TEXT);
    	
    	messageQueueUtil.sendMessage(message);
    	
    }
	
}
