package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
    private File outputFile;
    
    public LambdaFunctionHandler() {}

    // Test purpose only.
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
    	
    	String s3Bucket = null;
    	String s3Key;
    	String s3KeyOutput = "outputFile"; //Key for the new file (the output file, after removing even lines)
    	S3Object inputFileAsObject; 
    	
    	context.getLogger().log("===== event =====" + event.toString());
    		
		//Get the first record from the records list 
		S3EventNotificationRecord record = event.getRecords().get(0);
		String eventName = record.getEventName();
        
		//Get the bucket name and object key
		s3Key = record.getS3().getObject().getKey();
        s3Bucket = record.getS3().getBucket().getName();
        
        //Get the file from the bucket by key
        inputFileAsObject = s3.getObject(new GetObjectRequest(s3Bucket, s3Key));
        
        //Make sure that the lambda function won't be called over and over again
        if(inputFileAsObject.getObjectMetadata().getUserMetadata().containsKey("mod"))
        	return "This file already modified";
        
        //Read the odd lines from the input file to new output file
        try {
        	readOddLinesFromTextInputStream(inputFileAsObject.getObjectContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
        

        //Delete the old file from the bucket
        s3.deleteObject(new DeleteObjectRequest(s3Bucket, s3Key));
        
        //Add MetaData to the output file, to mark the output file as modified
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.addUserMetadata("mod", "true");
        
        //Create InputStream from the output file
        InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Upload the new file to the bucket
        s3.putObject(new PutObjectRequest(s3Bucket, s3KeyOutput,  inputStream,metaData));

        context.getLogger().log("*********** eventName = " + eventName + " from bucket " + s3Bucket+" Key "+s3Key); 
            
        return s3Bucket;
    }
    
    private void readOddLinesFromTextInputStream(InputStream input) throws IOException {
    	BufferedReader reader = null;
    	Writer writer = null;
    	try {
	    	reader = new BufferedReader(new InputStreamReader(input));
	
	        outputFile = File.createTempFile("outputFile", ".txt");
	        outputFile.deleteOnExit();
	        
	         writer = new OutputStreamWriter(new FileOutputStream(outputFile));
	
	        String line = "";
	        int lineIndex = 1;
	        while((line = reader.readLine()) != null){
	        	if((lineIndex % 2) != 0) {
	        		writer.write(line + "\n");
	        	}
	        	lineIndex++;
	        }
    	}finally {
	        writer.close();
	        reader.close();
    	}
    }
    
}