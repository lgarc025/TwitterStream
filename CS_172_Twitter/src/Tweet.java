import twitter4j.GeoLocation;

public class Tweet 
{
	//Public Variables of Tweet Class
	 public String UserName;
	 public String Text;
	 public GeoLocation Location;
	 public long ID;
	 
	 //Basic Constructor
	 public Tweet ( String A, String B, GeoLocation C,long D ) 
	 {
		 UserName = A;
		 Text = B;
		 Location = C;
		 ID = D;
	        
	  }
	 
}
