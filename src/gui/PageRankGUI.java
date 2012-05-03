/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 * Date : 20/04/2012                                                   *                          
 * Description : Launcher of the GUI interface.                        *
 ***********************************************************************/
package gui;

import java.util.ArrayList;
import java.util.HashMap;

public class PageRankGUI {

	/**
	 * Static variables.
	 */
	// Limit.
	public static String maxRetrieved = "1000";
	
	// Paths.
	public static String rootPath = "./data/";    
	public static String pagerankMentionedPath = "/pagerank/mentioned/";
	public static String pagerankRetweetedPath = "/pagerank/retweeted/";
	public static String blackListPath = "./data/blacklist.txt";
	public static String iconPath = "./images/twitter_bird.png";
	
	// Options selected by user.
	public static String actUniverse = null;
	public static String actMethod = null;
	public static String actOption = null;
	public static String actRetrieved = "0";
	
	// Options to choose for the admin.
	public static String maxDocumentsRetrieved = "10";
	public static String maxUsersRetrieved = "20";
	
	// Storage of the actuals pageranks.
	public static HashMap<String,Double> pagerankScoresMention = new HashMap<String,Double>();
	public static HashMap<String,Double> pagerankScoresRetweet = new HashMap<String,Double>();
	
	// Storage of the actuals retrieved tweets for a given query.
	public static ArrayList<Tweet> actTweetsRetrieved = new ArrayList<Tweet>();
	
	/**
	 * Attributes.
	 */
	MainFrame mainFrame;	
	
	/**
	 * Constructor
	 */
	public PageRankGUI(){
		mainFrame = new MainFrame();
	}
	
	/**
	 * Main method.
	 */
	public static void main(String[] args){
		new PageRankGUI();
	}
	
}
