/**
 * @author Fernando José Iglesias García
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.OAuth;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;


public class SaveRetweetsUserIDs {

	/**
	 * Directory with users' statuses files
	 */

	private static final String USERS_PATH = 
			"/home/nando/Documents/Dropbox/" + 
			"DD2476 Search Engines and Information Retrieval Project/BerFer/" +
			"data/BowieState/TweetsList";
	
	/**
	 * Set to true to parse USERS_PATH and save the screen_name of the users
	 * together with id_str of the tweets that have been retweeted
	 */
	private static final boolean READ_TWEETS = false;
	
	/**
	 * Auxiliary structure to store the association between user screen names
	 * and the id_str of the tweet
	 */
	public static HashMap<String, String> tweetIdToScreenName = 
			new HashMap<String, String>();
	
	/**
	 * Auxiliary structure to store for each tweet the list of user IDs that
	 * has retweeted that tweet
	 */
	public static HashMap<String, LinkedList<String>> tweetIdToRetweetersIds =
			new HashMap<String, LinkedList<String>>();
	
	/**
	 * @param args path to tweets&retweets file
	 */
	public static void main(String[] args) {
		
		if ( READ_TWEETS ) {
			// Create the tweets&retweets file and exit
			readTweets();
			return;
		}
		
		if ( args.length < 1 ) {
			System.err.println("usage: SaveRetweetUserIDs " + 
					"<tweets&retweetsFile>");
			return;
		}

		File tweetsRetweetsFile = new File(args[0]);
		
		// Read the available information from the file and populate the 
		// HashMaps
		loadTweetsRetweetsFile(tweetsRetweetsFile);
		
		// Authenticate with OAuth because the petition we are about to do with
		// the API requires it
		OAuth.authenticate();
		
		// Solve as many retweeters ids as possible
		sendRequests();
		
		// Save file
		saveTweetsRetweetsFile(tweetsRetweetsFile);
	}


	private static void readTweets() {
		
		// Directory where the user files are
		File dir = new File(USERS_PATH);
		
		// Output file, name with a unique timestamp
		
		Calendar cal = Calendar.getInstance();
		
		File outFile = new File("tweets&retweets_" + 
							dir.getAbsolutePath().replace('/', '_') + "_" + 
							cal.getTimeInMillis() + ".txt");
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
		} catch (IOException e) {
			System.out.println("Impossible to create the output file");
			e.printStackTrace();
		}
		
		
		if ( dir.exists() && dir.isDirectory() ) {
			
			File[] tweets = dir.listFiles();
			
			for (int i = 0; i < tweets.length; i++) {
				
				// Get the user name (file without the trailing .json)
				String fileName = tweets[i].getName();
				String userName = fileName.substring(0, fileName.length()-5);
				
				try {
					
					// Read list of tweets in JSON format
					
					BufferedReader br = new BufferedReader(
							new FileReader(tweets[i]));
					// .json file, just one line
					JSONArray jsonArray = new JSONArray(br.readLine());
					br.close();
					
					for (int j = 0; j < jsonArray.length(); j++) {
						
						JSONObject tweet = (JSONObject) jsonArray.get(j);
						
						// Save information only if the tweet has been 
						// retweeted
						
						int retweetCount = Integer.parseInt(
								tweet.get("retweet_count").toString());
						
						if ( retweetCount > 0 ) {
							String tweetId = ( (JSONObject) jsonArray.get(j) ).
												get("id_str").toString();
							bw.write(userName + ";" + tweetId + ";\n");
						}
						
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		} else {
			System.out.println("File " + USERS_PATH + " not found or not a " + 
									"directory");
		}
		
		try {
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not close the output file");
			e.printStackTrace();
		}
		
	}


	private static void loadTweetsRetweetsFile(File tweetsRetweetsFile) {

		if ( tweetsRetweetsFile.exists() ) {
			
			int nTweetsResolved = 0, nTweetsTotal = 0;
			
			try {
				
				BufferedReader br = new BufferedReader(
						new FileReader(tweetsRetweetsFile));
				String line;
				while ( (line = br.readLine()) != null ) {
					
					nTweetsTotal++;
					String[] tokens = line.split(";");
					
					tweetIdToScreenName.put(tokens[1], tokens[0]);
					
					LinkedList<String> list = new LinkedList<String>();
					if (tokens.length == 3) {
						// The id_str on this line had already been resolved
						nTweetsResolved++;
						
						String[] retweetersIds = tokens[2].split(",");
						for (int i = 0; i < retweetersIds.length; i++) {
							list.add(retweetersIds[i]);
						}
							
					}
					tweetIdToRetweetersIds.put(tokens[1], list);
				}
				
				br.close();
				
				System.out.println("tweet&retweets file loaded, " + 
						nTweetsResolved + " tweets solved out of " + 
						nTweetsTotal);
				System.out.println();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		} else {
			System.err.println("File " + tweetsRetweetsFile.getName() + " not" +
					"found");
			System.exit(1);
		}
		
	}


	private static void sendRequests() {
		
		Iterator<String> it = tweetIdToRetweetersIds.keySet().iterator();
		int nRequest = 0, nTweetsResolved = 0;
		while ( it.hasNext() ) {
			
			String key = it.next();
			LinkedList<String> list = tweetIdToRetweetersIds.get(key);
			
			if ( list.size() == 0 ) {
				
				nRequest++;
				System.out.println(">>>> Request #" + nRequest + " -> " + key);
				// Get the user_id(s) of the people that has retweeted this 
				// tweet
				
				OAuthRequest request = new OAuthRequest(Verb.GET, 
						buildRetweetedByURL(key));
				OAuth.signRequest(request);
				Response response = request.send();
				
				String userIdsStr = response.getBody();
				System.out.println(userIdsStr);
				
				JSONObject respJSONObj = null;	
				// This is a bit ugly but works fine
				if ( userIdsStr.charAt(0) == '{' ) {
					// We deal with a JSONObject, probably an error occurred
					try {
						respJSONObj = new JSONObject(userIdsStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				if ( respJSONObj != null && respJSONObj.has("error") ) {
					System.out.println("The request produced an error..." +
							" no more requests will be sent. " + 
							nTweetsResolved + " new tweets associated.");
					return;
				}
				
				// Remove '[' and ']' from the beginning and the end
				userIdsStr = userIdsStr.substring(1, userIdsStr.length()-1);
				
				String[] userIds = userIdsStr.split(",");
				
				if ( userIds.length > 0 ) {
					nTweetsResolved++;
				}
				
				for (int i = 0; i < userIds.length; i++) {
					list.add(userIds[i]);
				}
				System.out.println();
				
			}
			
		}
		
	}


	private static String buildRetweetedByURL(String tweetIdStr) {
		return "http://api.twitter.com/1/statuses/" +
				tweetIdStr + "/retweeted_by/ids.json";
	}


	private static void saveTweetsRetweetsFile(File tweetsRetweetsFile) {
		
		try {
			
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(tweetsRetweetsFile));
			
			// Dump the data into the file
			Iterator<String> it = tweetIdToScreenName.keySet().iterator();
			while (it.hasNext()) {
				String tweetId = it.next();
				String screenName = tweetIdToScreenName.get(tweetId);
				bw.write(screenName + ";" + tweetId + ";");
				
				LinkedList<String> retwettersIds = 
						tweetIdToRetweetersIds.get(tweetId);
				for (int i = 0; i < retwettersIds.size(); i++) {
					// Write the id user that retweeted followed by a comma
					// (the last one without comma)
					bw.write(retwettersIds.get(i) + 
							((i < retwettersIds.size()-1) ? "," : ""));
				}
				
				bw.write((it.hasNext() ? "\n" : ""));
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
