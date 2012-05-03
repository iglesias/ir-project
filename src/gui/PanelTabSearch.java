/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import solr.HandlerSolr;


@SuppressWarnings("serial")
public class PanelTabSearch extends JPanel{

	/**
	 * Create Elements as attributes.
	 */
	private JLabel icon;
	private JTextField searchBox;
	private JButton searchButton;
	private JSlider sliderBar;
	private JLabel nRetrieved;
	private JEditorPane retrievedTweets;
	private JScrollPane scrollRetrievedTweets;
	
	private JPanel checkPanel;
	private JCheckBox checkAuthor;
	private JCheckBox checkHashtag;
	private JCheckBox checkDescription;
	private JCheckBox checkText;
	private JCheckBox checkDate;
	
	public PanelTabSearch(){
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		icon = new JLabel(new ImageIcon(PageRankGUI.iconPath));
		searchBox = new JTextField("Query...");
		searchButton = new JButton("Search");
		sliderBar = new JSlider(0,100,50);
		nRetrieved = new JLabel("Retrieved:" + PageRankGUI.actRetrieved);
		retrievedTweets = new JEditorPane("text/html","");
		retrievedTweets.setEditable(false);
		scrollRetrievedTweets = new JScrollPane(retrievedTweets);
		
		// ----------------------------------------------------------------------------
		//                            SOME CONFIGURATIONS
		// ----------------------------------------------------------------------------
		//this.icon.setSize(new Dimension(100,400));
		sliderBar.setSize(200,20);
		sliderBar.setMinimumSize(new Dimension(200,20));

		checkPanel = new JPanel();
		checkAuthor = new JCheckBox("Author");
		checkHashtag = new JCheckBox("Hashtag");
		checkDescription = new JCheckBox("Description");
		checkText = new JCheckBox("Text");
		checkDate = new JCheckBox("Date");
		checkPanel.add(checkAuthor);
		checkPanel.add(checkText);
		//checkPanel.add(checkDescription);
		//checkPanel.add(checkHashtag);
		//checkPanel.add(checkDate);
				
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
		
		GridBagConstraints infoSearchBox = new GridBagConstraints();
		infoSearchBox.insets = new Insets(20,50,3,0);
		infoSearchBox.gridx = 0;
		infoSearchBox.gridy = 1;
		infoSearchBox.weightx = 1.0;
		infoSearchBox.weighty = 0.0;
		infoSearchBox.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoSearchButton = new GridBagConstraints();
		infoSearchButton.insets = new Insets(20,0,3,50);
		infoSearchButton.gridx = 1;
		infoSearchButton.gridy = 1;
		infoSearchButton.weightx = 0.0;
		infoSearchButton.weighty = 0.0;
		infoSearchButton.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints infoCheckPanel = new GridBagConstraints();
		infoCheckPanel.gridx = 0;
		infoCheckPanel.gridy = 2;
		infoCheckPanel.weightx = 0.0;
		infoCheckPanel.weighty = 0.0;
		infoCheckPanel.gridwidth = 2;
		infoCheckPanel.anchor = GridBagConstraints.CENTER;
		infoCheckPanel.fill = GridBagConstraints.CENTER;
		
		GridBagConstraints infonRetrieved = new GridBagConstraints();
		infonRetrieved.insets = new Insets(0,0,2,0);
		infonRetrieved.gridx = 0;
		infonRetrieved.gridy = 3;
		infonRetrieved.weightx = 0.0;
		infonRetrieved.weighty = 0.0;
		infonRetrieved.anchor = GridBagConstraints.NORTHWEST;
		
		GridBagConstraints infoScroll = new GridBagConstraints();
		infoScroll.gridx = 0;
		infoScroll.gridy = 4;
		infoScroll.weightx = 1.0;
		infoScroll.weighty = 1.0;
		infoScroll.fill = GridBagConstraints.BOTH;
		infoScroll.gridwidth = 2;
		
		GridBagConstraints infoSliderBar = new GridBagConstraints();
		infoSliderBar.insets = new Insets(3,3,3,3);
		infoSliderBar.gridx = 0;
		infoSliderBar.gridy = 5;
		infoSliderBar.weightx = 0.0;
		infoSliderBar.weighty = 0.0;
		infoSliderBar.fill = GridBagConstraints.CENTER;
		infoScroll.gridwidth = 3;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		//this.add(icon,infoIcon);
		this.add(searchBox,infoSearchBox);
		this.add(searchButton,infoSearchButton);
		this.add(checkPanel,infoCheckPanel);
		this.add(nRetrieved,infonRetrieved);
		this.add(scrollRetrievedTweets,infoScroll);
		this.add(sliderBar,infoSliderBar);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------	
		this.searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Query : " + searchBox.getText());
				loadRetrievedTweets();
			}
		});
		
		this.searchBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Query : " + searchBox.getText());
				loadRetrievedTweets();
			}
		});
		
		this.sliderBar.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider) e.getSource()).getValue();
				System.out.println(value);
				reloadRetrievedTweets(value);
			}
		});
		
	}
	
	/**
	 * Reload retweets.
	 * @param value
	 */
	 
	@SuppressWarnings("unchecked")
	public void reloadRetrievedTweets(int value){
		
		// Set the score.
		for (int i=0; i<PageRankGUI.actTweetsRetrieved.size(); i++)
		    PageRankGUI.actTweetsRetrieved.get(i).computeFinalScore(value);
		
		// Reoorder.
		Collections.sort(PageRankGUI.actTweetsRetrieved);
		
		// Convert to html with the right value.
		String content = HandlerSolr.convertTweetsToHTML();
		
		// Set the content in the JEditorPane.
		retrievedTweets.setText(content);
	}
	
	/**
	 * Method that loads all the retrieved tweets given a query.
	 */
	public void loadRetrievedTweets(){
		
		// Get the query.
		String query = getQuerySelected();
		String value = getValueSelected();
		System.out.println(query + "-" + value);
		
		// Save the tweets that matches with the query.
		HandlerSolr.saveRetrievedTweets(query, value);
		
		// Set the score.
		for (int i=0; i<PageRankGUI.actTweetsRetrieved.size(); i++) {
			Tweet t = PageRankGUI.actTweetsRetrieved.get(i);
			t.setFinalScore(t.getTFIDFScore());
		}
		
		String content = HandlerSolr.convertTweetsToHTML();
		
		// Set the value of retrieved tweets.
		nRetrieved.setText("Retrieved:" + PageRankGUI.actRetrieved);
		
		// Set the content in the JEditorPane.
		retrievedTweets.setText(content);
	}
	
	/**
	 * Method to get the query that corresponds with the checkboxs. 
	 */
	public String getQuerySelected(){
		String query = "";
		if (checkText.isSelected()) query += "text";
		if (checkAuthor.isSelected()) query += "+screen_name";
		if (query.equals("")) query = "text+screen_name";
		return query;
	}
	
	/**
	 * Method to get the value that corresponds to the searchBox field.
	 */
	public String getValueSelected(){
		String value = searchBox.getText().replace(" ","+");
		if (value.equals("")) value = "*";
		return value;
	}	
	
}

