import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Fernando José Iglesias García
 *
 */

public class CountRetweets {

	/**
	 * TODO explanation
	 */
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.err.println("usage: CountRetweets " + "<tweetsDir>");
		}
		
		// Read all the files of the directory, assume their name is 
		// screen_name.txt
			
		File dir = new File(args[0]);
		
		if (dir.exists() && dir.isDirectory()) {
			
			File[] tweets = dir.listFiles();
			
			for (int i = 0; i < tweets.length; ++i) {
				
				// Get the user name (file name without the trailing .json)
				String fileName = tweets[i].getName();
				String userName = fileName.substring(0, 
						fileName.length()-5);
				
				// Get the total number of retweets of all his/her statuses
				int nRetweets = getNumberRetweets(tweets[i]);
				
				System.out.println(">>>> The statuses of user " + pad(userName)
						+ " have been retweeted " + nRetweets + "\ttimes");
				
			}
			
		} else {
			System.err.println("File " + args[0] + "not found or not a " + 
					"directory");
		}
		
	}

	private static String pad(String userName) {
		while (userName.length() < 20)
			userName += " ";
		return userName;
	}

	/**
	 *  Given a json file with the statuses of a user, count the total number of 
	 *  retweets of all of them together
	 */
	private static int getNumberRetweets(File file) {
		
		// Total number of retweets
		int count = 0;
		
		try {
			
			// Read list of tweets in JSON format
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			// .json file, just one line
			JSONArray jsonArray = new JSONArray(br.readLine());
			br.close();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				count += Integer.parseInt(
							((JSONObject) jsonArray.get(i) ).
							get("retweet_count").
							toString());   
				
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return count;
		
	}

}