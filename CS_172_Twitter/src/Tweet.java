import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;

public class Tweet 
{
	//Public Variables of Tweet Class
	 public String UserName;
	 public String Text;
	 public GeoLocation Location;
	 public long ID;
	 public String [] URL_Titles = new String [5];
	 public int NumURLTitles;
	 
	 //Basic Constructor
	 public Tweet ( String A, String B, GeoLocation C,long D ) 
	 {
		 UserName = A;
		 Text = B;
		 Location = C;
		 ID = D;
		 NumURLTitles = 0;
	  }
	 
}
