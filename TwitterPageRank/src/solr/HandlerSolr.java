package solr;

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
	 * Method that given the parameters of a query return the objects.
	 */
	public static String getRetrievedTweets(String parameter, String value){
		try {
			// Get response information in json format.
			URL url = new URL("http://localhost:8983/solr/select?q=" + parameter + ":" + value + 
					          "&wt=json&indent=true");
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
			
			// --DEBUG
			if (DEBUG){
				System.out.println("Header : " + jObject.get("responseHeader"));
				System.out.println("Response : " + jObject.get("response"));
				System.out.println("Docs : " + ((JSONObject) jObject.get("response")).get("docs"));	
			}
			
			// Build a json Array with the objects we have in "docs".
			JSONArray jArray = (JSONArray) ((JSONObject) jObject.get("response")).get("docs");

			// Show the number of objects returned.
			return convertToHTML(jArray);
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return "No retrieved documents";
	}
	
	
	/**
	 * Convert to a nice HTML code.
	 * HTML document :
	 * <html>
	 * <head></head>
	 * <body> 
	 *	   <p><hr></hr>
	 *	   <img src="url" align="left" hspace="10">
	 *	   <B> userName </B> userLoginName Data </br>
	 *   	<I>Tweet content</I>
	 *	   </p>
	 *</body>
	 */
	public static String convertToHTML(JSONArray tweetsArray){
		
		String content = "<html><head></head><body><table>";
		for (int i=0; i<tweetsArray.length(); i++){
			try {
				JSONObject tweet = tweetsArray.getJSONObject(i);
				
				content += "<tr>";
				content += "<td><img src='" + tweet.get("profile_image_url") + "'></img></td>";
				content += "<td><B>" + tweet.get("screen_name") + "</B><br>";
				content += "<I>" + tweet.get("text") + "</I></td>";
				content += "</tr>";
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		content += "</table></body></html>";
		
		return content;
			
	}
	
	public static void main(String[] args){
		
		getRetrievedTweets("author","Rick");
	
	}
	
}
