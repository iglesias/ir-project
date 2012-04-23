/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PanelShowPageRank extends JPanel{
    	
	/**
	 * Constants.
	 */
    public final int width = 500;
    public final int height = 500;
	
	/**
	 * Declare components as attributes.
	 */
	private JLabel titleTweets;
	private JLabel titleRetweets;
	private static JEditorPane textTweets;
	private static JEditorPane textRetweets;
	private JButton buttonTweets;
	private JButton buttonRetweets;
	
	/**
	 * Constructor.
	 */
	public PanelShowPageRank(){
		
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		titleTweets = new JLabel("Pagerank <@User>:");
		titleRetweets = new JLabel("Pagerank retweets:");
		textTweets = new JEditorPane("text/html","");
		textRetweets = new JEditorPane("text/html","");
		textTweets.setEditable(false);
		textRetweets.setEditable(false);
		buttonTweets = new JButton("Recompute");
		buttonRetweets = new JButton("Recompute");
		
		JScrollPane scrollTweets = new JScrollPane(textTweets);
		JScrollPane scrollRetweets = new JScrollPane(textRetweets);
		
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(width,height));
		this.setMaximumSize(new Dimension(width,height));
		
		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------
		GridBagConstraints infoTitleTweets = new GridBagConstraints();
		infoTitleTweets.insets = new Insets(3,3,3,3);
		infoTitleTweets.gridx = 0;
		infoTitleTweets.gridy = 0;
		infoTitleTweets.weightx = 1.0;
		infoTitleTweets.weighty = 0.0;
		infoTitleTweets.anchor = GridBagConstraints.NORTHWEST;
		
		GridBagConstraints infoTitleRetweets = new GridBagConstraints();
		infoTitleRetweets.insets = new Insets(3,3,3,3);
		infoTitleRetweets.gridx = 1;
		infoTitleRetweets.gridy = 0;
		infoTitleRetweets.weightx = 1.0;
		infoTitleRetweets.weighty = 0.0;
		infoTitleRetweets.anchor = GridBagConstraints.NORTHWEST;
		
		GridBagConstraints infoScrollTweets = new GridBagConstraints();
		infoScrollTweets.gridx = 0;
		infoScrollTweets.gridy = 1;
		infoScrollTweets.weightx = 1.0;
		infoScrollTweets.weighty = 1.0;
		infoScrollTweets.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoScrollRetweets = new GridBagConstraints();
		infoScrollRetweets.gridx = 1;
		infoScrollRetweets.gridy = 1;
		infoScrollRetweets.weightx = 1.0;
		infoScrollRetweets.weighty = 1.0;
		infoScrollRetweets.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoButtonTweets = new GridBagConstraints();
		infoButtonTweets.gridx = 0;
		infoButtonTweets.gridy = 2;
		infoButtonTweets.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints infoButtonRetweets = new GridBagConstraints();
		infoButtonRetweets.gridx = 1;
		infoButtonRetweets.gridy = 2;
		infoButtonRetweets.fill = GridBagConstraints.CENTER;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		this.add(titleTweets,infoTitleTweets);
		this.add(titleRetweets,infoTitleRetweets);
		this.add(scrollTweets,infoScrollTweets);
		this.add(scrollRetweets,infoScrollRetweets);
		this.add(buttonTweets,infoButtonTweets);
		this.add(buttonRetweets,infoButtonRetweets);
		
	}
	
	/**
	 * Load the page rank.
	 */
	public static void loadPageRank(){
		
		// Set the pagerank in pagerank @user.
		String pathMentioned = PageRankGUI.rootPath + PageRankGUI.actUniverse + 
		                       PageRankGUI.pagerankMentionedPath + PageRankGUI.actMethod + ".txt";
		textTweets.setText(HandlerGUI.getContentRank(pathMentioned));
		
		// Set the pagerank in pagerank retweets.
		String pathRetweeted = PageRankGUI.rootPath + PageRankGUI.actUniverse + 
        					   PageRankGUI.pagerankRetweetedPath + PageRankGUI.actMethod + ".txt";
		textRetweets.setText(HandlerGUI.getContentRank(pathRetweeted));	
	}
	
}
