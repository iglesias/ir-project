/*
 *   This file is part of the computer assignment for the Information Retrieval
 *   course at KTH.
 *
 *   Students:       Jack Ha, Emil Broqvist Widham 2012
 *
 *   Date : 22/04/2012.
 */  

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.lang.String;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.twitter.Extractor;

import com.larvalabs.megamap.MegaMapException;
import com.larvalabs.megamap.MegaMapManager;
import com.larvalabs.megamap.MegaMap;

public class TweetParser {

	// Constant (better put as an input parameter).
	private static String path = "C:\\Users\\Jack\\Dropbox\\KTH-stuff\\DD2476 Search Engines and Information Retrieval Systems\\DD2476 Search Engines and Information Retrieval Project\\EmilJack\\tweets";
	private static String path2 = "C:\\Users\\Jack\\Dropbox\\KTH-stuff\\DD2476 Search Engines and Information Retrieval Systems\\DD2476 Search Engines and Information Retrieval Project\\BerFer\\data\\BowieState\\TweetsList";
	private static String followers = "C:\\Users\\Jack\\Dropbox\\KTH-stuff\\DD2476 Search Engines and Information Retrieval Systems\\DD2476 Search Engines and Information Retrieval Project\\BerFer\\followers_BowieState_1334011733253.txt";
	
	/** Store the screen names in a Hashmap. */
    public static HashMap<String, String> followerIndex = new HashMap<String, String>();
	
    private static MegaMap index;
    private static MegaMapManager manager;
    private static ArrayList<JSONObject> TweetList = new ArrayList<JSONObject>();
    private static int id = 0;
    
	public static void main(String[] args) throws MegaMapException, JSONException {
		// Variables.
    	Extractor extractor = new Extractor();   // To extract @user, #hashtag ...		
    	File dokDir = new File(path2);
    	String[] fs = dokDir.list();
		ArrayList<String> userTweets = new ArrayList<String>();		
		
		try {
		    manager = MegaMapManager.getMegaMapManager();
		    index = manager.createMegaMap( "graph", path.replace("\\tweets", "\\graph"), true, false );
		    
		    /** Open the followers list file */
		    FileInputStream fstream = new FileInputStream(followers);
		    
		    /** Get the object of DataInputStream */
		    DataInputStream in = new DataInputStream(fstream);		    
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    String strLine;
		    
		    /** Read File Line By Line and add to followerIndex */
		    while ((strLine = br.readLine()) != null)   {
		    	String [] splitted = new String[2];
		    	splitted = strLine.split(";");
		    	followerIndex.put(splitted[1], ";");		    	
		    }
		    /** Close the input stream */
		    in.close();
		}
		catch ( Exception e ) {
		    e.printStackTrace();
		}
    	
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
			
		// Jason array with JSon Object (each object would be one tweet).
    	for (int i=0; i<fs.length; i++){
    		try {
	         	JSONArray jsonArray;
				jsonArray = new JSONArray(userTweets.get(i));
	            
				System.out.print("Reading " + fs[i].toString() + " tweets.");
	    	    System.out.println(" (Total : " + jsonArray.length() + ")");
	    	 
		    	// Look the @user references in each of the tweets.
		    	List<String> namesReferenced;
		    	
		    	
		    	/**Loop through every tweet for user fs[i]*/
		    	for (int j = 0; j < jsonArray.length(); j++) {
		    	
		    		System.out.print("Tweet " + j + " : ");
		    		
		    		// Get JSon Object (tweet) in position j.
		    		JSONObject jsonObject = jsonArray.getJSONObject(j);
		    		
		    		// Get the field "Text" in that tweet. (also are others "id", "created_at", ...").
		    		namesReferenced = extractor.extractMentionedScreennames(jsonObject.getString("text"));
		    		
		    		LinksList links = new LinksList();
		    		LinksList listOfLinksEntry = null;
		    		
		    		/** Write all Tweets to JSON Files - these JSON Files are later used for indexing on Solr **/
		    		JSONObject Tweets = new JSONObject();
		    		Tweets.put("id", id);					
					Tweets.put("author", fs[i].replace(".json", ""));
					Tweets.put("title", jsonObject.getString("text"));
					TweetList.add(Tweets);
		    		id++;
		    		if (index.hasKey(fs[i].replace(".json", ""))){		    			
		    			listOfLinksEntry = (LinksList) index.get(fs[i].replace(".json", ""));		    			
		    		}
		    		
		    	    for (String name : namesReferenced) {
		    	        System.out.print(name);
		    	        if (name!=""){
		    	        	if (!index.hasKey(fs[i].replace(".json", ""))){
				    			links.add(name);
				    			index.put(fs[i].replace(".json", ""),links);		    			
				    		}
				    		else{
				    			boolean tru = false;
				    			for (int k = 0; k<listOfLinksEntry.size(); k++){
				    				System.out.println(listOfLinksEntry.get(k).getScreenName());
				    				if (name.toString().equals(listOfLinksEntry.get(k).getScreenName())){				    					
				    					listOfLinksEntry.get(k).addOne();
				    					k=listOfLinksEntry.size()+1;
				    					tru = true;
				    				}
				    			}
				    			if (!tru) listOfLinksEntry.add(name);
				    		}
		    	        }
		    	        
		    	    }
		    	    System.out.println();		    	    
		    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	
    	System.out.println();
    	Object[] keylist = getDictionary(1);
    	
    	/** Create Output Graph.txt File */
    	try {
			FileWriter fstream = new FileWriter(path.replace("\\tweets", "\\graph\\") + "graph.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			
			for (int k=0; k<keylist.length; k++){		
				//String outPutRow = keylist[k].toString() + ";";				
	    		try {
	    			LinksList temp = (LinksList) index.get(keylist[k].toString());
	    			
	    			/**Loop through all LinksEntries that exist in the current LinksList for the current key (screenname)*/
	    			for (int y = 0; y<temp.size(); y++){
	    				LinksEntry tempEntry = temp.get(y);
	    				followerIndex.put(keylist[k].toString(), followerIndex.get(keylist[k].toString()) + tempEntry.getScreenName() + "(" + tempEntry.getNumberOfPaths() + "),");
	    				//outPutRow = outPutRow + tempEntry.getScreenName() + "(" + tempEntry.getNumberOfPaths() + "),";	    				
	    			}
	    			followerIndex.put(keylist[k].toString(), followerIndex.get(keylist[k].toString()));
	    			//out.write(outPutRow + "\n");
					System.out.println(keylist[k].toString() + ": " + index.get(keylist[k].toString()));
				} catch (MegaMapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    		
	    	}
			Object[] allFollowers = getDictionary(2);
			for (int p = 0; p<allFollowers.length;p++){
				out.write(allFollowers[p].toString() + followerIndex.get(allFollowers[p].toString()) + "\n");
			}			
			out.close();
			try {
				 
				FileWriter file = new FileWriter(path.replace("\\tweets", "\\graph") + "\\tweets.json");
				file.write("[\n");
				for (int i = 0; i<TweetList.size(); i++){
					if (i!=TweetList.size()-1){
						file.write(TweetList.get(i).toString()+",\n");
					}
					else {
						file.write(TweetList.get(i).toString()+"\n");
					}
				}
				file.write("]");
				file.flush();
				file.close();
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 	
    	
    	manager.shutdown();
	}	
	
	/**
     *  Returns the dictionary (the set of terms in the index)
     *  as a Object.
     */
    @SuppressWarnings("rawtypes")
	public static Object[] getDictionary(int n) {
    	Object[] keylist = null;
    	Set keys = null;
    	if (n == 1){
    		keys = index.getKeys();    		
    	}
    	else if (n == 2){
    		keys = followerIndex.keySet();    		
    	}    	
    	keylist= keys.toArray();	
    	return keylist;
    }
	
	@SuppressWarnings("finally")
	public static String loadUserTweets(String name){
		String content = "";
		try {
			File file = new File(path2 + "/" + name);
			
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
