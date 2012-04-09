import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import org.json.JSONObject;

/**
 * @author Fernando José Iglesias García
 */

public class SaveFollowersIDs {

	/**
	 * Twitter's screen_name to retrieve followers from
	 */
	public static final String FOLLOWING = "BowieState";
	
	/**
	 * Show some debugging information
	 */
	public static final boolean DEBUG_OUT = true;
	
	/**
	 * Gets all the IDs of the followers of the twitter identity FOLLOWING 
	 * and stores them in a file
	 */
	public static void main(String[] args) {

	try {

		// Get the information about the followers in .json format
		URL url = new URL("http://api.twitter.com/1/followers/" +
				"ids.json?screen_name=" + FOLLOWING);
		BufferedReader urlInput = new BufferedReader(
				new InputStreamReader(url.openStream()));

		// Create JSON object, all the required information is in one 
		// line
		JSONObject jsonObject = new JSONObject(urlInput.readLine());
		urlInput.close();

		// Parse the followers' IDs
		String rawFollowerIDs = jsonObject.get("ids").toString();
		String[] followerIDs = 
			rawFollowerIDs.substring(1, rawFollowerIDs.length()-1)
				      .split(",");
		
		if (DEBUG_OUT) {
			System.out.println("Found " + followerIDs.length + 
					" followers");
			for (int i = 0; i < followerIDs.length; i++)
			System.out.println(">>>> " + followerIDs[i]);
		}

		// Store the followers' IDs in a file, using a unique timestamp 
		// for the file's name

		Calendar calendar = Calendar.getInstance();

		File file = new File("followers_" + FOLLOWING + "_" + 
				calendar.getTimeInMillis());
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < followerIDs.length; i++) {
			bw.write(followerIDs[i] + ";" +
				((i != followerIDs.length-1) ? "\n" : ""));
		}
		bw.close();

	} catch (Exception e) {
		e.printStackTrace();
	}	
		
}

}
