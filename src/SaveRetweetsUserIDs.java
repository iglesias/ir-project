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
import java.util.HashSet;
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
	
	private enum EMode {
		MReadTweets, MResolveTweets, MStoreRetweeters
	}
	
	/**
	 * @param args path to tweetsRetweets file
	 */
	public static void main(String[] args) {
	
		EMode mode = EMode.MStoreRetweeters;
		
		if ( args.length < 1 ) {
			System.err.println("usage: SaveRetweetUserIDs " + 
					"<tweets&retweetsFile>");
			return;
		}
		
		File tweetsRetweetsFile = new File(args[0]);
		
		switch (mode) {
		
			case MReadTweets:
				// Create the tweetsRetweets file and exit
				readTweets();
				break;
				
			case MResolveTweets:
				// Send as many requests as possible, update the file and exit
				
				// Read the available information from the file and populate the
				// HashMaps
				loadTweetsRetweetsFile(tweetsRetweetsFile);
				
				// Authenticate with OAuth because the petition we are about to
				// do with the API requires it
				OAuth.authenticate();
				
				// Solve as many retweeters ids as possible
				sendRequests();
				
				// Save file
				saveTweetsRetweetsFile(tweetsRetweetsFile);
				
				break;

			case MStoreRetweeters:
				// Save in a file retweetersIDs the user IDs of the people that
				// has retweeted something
		
				writeRetweeters(tweetsRetweetsFile);
				break;
		}
		
		
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
				
				System.out.println("tweetRetweets file loaded, " + 
						nTweetsResolved + " tweets solved out of " + 
						nTweetsTotal);
				System.out.println();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		} else {
			System.err.println("File " + tweetsRetweetsFile.getName() + " not" +
					" found");
			System.exit(1);
		}
		
	}


	private static void sendRequests() {
		
		HashSet<String> trapTweetsIds = new HashSet<String>();
		
		Iterator<String> it = tweetIdToRetweetersIds.keySet().iterator();
		int nRequest = 0, nTweetsResolved = 0;
		while ( it.hasNext() ) {
			
			String tweetId = it.next();
			LinkedList<String> list = tweetIdToRetweetersIds.get(tweetId);
			
			if ( list.size() == 0 ) {
				
				nRequest++;
				System.out.println(">>>> Request #" + nRequest + " -> " + 
									tweetId);
				// Get the user_id(s) of the people that has retweeted this 
				// tweet
				
				OAuthRequest request = new OAuthRequest(Verb.GET, 
						buildRetweetedByURL(tweetId));
				OAuth.signRequest(request);
				Response response = request.send();
				
				String userIdsStr = response.getBody();
				System.out.println(userIdsStr);
				
				// There are some "trap" tweets, the parameter retweet-count 
				// indicates that they have been retweeted but no user_id is
				// returned with this request. When that happens, we just delete
				// that tweet entry
				if ( userIdsStr.equals("[]") ) {
					System.out.println("Found trap tweet!");
					trapTweetsIds.add(tweetId);
					System.out.println();
					continue;
				}
				
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
					
					String errorCode = null;
					try {
						errorCode = respJSONObj.getString("error")
														.toString();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					if ( errorCode.equals("Not found") ) {
						
						// Tweet that might have been removed
						System.out.println("Found removed tweet!");
						trapTweetsIds.add(tweetId);
						System.out.println();
						continue;
						
					} else {
						// Assume that another error is caused by the rate limit
						System.out.println("The request produced an error..." +
								" no more requests will be sent. " + 
								nTweetsResolved + " new tweets associated.");
						break;	
					}
						
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
		
		// Remove "trap" tweets to avoid wasting requests in the future
		it = trapTweetsIds.iterator();
		while ( it.hasNext() ) {
			
			String trapTweetId = it.next();
			tweetIdToScreenName.remove(trapTweetId);
			tweetIdToRetweetersIds.remove(trapTweetId);
			
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


	private static void writeRetweeters(File tweetsRetweetsFile) {
		
		// Load the tweetsRetweetsFile
		loadTweetsRetweetsFile(tweetsRetweetsFile);
		
		// Store in a set the IDs of all the retweeters
		HashSet<String> retweetersIDs = new HashSet<String>();
		
		Iterator<LinkedList<String>> it = 
				tweetIdToRetweetersIds.values().iterator();
		
		
		while ( it.hasNext() ) {
			
			LinkedList<String> retweeters = it.next();
			
			for (int i = 0; i < retweeters.size(); i++) {
				retweetersIDs.add( retweeters.get(i) );
			}
		
		}
		
		// Dump to a file the retweeters IDs
		try {
			
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("retweetersIDs.txt"));
			
			Iterator<String> itt = retweetersIDs.iterator();
			while (itt.hasNext()) {
				String retweeterID = itt.next();
				
				bw.write(retweeterID + ";");
				bw.write((itt.hasNext() ? "\n" : ""));
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
