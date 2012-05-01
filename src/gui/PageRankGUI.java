/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 * Date : 20/04/2012                                                   *                          
 * Description : Launcher of the GUI interface.                        *
 ***********************************************************************/
package gui;

public class PageRankGUI {

	/**
	 * Static variables.
	 */
	public static String rootPath = "./data/";    
	public static String pagerankMentionedPath = "/pagerank/mentioned/";
	public static String pagerankRetweetedPath = "/pagerank/retweeted/";
	public static String blackListPath = "./data/blacklist.txt";
	public static String iconPath = "./images/twitter_bird.png";
	
	public static String actUniverse = null;
	public static String actMethod = null;
	public static String actOption = null;
	public static String actRetrieved = "0";
	
	public static String maxDocumentsRetrieved = "20";
	
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
