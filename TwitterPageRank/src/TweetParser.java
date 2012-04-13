import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import com.twitter.Extractor;
import org.apache.commons.io.FileUtils;


public class TweetParser {

	// Constant (better put as an input parameter).
	private static String path = "C:\\Users\\Jack\\Dropbox\\KTH-stuff\\DD2476 Search Engines and Information Retrieval Systems\\DD2476 Search Engines and Information Retrieval Project\\EmilJack\\tweets";
	
	/** The graph as a hashtables. */
    public static HashMap<String, ArrayList<String>> index = new HashMap<String,ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> index2 = new HashMap<String,ArrayList<String>>();
	
	public static void main(String[] args) {
		// Variables.
    	Extractor extractor = new Extractor();   // To extract @user, #hashtag ...		
    	File dokDir = new File(path);
    	String[] fs = dokDir.list();
		ArrayList<String> userTweets = new ArrayList<String>();	
    	
    	//Read all the files from /Users/ directory
    	if (dokDir.canRead()){
    		if (dokDir.isDirectory()){
    			if ( fs != null ) {
    			    for ( int i=0; i<fs.length; i++ ) {
    			    	// Read the files in the directory	
    			    	userTweets.add(loadUserTweets(fs[i]).toString());
    			    	System.out.println(userTweets.get(i));
    			    }
    			}
    		}
    	}
    	
		//String userTweets = loadUserTweets("Bernard_bahp");
    	//String userTweets = loadUserTweets(directory);
			
		// Jason array with JSon Object (each object would be one tweet).
    	for (int i=0; i<fs.length; i++){
    		try {
	         	JSONArray jsonArray;
				jsonArray = new JSONArray(userTweets.get(i));
	            
				System.out.print("Reading " + fs[i].toString() + " tweets.");
	    	    System.out.println(" (Total : " + jsonArray.length() + ")");
	    	 
		    	// Look the @user references in each of the tweets.
		    	List<String> namesReferenced;
		    	
		    	for (int j = 0; j < jsonArray.length(); j++) {
		    	
		    		System.out.print("Tweet " + j + " : ");
		    		
		    		// Get JSon Object (tweet) in position j.
		    		JSONObject jsonObject = jsonArray.getJSONObject(j);
		    		
		    		// Get the field "Text" in that tweet. (also are others "id", "created_at", ...").
		    		namesReferenced = extractor.extractMentionedScreennames(jsonObject.getString("text"));
		    		ArrayList<String> links = new ArrayList();
		    	    for (String name : namesReferenced) {
		    	        System.out.print(name);
		    	        if (name!="") links.add(name);
		    	        
		    	    }
		    	    if (!links.isEmpty()) index.put(fs[i].replace(".txt", ""), links);
		    	    System.out.println();		    	    
		    	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	String serialized = JSONValue.toJSONString(index);
    	try {
			FileUtils.writeStringToFile(new File("C:\\Users\\Jack\\Dropbox\\KTH-stuff\\DD2476 Search Engines and Information Retrieval Systems\\DD2476 Search Engines and Information Retrieval Project\\EmilJack\\graph\\graph"), serialized);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println();
    	Object[] keylist = getDictionary();
    	for (int k=0; k<keylist.length; k++){    		
    		System.out.println(keylist[k].toString() + ": " + index.get(keylist[k].toString()));    		
    	}
	}	
	
	/**
     *  Returns the dictionary (the set of terms in the index)
     *  as a Object.
     */
    @SuppressWarnings("rawtypes")
	public static Object[] getDictionary() {
    	Set keys = index.keySet();
	   	Object[] keylist= keys.toArray();    	
    	return keylist;
    }
	
	@SuppressWarnings("finally")
	public static String loadUserTweets(String name){
		String content = "";
		try {
			File file = new File(path + "/" + name);
			
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
