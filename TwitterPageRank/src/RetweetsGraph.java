import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author Fernando José Iglesias García, April 2012
 *
 */

public class RetweetsGraph {

	/**
	 * Auxiliary structure to store for each user that has retweeted the list of
	 * users he/she has retweeted together with the number of times he/she has
	 * retweeted each user
	 */
	public static HashMap<String, HashMap<String, Integer>> retweeterToUserIds 
			= new HashMap<String, HashMap<String, Integer>>();
	
	/**
	 * Auxiliary structure used to translate from the IDs of the retwetters
	 * to their screen names
	 */
	public static HashMap<String, String> userIDToScreeName 
			= new HashMap<String, String>(); 
	
	private final static boolean DEBUG_OUT = false;
	
	/**
	 * Read a type of file tweetsRetweets and writes a new file graphRetweets 
	 * with the correct format to be used by the PageRank algorithm
	 */
	public static void main(String[] args) {
		
		if ( args.length < 2 ) {
			System.err.println("usage: RetweetsGraph <tweetsRetweetsFile>" + 
					" <userIDsFile>");
			System.exit(1);
		}
		
		File tweetsRetweetsFile = new File(args[0]);
		File userIDsFile = new File(args[1]);
		
		loadUserIDsFile(userIDsFile);
				
		loadTweetsRetweetsFile(tweetsRetweetsFile);

		writeGraphRetweets(tweetsRetweetsFile);
		
	}
	
	/**
	 * Load into main memory the mapping from user IDs to screen names
	 * 
	 * @param userIDsFile
	 */
	private static void loadUserIDsFile(File userIDsFile) {

		if ( userIDsFile.exists() ) {
			
			try {
				
				BufferedReader br = new BufferedReader(
						new FileReader(userIDsFile));
				String line;
				while ( (line = br.readLine()) != null ) {
										
					// Each line of the file has the format: 
					// user_id;screen_name 
					// E.g.: 1234;pelleMelle
					
					String[] tokens = line.split(";");
					userIDToScreeName.put(tokens[0], tokens[1]);
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.err.println("File " + userIDsFile.getName() + " not" +
					" found");
			System.exit(1);
		}	
		
	}

	
	/**
	 * Load into the data structure retweeterToUserIds the tweetsRetweetsFile
	 * 
	 * @param tweetsRetweetsFile
	 */
	private static void loadTweetsRetweetsFile(File tweetsRetweetsFile) {
		
		if ( tweetsRetweetsFile.exists() ) {
			
			try {
				
				BufferedReader br = new BufferedReader(
						new FileReader(tweetsRetweetsFile));
				String line;
				while ( (line = br.readLine()) != null ) {
										
					// Each line of the file has the format: 
					// screen_name;tweet_id;user_ids of the people that 
					// retweeted tweet_id written by screen_Name separated by 
					// commas. E.g.: A;12345678;11,22,33
					
					String[] comps = line.split(";");
					String[] retweetersIDs = comps[2].split(",");
					String userRetweeted = comps[0];
					
					for ( int i = 0 ; i < retweetersIDs.length ; ++i ) {
						
						String userRetweeter = 
								userIDToScreeName.get(retweetersIDs[i]);
						
						if ( userRetweeter == null ) {
							// No idea why some user IDs cannot be solved
							System.err.println("The user ID " + retweetersIDs[i]
									+ " could not be mapped to screen_name!");
							continue;
						}
						
						HashMap<String, Integer> retweetedToFreq = 
								retweeterToUserIds.get(userRetweeter);
						
						if ( retweetedToFreq != null ) {
							// retweetersIds[i] has retweeted previously
							Integer oldFreq = 
									retweetedToFreq.get(userRetweeted);
							
							if ( oldFreq != null ) {
								// retweetersIds[i] has retweeted userRetweeted
								// previously
								retweetedToFreq.put(userRetweeted, oldFreq + 1);
							} else {
								// first retweet from retweetersIds[i] to
								// userRetweeted
								retweetedToFreq.put(userRetweeted, 1);
							}
							
						} else {
							// first retweet found of retweetersIds[i]
							HashMap<String, Integer> newValue = 
									new HashMap<String, Integer>();
							newValue.put(userRetweeted, 1);
							
							retweeterToUserIds.put(userRetweeter, newValue);
							
						}
						
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.err.println("File " + tweetsRetweetsFile.getName() + " not" +
					" found");
			System.exit(1);
		}
		
	}

	/**
	 * Dump to disk the data structure retweeterToUserIds
	 * 
	 * @param tweetsRetweetsFile used here to get the name
	 */
	private static void writeGraphRetweets(File tweetsRetweetsFile) {
		
		int count = 0;
		
		try {
			
			// Assume that tweetsRetweetsFile already has .txt as extension
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"GraphRetweets_" + tweetsRetweetsFile.getName()));
			
			// Dump the data into the file
			Iterator<String> it = retweeterToUserIds.keySet().iterator();
			while ( it.hasNext() ) {
				
				String retweeter = it.next();
				bw.write(retweeter + ";");
				
				if ( DEBUG_OUT ) {
					System.out.print(++count + ":" + retweeter + " -> ");
				}
				
				HashMap<String, Integer> retweetedToFreq = 
						retweeterToUserIds.get(retweeter);
				Iterator<String> itt = retweetedToFreq.keySet().iterator();
				while ( itt.hasNext() ) {
					String retweeted = itt.next();
					Integer freq = retweetedToFreq.get(retweeted);
					
					if ( DEBUG_OUT ) {
						System.out.print(retweeted + ":" + freq + ",");
					}
					
					bw.write(retweeted + "(" + freq + ")" + 
							(( itt.hasNext() )? "," : ""));
					
				}

				if ( DEBUG_OUT ) {
					System.out.println();
				}
				
				bw.write( ((it.hasNext())? "\n" : "") );
				
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
