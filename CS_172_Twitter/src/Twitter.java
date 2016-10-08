import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		//System.out.println(args[0]);
		//System.out.println(args[1]);
		//System.out.println(args[2]);
		//System.out.println(args[3]);
		
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
		
		double[][] boundingbox = {{-124.47,24.0},{-66.56,49.3843}}; 
		FilterQuery filter = new FilterQuery(); filter.locations(boundingbox);
		twitterStream.filter(filter);
	}
}
