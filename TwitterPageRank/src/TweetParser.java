import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.twitter.Extractor;


public class TweetParser {

	// Constant (better put as an input parameter).
	private static String path = ".";
	
	public static void main(String[] args) {
		
		// Variables.
    	Extractor extractor = new Extractor();   // To extract @user, #hashtag ...
		
		// Read the file @user.txt.
		String userTweets = loadUserTweets("Bernard_bahp");		
		System.out.println(userTweets);
			
		// Jason array with JSon Object (each object would be one tweet).
		try {
	         	JSONArray jsonArray;
				jsonArray = new JSONArray(userTweets);
	            
				System.out.print("Reading Bernard_bahp tweets.");
	    	    System.out.println(" (Total : " + jsonArray.length() + ")");
	    	 
		    	// Look the @user references in each of the tweets.
		    	List<String> namesReferenced;
		    	for (int i = 0; i < jsonArray.length(); i++) {
		    	
		    		System.out.print("Tweet " + i + " : ");
		    		
		    		// Get JSon Object (tweet) in position i.
		    		JSONObject jsonObject = jsonArray.getJSONObject(i);
		    		
		    		// Get the field "Text" in that tweet. (also are others "id", "created_at", ...").
		    		namesReferenced = extractor.extractMentionedScreennames(jsonObject.getString("text"));
		    	    for (String name : namesReferenced) {
		    	        System.out.print(name + ",");
		    	    }	
		    	    System.out.println();
		    	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
	
	@SuppressWarnings("finally")
	public static String loadUserTweets(String name){
		String content = "";
		try {
			File file = new File(path + "/" + name + ".txt");
			
			if (file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				content = br.readLine();
				br.close();
			} else {
				System.out.println("File " + file.getPath() + " doesnt exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return content;
		}
	}	
}
