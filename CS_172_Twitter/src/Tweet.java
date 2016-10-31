import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;

public class Tweet 
{
	//Public Variables of Tweet Class
	 public String UserName;
	 public String Text;
	 public GeoLocation Location;
	 public long ID;
	 public String [] URL_Titles;
	 public int NumURLTitles;
	 public String [] HashTags;
	 public int NumHashTags;
	 public String TweetDate; 
	 
	 //Basic Constructor
	 public Tweet ( String A, String B, GeoLocation C,long D, String F ) 
	 {
		 UserName = A;
		 Text = B;
		 Location = C;
		 System.out.println(C);
		 ID = D;
		 NumURLTitles = 0;
		 NumHashTags = 0;
		 TweetDate = F;
	  }
	 
}
