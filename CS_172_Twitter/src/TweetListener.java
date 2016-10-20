import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSeparator;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TweetListener implements StatusListener { 
	
	
	public List <Tweet> TweetList = new ArrayList<Tweet>();
	
	
	@Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }
	
    @Override
    public void onStatus(Status status) 
    {
    	/*
    	while (true){    	
    	System.out.println(TweetList.size());
    	if (TweetList.size() >= 10)
    	{
    		ObjectMapper objectMapper = new ObjectMapper();
    		//Set pretty printing of json
        	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        	String arrayToJson = null;
			try {
				arrayToJson = objectMapper.writeValueAsString(TweetList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println("1. Convert List of person objects to JSON :");
        	System.out.println(arrayToJson);
    		
		}
		*/

    	String A = status.getUser().getScreenName();
    	String B = status.getText();
    	GeoLocation C = status.getGeoLocation();
    	Tweet obj = new Tweet(A, B, C );
    	TweetList.add(obj);
    	System.out.println(TweetList.size());
    	if (TweetList.size() >= 10)
    	{
    		ObjectMapper objectMapper = new ObjectMapper();
    		//Set pretty printing of json
        	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        	String arrayToJson = null;
			try {
				arrayToJson = objectMapper.writeValueAsString(TweetList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
			try {
				writer.writeValue(new File("Lman.txt"), arrayToJson);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
        	System.out.println("1. Convert List of person objects to JSON :");
        	System.out.println(arrayToJson);
    		
		}
    	
    	System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + status.getGeoLocation() );
	
      
    }
    
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }
    
    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
    }
    
    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }
    
    @Override
    public void onStallWarning(StallWarning warning) {
        System.out.println("Got stall warning:" + warning);
    }

}

