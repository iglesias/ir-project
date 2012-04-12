/* Date : 9/04/2012                                                            *
 * Description : This class download the tweets of a group of followers        *
 *               given in an ArrayList.                                        *
 *                                                                             *
 * Improvements : I am saving the  last @user, if instead of that I save all,  *             
 *                later I could remove from the  original array all the users  *
 *                I have  already  done  and  then works with the rest of the  *
 *                array.                                                       *
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bernard Hernandez Perez
 */

public class SaveTweets {

	/**
	 * Auxiliary structure to keep track of the IDs that have already been 
	 * associated with a screen name and those that have not
	 */
	public static HashMap<String, Boolean> idToNumberTweets = 
			new HashMap<String, Boolean>();

	private static int nTweetsResolved = 0;

	private static int nTweetsTotal = 0;
	
	private static int nTweetsNew = 0;
	
	private static int nTweetsToDownload = 50;
	
	/**
	 * Show some debugging information
	 */
	public static final boolean DEBUG_OUT = false;
	
	/**
	 * TODO explanation
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("usage: SaveTweets " + 
					"<ScreenNamesFile>");
			return;
		}
		
		// Read the available information from the file and populate the 
		// HashMap
		try {

			File file = new File(args[0]);
			
			if (file.exists()) {
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ( (line = br.readLine()) != null ) {
					String[] tokens = line.split(";");
					
					if (tokens.length < 2) {
						idToNumberTweets.put(tokens[0], false);
					} else {
						idToNumberTweets.put(tokens[0], true);
						nTweetsResolved++;
					}
					
					nTweetsTotal++;
				}
				br.close();
				
			} else {
				System.err.println("File " + args[0] + "not found");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Followers file loaded, " + nTweetsResolved + 
				" IDs out of " + nTweetsTotal + " are associated");
		
		if (DEBUG_OUT) {
			System.out.println(">>>> State of the HashMap after reading");
			
			// Display the state of the HashMap
			Iterator<String> it = idToNumberTweets.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				boolean value = idToNumberTweets.get(key);
				if (value)
					System.out.println(key + " -> " + value);
				else
					System.out.println(key);
			}
		}
		
		// Get the screen names for the IDs that have not been associated yet
		Iterator<String>  it = idToNumberTweets.keySet().iterator();
		while (it.hasNext()) {
			
			String key = it.next();
			boolean value = idToNumberTweets.get(key);
			
			if (!value) {
				
				// Get the screen_name associated to this follower ID
				
				try {
					// Get user information in .json
					URL url = new URL("https://twitter.com/statuses/user_timeline/" +
							key + ".json?count=" + nTweetsToDownload);
				
					BufferedReader urlInput = new BufferedReader(
								new InputStreamReader(url.openStream()));      	
			    	
			    	String content = urlInput.readLine();
				
			    	// DEBUG : Knowing the number of tweets.
			    	if (DEBUG_OUT) {
					    JSONArray jsonArray = new JSONArray(content);
					    System.out.println("User:" + key + " nTweets:" + jsonArray.length());
			    	}
				    
			    	StringBuffer urlOutput = new StringBuffer(content); 
			    	
					// Save it in a file.
					try {
						File file = new File("TweetsList/" + key + ".json");
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						bw.write(urlOutput.toString());		
						bw.close();			
					} catch (IOException e) {
						System.out.println("Error saving the Tweetsfile.");
						e.printStackTrace();
					}
			    						
				    urlInput.close();
					
					// Overwrite the previous value associated to this key
					idToNumberTweets.put(key, true);

					nTweetsNew++;
				} catch (FileNotFoundException e) {
					System.out.println("ID " + key + " not found!!");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Limit reached, " + nTweetsNew + 
						" new IDs associated, saving HashMap ...");
					saveIDsToScreenNames(args[0]);
					//e.printStackTrace();
					System.out.println(e.getMessage().split(" ")[5]);
					System.err.println(e.getMessage());
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
		
		// All the IDs have been associated
		System.out.println("Assocation complete! Saving HashMap ...");
		saveIDsToScreenNames(args[0]);
	}

	private static void saveIDsToScreenNames(String fname) {

		if (DEBUG_OUT) {
			System.out.println(">>>> State of the HashMap before saving");

			// Display the state of the HashMap
			Iterator<String> it = idToNumberTweets.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				boolean value = idToNumberTweets.get(key);
				if (value)
					System.out.println(key + " -> " + value);
				else
					System.out.println(key);
			}
		}
		
		try {
			File file = new File(fname);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			// Write the associations in the file
			Iterator<String> it = idToNumberTweets.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				boolean value = idToNumberTweets.get(key);
				if (value)
					bw.write(key + ";" + value + ((it.hasNext() ? "\n" : "")));
				else
					bw.write(key + ";" + ((it.hasNext() ? "\n" : "")));
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}


	/**
	 * Function that loads the tweets of @user that are in a file.
	 
	@SuppressWarnings("finally")
	public String loadUserTweets(String name){
		String content = "";
		try {
			File file = new File(this.path + "/" + name + ".txt");
			
			if (file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				content = br.readLine();
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return content;
		}
	}	
	*/