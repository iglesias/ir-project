/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import solr.HandlerSolr;


@SuppressWarnings("serial")
public class PanelTabSearch extends JPanel{

	/**
	 * Create Elements as attributes.
	 */
	private JLabel icon;
	private JTextField searchBox;
	private JButton searchButton;
	private JCheckBox checkAuthor;
	private JCheckBox checkHashtag;
	private JCheckBox checkDescription;
	private JCheckBox checkText;
	private JCheckBox checkDate;
	private JEditorPane retrievedTweets;
	
	public PanelTabSearch(){
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		icon = new JLabel(new ImageIcon(PageRankGUI.iconPath));
		searchBox = new JTextField("Query...");
		searchButton = new JButton("Search");
		checkAuthor = new JCheckBox("Author");
		checkHashtag = new JCheckBox("Hashtag");
		checkDescription = new JCheckBox("Description");
		checkText = new JCheckBox("Text");
		checkDate = new JCheckBox("Date");
		retrievedTweets = new JEditorPane("text/html","");
		retrievedTweets.setEditable(false);
		JScrollPane scrollRetrievedTweets = new JScrollPane(retrievedTweets);
		
		
		//retrievedTweets.setBorder(javax.swing.BorderFactory.createTitledBorder("Retrieved Tweets"));
		
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		
		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------
		GridBagConstraints infoIcon = new GridBagConstraints();
		infoIcon.insets = new Insets(3,3,3,3);
		infoIcon.gridx = 0;
		infoIcon.gridy = 0;
		infoIcon.weightx = 1.0;
		infoIcon.weighty = 1.0;
		infoIcon.gridwidth = 4;
		
		GridBagConstraints infoSearchBox = new GridBagConstraints();
		infoSearchBox.insets = new Insets(20,50,20,0);
		infoSearchBox.gridx = 0;
		infoSearchBox.gridy = 1;
		infoSearchBox.weightx = 1.0;
		infoSearchBox.weighty = 0.0;
		infoSearchBox.fill = GridBagConstraints.BOTH;
		infoSearchBox.gridwidth = 4;
		
		GridBagConstraints infoSearchButton = new GridBagConstraints();
		infoSearchButton.insets = new Insets(20,0,20,50);
		infoSearchButton.gridx = 4;
		infoSearchButton.gridy = 1;
		infoSearchButton.weightx = 0.0;
		infoSearchButton.weighty = 0.0;
		infoSearchButton.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints infoCheckAuthor = new GridBagConstraints();
		infoCheckAuthor.insets = new Insets(3,3,3,3);
		infoCheckAuthor.gridx = 0;
		infoCheckAuthor.gridy = 2;
		infoCheckAuthor.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoCheckHashtag = new GridBagConstraints();
		infoCheckHashtag.insets = new Insets(3,3,3,3);
		infoCheckHashtag.gridx = 1;
		infoCheckHashtag.gridy = 2;
		infoCheckHashtag.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoCheckDescription = new GridBagConstraints();
		infoCheckDescription.insets = new Insets(3,3,3,3);
		infoCheckDescription.gridx = 2;
		infoCheckDescription.gridy = 2;
		
		GridBagConstraints infoCheckText = new GridBagConstraints();
		infoCheckText.insets = new Insets(3,3,3,3);
		infoCheckText.gridx = 3;
		infoCheckText.gridy = 2;
		
		GridBagConstraints infoCheckDate = new GridBagConstraints();
		infoCheckDate.insets = new Insets(3,3,3,3);
		infoCheckDate.gridx = 4;
		infoCheckDate.gridy = 2;
		
		GridBagConstraints infoScroll = new GridBagConstraints();
		infoScroll.gridx = 0;
		infoScroll.gridy = 3;
		infoScroll.weightx = 1.0;
		infoScroll.weighty = 1.0;
		infoScroll.fill = GridBagConstraints.BOTH;
		infoScroll.gridwidth = 5;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		//this.add(icon,infoIcon);
		this.add(searchBox,infoSearchBox);
		this.add(searchButton,infoSearchButton);
		//this.add(checkAuthor,infoCheckAuthor);
		//this.add(checkHashtag,infoCheckHashtag);
		//this.add(checkDescription,infoCheckDescription);
		//this.add(checkText,infoCheckText);
		//this.add(checkDate,infoCheckDate);
		this.add(scrollRetrievedTweets,infoScroll);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------	
		this.searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Query : " + searchBox.getText());
				loadRetrievedTweets();
			}
		});
	}
	
	/**
	 * Method that loads all the retrieved tweets given a query.
	 */
	public void loadRetrievedTweets(){
		
		// Get the query.(searchBox.getText()
		String query = searchBox.getText();
		
		// Get the retrieved tweets.
		String content = HandlerSolr.getRetrievedTweets(query.split(" ")[0], query.split(" ")[1]);
		
		// Set the content in the JEditorPane.
		retrievedTweets.setText(content);
	}
	
	
}

