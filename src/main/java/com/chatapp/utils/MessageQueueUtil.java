package com.chatapp.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.chatapp.logic.ChatMessage;
import com.amazonaws.services.sqs.model.Message;

public class MessageQueueUtil {

	private AmazonSQS sqs;
	private Regions region;
	private void init(Regions region) throws Exception {
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
	                           .withRegion(region)
	                           .build();
	}
	
	public MessageQueueUtil(Regions region) {
		try {
			this.region = region;
			init(region);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(ChatMessage message) {

		String sqsUrl = getSQSUrlByRegion();

		//Set message attributes
		Map<String,MessageAttributeValue> attributes = new HashMap<String,MessageAttributeValue>();
		MessageAttributeValue senderUsernameValue = new MessageAttributeValue();
		senderUsernameValue.setDataType("String");
		senderUsernameValue.setStringValue(message.getSenderUsername());	
		attributes.put("senderId", senderUsernameValue);
		
		SendMessageRequest sendMessageRequest = new SendMessageRequest();
		
		sendMessageRequest.withQueueUrl(sqsUrl)  
						  .withMessageAttributes(attributes)
						  .withMessageBody(message.getContent());
		
	    sqs.sendMessage(sendMessageRequest);
	}
	
	public void receiveMessages(Regions region) {
		
		String sqsUrl = getSQSUrlByRegion(); 		
        
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withMessageAttributeNames("All");
        
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
        	System.out.println("Message body: " + message.getBody());
        	System.out.println("senderId: " + message.getMessageAttributes().get("senderId").getStringValue());
        	System.out.println("attributes: " + message.getAttributes());
        }
	}
	
	private String getSQSUrlByRegion(){
		
		String sqsUrl = null;
		
		switch(region) {
			case US_EAST_2:
				sqsUrl = AppConsts.US_EAST_SQS_URL;
				break;
			case US_WEST_2:
				sqsUrl = AppConsts.US_WEST_SQS_URL;
				break;
			case EU_WEST_2:
				sqsUrl = AppConsts.EUORPE_PARIS_SQS_URL;
				break;
		}
		
		return sqsUrl;
	}

    public static void main(String[] args) {
    	
    	MessageQueueUtil messageQueueUtil = new MessageQueueUtil(Regions.US_EAST_2);
    	
    	ChatMessage message = new ChatMessage("Moshe","This is the content of the message");
    	
    	messageQueueUtil.sendMessage(message);
    	
    	//messageQueueUtil.receiveMessages(Regions.US_EAST_2);
    	
    	//Run ec2 instance
        /*AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\USER\\.aws\\credentials), and is in valid format.",
                    e);
        }*/
        //Build EC2 instance
    	/*AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-2")
                .build();
    	
    	try {
            //Create RunEequest for the EC2 instance
            RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
         	runInstancesRequest.withImageId("ami-0cf31d971a3ca20d6")
         		                   .withInstanceType("t2.micro")
         		                   .withMinCount(1)
         		                   .withMaxCount(1);
         		
         	//Run the EC2 instance
         	RunInstancesResult result = ec2.runInstances(runInstancesRequest);
         	
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }*/
    	
    }
	
}
