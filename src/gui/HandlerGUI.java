/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import solr.HandlerSolr;

public class HandlerGUI {
	
	/**
	 * Maximun number of users ranked we want to show.
	 */
	public final static int maxUsers = Integer.parseInt(PageRankGUI.maxUsersRetrieved);
	
	/**
	 * Method to get all the document that are in a directory.
	 * @param path
	 * @return File[]
	 */
	public static File[] getElementsIn(String path){
		File dir = new File(path);
		if (dir.exists())
			return dir.listFiles();
		return null;
	}
	
	/**
	 * Method to get all the users in the file "BlackList".
	 */
	@SuppressWarnings("finally")
	public static ArrayList<String> getContentBlackList(String path){
		ArrayList<String> blackList = new ArrayList<String>();
		try {
			File file = new File(path);
			
			if (file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line=br.readLine())!=null) {
					blackList.add(line);
				}
				br.close();
			} else {
				blackList.add("The file " + path + " doesnt exist.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return blackList;
		}	
		
	}
	
	/**
	 * Method to get all the content of one "PageRank" file, and save it in the
	 * corresponding hasmap<username,pagerankScore>.
	 * @param file : name of the file.
	 * @param mentioned : true if mentioned, false if retweeted.
	 * @return String
	 */
	@SuppressWarnings("finally")
	public static String getContentRankHTML(String path, boolean mentioned){
		String content = "";
		try {
			File file = new File(path);
			
			if (file.exists()){
				
				// Remove the previuous scores.
				if (mentioned)  PageRankGUI.pagerankScoresMention.clear();
				else PageRankGUI.pagerankScoresRetweet.clear();
				
				// Header.
				content += "<html><head></head><body><table border=1>";
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				int cont = 1;
				String line;			
				while (((line=br.readLine())!=null)) {
					int index = line.indexOf( ";" );           // Position of ;.
		    		String name = line.substring( 0, index );  // String from 0 to ;.
		    		String score = line.substring(index+1,line.length());
		    		
	    			// Create the html.
		    		if (cont<=maxUsers) {
		    			String photo_url = HandlerSolr.getParameter("profile_url",name);

						content += "<tr>";
						content += "<td><img src='" + photo_url + "' width=50 height=50></img></td>";
						content += "<td align=left valign=top>";
						content += "<B>" + name + "</B><br>";
						content += "<I>" + "descripcion" + "</I></td>";
						content += "<td valign=top><font size=2>" + score + "</font></td>";
						content += "</tr>";	
						
						cont++;
		    		}
		    		
                    // Add to the pagerank scores.
		    		if (mentioned)
		    			PageRankGUI.pagerankScoresMention.put(name, Double.parseDouble(score));
		    		else
		    			PageRankGUI.pagerankScoresRetweet.put(name, Double.parseDouble(score));
				}
				content += "</table></body></html>";
				br.close();		
				
			} else {
				content = "The file " + path + " doesnt exist.";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return content;
		}	
	}
	
	/**
	 * Method that set int the actualTweetsRetrieved the values of the corresponding
	 * scores, we will cal this method after caling getContentRankHTML because in 
	 * this function we set up the pagerank hashmaps.
	 * @param args
	 */
	public static void setPagerankScoresToTweets(){
		for (int i=0; i<PageRankGUI.actTweetsRetrieved.size(); i++){
			Tweet tweet = PageRankGUI.actTweetsRetrieved.get(i);
			tweet.setPRMentionedScore(PageRankGUI.pagerankScoresMention.get(tweet.getAuthor()));
		}
	}
	
	public static void main(String[] args){
	}
	
}
