import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSeparator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import java.util.Date;
import java.util.HashSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.regex.*;


public class TweetListener implements StatusListener { 
	
	
	public List <Tweet> TweetList = new ArrayList<Tweet>();
	public HashSet<Long> TweetHash = new HashSet<Long>();
	
	public void AddTweetToList (Status status)
	{
		String A = status.getUser().getScreenName();
    	String B = status.getText();
    	GeoLocation C = status.getGeoLocation();
    	long D = status.getId();
    	
    	Tweet obj = new Tweet(A, B, C, D );
    	
    	//Split Text into Tokens
		StringTokenizer TweetSplit = new StringTokenizer(B);
	    //Find all parts that begin with "http"
		while (TweetSplit.hasMoreTokens())
		{
	    	 String Temp = TweetSplit.nextToken();
	    	 if(Temp.startsWith("http"))
	    	 {
	    		Document doc = null;
	 			try {
	 				doc = Jsoup.connect(Temp).get();
	 			} catch (IOException e) {
	 				// TODO Auto-generated catch block
	 				//e.printStackTrace();
	 			} 
	 			
	 			String title = null;
	 			
	 			if (doc != null)
	 			{
	 				title = doc.title();
	 			}
	 			
	 			if(title != null)
	 			{
	 				obj.URLTitle.add(title);
	 				System.out.println(title);
	 			}
	    	 }
	         
	     }
    	TweetList.add(obj);
	}
	
	public void PrintTweetsToFile ()
	{
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
	 	//get current date time with Date()
	 	Date date = new Date();
	 	String CurDate = dateFormat.format(date);
	 	  
	 	   
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
			writer.writeValue(new File("Twitter_"+"File_"+CurDate+".txt"), arrayToJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Empty the List After Written To File
		TweetList.clear();
	
	}
	
	//Return true if Tweet exists false otherwise
	public Boolean DuplicateTweetCheck (Status status)
	{
		//TweetID
		long IDValue = status.getId();
		
		//Clear Hash After 5000
		if(TweetHash.size() == 5000)
		{
			//Empty the Hash
			TweetHash.clear();
		}
		
		if (TweetHash.contains(IDValue))
		{
			//System.out.print("In Hash");
			return true;
		}
		else
		{
			TweetHash.add(IDValue);
			//System.out.print("Not in Hash");
			
		}
		
		return false;
	}
	
	
	@Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }
	
    @Override
    public void onStatus(Status status) 
    {
    	
    	//Get the Tweet 
    	//Make Sure it has a GeoLocation
    	if(status.getGeoLocation() != null && !(DuplicateTweetCheck(status)))
    	{
    		AddTweetToList (status);
    	
    	}
    	 
    	//50000 will give a 10MB file
    	//250 for debugging Purposes
    	if (TweetList.size() == 250)
    	{
    		PrintTweetsToFile ();
    	}
    	
    	//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + status.getGeoLocation() );
	
      
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

