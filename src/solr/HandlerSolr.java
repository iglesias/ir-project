package solr;

import gui.PageRankGUI;
import gui.Tweet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HandlerSolr {
	
	/**
	 * Static variables.
	 */
	
	public final static boolean DEBUG = false;
	
	/**
	 * Method that given the parameters of a query, store in the static variable
	 * actTweetsRetrieved the first maxDocumentsRetrieved tweets that  solr give
	 * us.
	 * 
	 * Format of the json response:
	 *  { "responseHeader":{...info...},
	 *	  "response":{ "numFound":2,"start":0,"docs":[{r1},{r2},...{rn}]}
	 *	} 
	 * 
	 */
	public static void saveRetrievedTweets(String formedQuery){
		
		try {
			// Remove the content of the actual tweets.
			PageRankGUI.actTweetsRetrieved.clear();
			
			// Get response information in json format.
			URL url = new URL("http://localhost:8983/solr/select?" +
			                  "q=" + formedQuery + "&wt=json" +
					          "&rows=" + PageRankGUI.maxRetrieved + 
					          "&fl=*,score&sort=score%20desc" + "&indent=true");
			BufferedReader urlInput = new BufferedReader(new InputStreamReader(url.openStream()));      	
	    	
			System.out.println(url.toString());
			
			// Read all the content in variable content.
			String content = "";
			String linea = null;
			while ((linea = urlInput.readLine()) != null)
				content += linea;
			
			// Build a json Object with the all response.
			JSONObject jObject = new JSONObject(content);
			
			// --DEBUG
			if (DEBUG){
				System.out.println("Header : " + jObject.get("responseHeader"));
				System.out.println("Response : " + jObject.get("response"));
				System.out.println("Docs : " + ((JSONObject) jObject.get("response")).get("docs"));	
			}
			
			// Set the number of retrieved documents.
			PageRankGUI.actRetrieved = String.valueOf(((JSONObject) jObject.get("response")).get("numFound"));
			
			// Build a json Array with the objects we have in "docs".
			JSONArray jArray = (JSONArray) ((JSONObject) jObject.get("response")).get("docs");

			// Fill the actual retrieved tweets.
			for (int i=0; i<jArray.length(); i++){
				try {
					// Get the object.
					JSONObject tweet = jArray.getJSONObject(i);
					
					// Get the parameters.
					String profile_url = (String) tweet.get("profile_url");
					String author = (String) tweet.get("screen_name");
					String text = (String) tweet.get("text");
					Double tfidfScore = (Double) tweet.get("score");
					Double pagerankMentionScore = PageRankGUI.pagerankScoresMention.get(author);
					Double pagerankRetweetScore = PageRankGUI.pagerankScoresRetweet.get(author);
					
					// Add to the list.
					PageRankGUI.actTweetsRetrieved.add( new Tweet(profile_url, author, text, tfidfScore,
							pagerankMentionScore, pagerankRetweetScore));
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			// Show the number of objects returned.
			System.out.println("actTweetsRetrieved : " + PageRankGUI.actTweetsRetrieved.size());
		
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Convert to a nice HTML code.
	 */
	public static String convertTweetsToHTML(){

		// Header.
		String content = "<html><head></head><body><table border=1>";
		
		// Set the number of tweets we will show in the JEditorPane.
		int limit;
		if (PageRankGUI.actTweetsRetrieved.size() < Integer.parseInt(PageRankGUI.maxDocumentsLoad))
			limit = PageRankGUI.actTweetsRetrieved.size();
		else
			limit = Integer.parseInt(PageRankGUI.maxDocumentsLoad);
		
		// All the tweets.
		for (int i=0; i<limit; i++) 
				content += PageRankGUI.actTweetsRetrieved.get(i).converToHTML();
		
		// Footer.
		content += "</table></body></html>";
		
		return content;
	}
	
	
	/**
	 * Method that given the parameters of a query return the objects.
	 * If the user doesnt exist and the parameters is:
	 *   - photo : default_photo.
	 *   - description : Is not a follower.
	 */
	public static String getParameter(String parameter, String user){
		try {			
			// Get response information in json format.
			URL url = new URL("http://localhost:8983/solr/select?" +
			                  "q=screen_name:" + user + "&wt=json" +
					          "&rows=1&fl=" + parameter + "&indent=true");
			BufferedReader urlInput = new BufferedReader(new InputStreamReader(url.openStream()));      	
	    	
			// Read all the content in variable content.
			String content = "";
			String linea = null;
			while ((linea = urlInput.readLine()) != null)
				content += linea;
			
			// Build a json Object with the response.
			// { "responseHeader":{...info...},
			//  "response":{ "numFound":2,"start":0,"docs":[{r1},{r2},...{rn}]}
			// } 
			JSONObject jObject = new JSONObject(content);
			JSONArray jArray = (JSONArray) ((JSONObject) jObject.get("response")).get("docs");	
			
			if (jArray.length()>0)
				return (String) jArray.getJSONObject(0).get(parameter);
			else
				return "https://twimg0-a.akamaihd.net/sticky/default_profile_images/default_profile_3_normal.png";
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return "No retrieved documents";
	}
	
	public static void main(String[] args){	
	}
	
}
