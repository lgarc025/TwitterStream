import java.util.ArrayList;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter {
	
	public static List <Tweet> TweetList = new ArrayList<Tweet>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		
		ConfigurationBuilder confBuilder = new twitter4j.conf.ConfigurationBuilder();
		confBuilder.setOAuthConsumerKey(args[0]); 
		confBuilder.setOAuthConsumerSecret(args[1]); 
		confBuilder.setOAuthAccessToken(args[2]); 
		confBuilder.setOAuthAccessTokenSecret(args[3]); 
		Configuration config = confBuilder.build();
		
		
		TwitterStream twitterStream = new TwitterStreamFactory(config).getInstance();
		System.out.println(twitterStream);
		TweetListener listener = new TweetListener(); 
		twitterStream.addListener(listener);
		twitterStream.sample("en");
		//twitterStream.sample();
		
		double[][] boundingbox = {{-117.92,33.64},{-71.15,42.16}}; 
		FilterQuery filter = new FilterQuery(); 
		filter.locations(boundingbox);
		twitterStream.filter(filter);
	}
}
