import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSeparator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
import twitter4j.JSONArray;
import twitter4j.JSONException;
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
	
	public StringBuffer Buffer = new StringBuffer(1000000);
	
	public void AddTweetToList (Status status)
	{
		String A = status.getUser().getScreenName();
    	String B = status.getText();
    	GeoLocation C = status.getGeoLocation();
    	long D = status.getId();
    	String F = status.getCreatedAt().toString();
    	
    	Tweet obj = new Tweet(A, B, C, D, F);
    	
    	//Split Text into Tokens
		StringTokenizer TweetSplit = new StringTokenizer(B);
	    //Find all parts that begin with "http"
		while (TweetSplit.hasMoreTokens())
		{
	    	 String Temp = TweetSplit.nextToken();
	    	 if(Temp.startsWith("#"))
	    	 {
	    		 ///*
	    		 obj.NumHashTags++;
	    		 String [] TempArray = new String [obj.NumHashTags];
	    		 for (int i = 0; i < obj.NumHashTags - 1; i++ )
	    		 {
	    			 TempArray[i] = obj.HashTags[i];
	    		 }
	    		 
	    		 //System.out.println(Temp);
	    		 TempArray[obj.NumHashTags - 1] = Temp;
	    		 obj.HashTags = TempArray;
	    		 //*/
	    		 //System.out.println(Temp);
	    	 }
	    	 
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
	 				obj.NumURLTitles++;
		    		String [] TempArray1 = new String [obj.NumURLTitles];
		    		 for (int i = 0; i < obj.NumURLTitles - 1; i++ )
		    		 {
		    			 TempArray1[i] = obj.URL_Titles[i];
		    		 }
		    		 
		    		 //System.out.println(Temp);
		    		 TempArray1[obj.NumURLTitles - 1] = title;
		    		 obj.URL_Titles = TempArray1;
	
	 				//System.out.println(title);
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
    	String arrayToJson = null;
    	Buffer = new StringBuffer(1000000);
    	Buffer.append('[');
    	Buffer.append('\n');
		try {	
			for(int i = 0; i < TweetList.size(); i++)
			{
				
				arrayToJson = objectMapper.writeValueAsString(TweetList.get(i));
				Buffer.append(arrayToJson);
				Buffer.append(',');
				Buffer.append('\n');
			}
			
			Buffer.append(']');
				
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File file = new File("Twitter_"+"File_"+CurDate+".txt");
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Buffer.toString());
			bw.close();

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
    	///*
    	if(status.getGeoLocation() != null &&!(DuplicateTweetCheck(status)))
    	{
    		AddTweetToList (status);
    	
    	}
    	//*/
    	
    	
    //	if(!(DuplicateTweetCheck(status)))
    	//{
    	//	AddTweetToList (status);
    	
    //	}
    
    	 
    	//50000 will give a 10MB file
    	//250 for debugging Purposes
    	if (TweetList.size() == 30000)
    	{
    		PrintTweetsToFile ();
    	}
    	System.out.println( TweetList.size() );
    	
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

