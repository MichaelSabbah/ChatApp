package com.chatapp.utils;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.metrics.internal.cloudwatch.spi.RequestMetricTransformer.Utils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.chatapp.logic.User;

public class DynamoDBUtil {
	
	private static DynamoDBUtil dyanmoDBUtil;//Singleton
	private AmazonDynamoDB dynamoDB;
	
    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    private void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\USER\\.aws\\credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            //credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\USER\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
           //.withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build(); 
}
	
    private DynamoDBUtil() {
    	try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static DynamoDBUtil getInstance() {
    	if(dyanmoDBUtil == null)
    		dyanmoDBUtil = new DynamoDBUtil();
    	
    	return dyanmoDBUtil;
    }
	
	public boolean register(User user) {
        Map<String, AttributeValue> item = newUserItem(user);
        PutItemRequest putItemRequest = new PutItemRequest(AppConsts.USERS_TABLE_NAME,item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        if(putItemResult != null)
        	return true;
        return false;
        //System.out.println("Result: " + putItemResult);
	}
	
	public User login(String username,String password) {
        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition userNameCondition = new Condition()
        	.withComparisonOperator(ComparisonOperator.EQ.toString())
            .withAttributeValueList(new AttributeValue(username));
        scanFilter.put("username", userNameCondition);
        ScanRequest scanRequest = new ScanRequest(AppConsts.USERS_TABLE_NAME).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        
        User user = null;
        
        if(scanResult.getCount() > 0) {
        	if(password.equals(scanResult.getItems().get(0).get("password").getS())) {
        		System.out.println("Connected...");
        		//System.out.println("ScanResult: " + scanResult);

        		String regionStr = scanResult.getItems().get(0).get("location").getS();
        		Regions region = ChatappUtils.converFromStringToRegions(regionStr);
        		user = new User(username,password,region);
        	}else {
        		System.out.println("password incorrect");
        	}
        }else {
        	System.out.println("username or password incorrect");
        }
        return user;
	}

    private Map<String, AttributeValue> newUserItem(User user) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("username", new AttributeValue(user.getUsername()));
        item.put("password", new AttributeValue(user.getPassword()));
        item.put("location", new AttributeValue().withS(user.getRegion().toString()));
        return item;
    }
    
    public static void main(String[] args) {
    	DynamoDBUtil dynamoDBUtil = DynamoDBUtil.getInstance();
    	User user = new User("Amir","12345",Regions.US_WEST_2);
    	dynamoDBUtil.register(user);
    	System.out.println("Register");
    	dynamoDBUtil.login("Amir", "12345");
    	dynamoDBUtil.login("Amir", "1234");
    	dynamoDBUtil.login("Amirr", "12345");
    }
}
