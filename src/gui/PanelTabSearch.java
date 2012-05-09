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
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;

import solr.HandlerSolr;


@SuppressWarnings("serial")
public class PanelTabSearch extends JPanel{

	/**
	 * Constants.
	 */
	private String[] queryTypes = {"one", "all", "phrase"}; 
	
	/**
	 * Create Elements as attributes.
	 */
	private JTextField searchBox;
	private JButton searchButton;
	@SuppressWarnings("rawtypes")
	private JComboBox queryComboBox;
	private JLabel nRetrieved;
	private JEditorPane retrievedTweets;
	private JScrollPane scrollRetrievedTweets;
	
	public static JLabel sliderLabel;
	public static JSlider sliderBarPR;
	public static JSlider sliderBarTFIDF;
		
	private JPanel checkPanel;
	private JCheckBox checkAuthor;
	@SuppressWarnings("unused")
	private JCheckBox checkHashtag;
	private JCheckBox checkDescription;
	private JCheckBox checkText;
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public PanelTabSearch(){
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		searchBox = new JTextField("Query...");
		searchButton = new JButton(new ImageIcon(PageRankGUI.iconPath));
		queryComboBox = new JComboBox(queryTypes);
		nRetrieved = new JLabel("Retrieved:" + PageRankGUI.actRetrieved);
		retrievedTweets = new JEditorPane("text/html","");
		retrievedTweets.setEditable(false);
		scrollRetrievedTweets = new JScrollPane(retrievedTweets);
		sliderLabel = new JLabel("(PR not selected!)");
		sliderBarPR = new JSlider(0,100,50);
		sliderBarTFIDF = new JSlider(0,100,50);
		
		// ----------------------------------------------------------------------------
		//                            SOME CONFIGURATIONS
		// ----------------------------------------------------------------------------	
		// Query selector.
		queryComboBox.setSelectedIndex(0);
		
		// Check boxes.
		checkPanel = new JPanel();
		checkAuthor = new JCheckBox("Author");
		checkHashtag = new JCheckBox("Hashtag");
		checkDescription = new JCheckBox("Description");
		checkText = new JCheckBox("Text");
		checkPanel.add(checkAuthor);
		checkPanel.add(checkText);
		checkPanel.add(checkDescription);
		//checkPanel.add(checkHashtag);

		// Slider bars.
		sliderBarPR.setMinimumSize(new Dimension(200,20));
		sliderBarTFIDF.setMinimumSize(new Dimension(200,20));
		sliderBarPR.setMaximumSize(new Dimension(200,20));
		sliderBarTFIDF.setMaximumSize(new Dimension(200,20));
		sliderBarPR.setEnabled(false);
		sliderBarTFIDF.setEnabled(false);
				
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		
		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------		
		GridBagConstraints infoSearchBox = new GridBagConstraints();
		infoSearchBox.insets = new Insets(20,50,3,0);
		infoSearchBox.gridx = 0;
		infoSearchBox.gridy = 1;
		infoSearchBox.weightx = 1.0;
		infoSearchBox.weighty = 0.0;
		infoSearchBox.fill = GridBagConstraints.BOTH;
		infoSearchBox.gridwidth = 2;
		
		GridBagConstraints infoSearchButton = new GridBagConstraints();
		infoSearchButton.insets = new Insets(20,0,3,3);
		infoSearchButton.gridx = 2;
		infoSearchButton.gridy = 1;
		infoSearchButton.weightx = 0.0;
		infoSearchButton.weighty = 0.0;
		
		GridBagConstraints infoQueryComboBox = new GridBagConstraints();
		infoQueryComboBox.insets = new Insets(20,0,3,20);
		infoQueryComboBox.gridx = 3;
		infoQueryComboBox.gridy = 1;
		infoQueryComboBox.weightx = 0.0;
		infoQueryComboBox.weighty = 0.0;
		infoQueryComboBox.anchor= GridBagConstraints.WEST;
		
		GridBagConstraints infoCheckPanel = new GridBagConstraints();
		infoCheckPanel.gridx = 0;
		infoCheckPanel.gridy = 2;
		infoCheckPanel.weightx = 0.0;
		infoCheckPanel.weighty = 0.0;
		infoCheckPanel.gridwidth = 3;
		infoCheckPanel.anchor = GridBagConstraints.CENTER;
		infoCheckPanel.fill = GridBagConstraints.CENTER;
		infoCheckPanel.gridwidth = 4;
		
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
		infoScroll.gridwidth = 4;
		
		GridBagConstraints infoSliderBarPR = new GridBagConstraints();
		infoSliderBarPR.insets = new Insets(3,3,3,3);
		infoSliderBarPR.gridx = 0;
		infoSliderBarPR.gridy = 5;
		infoSliderBarPR.weightx = 0.5;
		infoSliderBarPR.weighty = 0.0;
		infoSliderBarPR.anchor = GridBagConstraints.EAST;
		
		GridBagConstraints infoSliderBarTFIDF = new GridBagConstraints();
		infoSliderBarTFIDF.insets = new Insets(3,3,3,3);
		infoSliderBarTFIDF.gridx = 1;
		infoSliderBarTFIDF.gridy = 5;
		infoSliderBarTFIDF.weightx = 0.5;
		infoSliderBarTFIDF.weighty = 0.0;
		infoSliderBarTFIDF.anchor = GridBagConstraints.WEST;
		
		GridBagConstraints infoSliderLabel= new GridBagConstraints();
		infoSliderLabel.insets = new Insets(3,3,3,3);
		infoSliderLabel.gridx = 2;
		infoSliderLabel.gridy = 5;
		infoSliderLabel.weightx = 0.0;
		infoSliderLabel.weighty = 0.0;
		infoSliderLabel.anchor = GridBagConstraints.WEST;
		infoSliderLabel.gridwidth = 2;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		//this.add(icon,infoIcon);
		this.add(searchBox,infoSearchBox);
		this.add(searchButton,infoSearchButton);
		this.add(queryComboBox,infoQueryComboBox);
		this.add(checkPanel,infoCheckPanel);
		this.add(nRetrieved,infonRetrieved);
		this.add(scrollRetrievedTweets,infoScroll);		
		this.add(sliderBarPR,infoSliderBarPR);
		this.add(sliderBarTFIDF,infoSliderBarTFIDF);
		this.add(sliderLabel,infoSliderLabel);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------			
		this.searchBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Query : " + searchBox.getText());
				requestAndSaveRetrievedTweets();
				loadRetrievedTweets();
			}
		});
		
		this.searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Query : " + searchBox.getText());
				requestAndSaveRetrievedTweets();
				loadRetrievedTweets();
			}
		});
		
		this.queryComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Query Type : " + queryComboBox.getSelectedIndex());
				PageRankGUI.actQueryType = queryComboBox.getSelectedIndex();
			}
			
		});
		
		this.sliderBarPR.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				PageRankGUI.actPagerankMentionPercent = ((JSlider) e.getSource()).getValue();
				loadRetrievedTweets();
			}
		});
		
		this.sliderBarTFIDF.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				PageRankGUI.actPagerankPercent = ((JSlider) e.getSource()).getValue();
				loadRetrievedTweets();
			}
		});		
	}
	
	/**
	 * Method that sets the corresponding score based on the actPagerankMentionPercent and the
	 * actPagerankPercent, and then loads the first maxDocumentLoad documents in the JEditorPane.
	 */
	@SuppressWarnings("unchecked")
	public void loadRetrievedTweets(){
		
		// Set the score.
		for (int i=0; i<PageRankGUI.actTweetsRetrieved.size(); i++)
		    PageRankGUI.actTweetsRetrieved.get(i).computeFinalScore();
		
		// Reoorder.
		Collections.sort(PageRankGUI.actTweetsRetrieved);
		
		// Convert to html with the right value.
		String content = HandlerSolr.convertTweetsToHTML();
		
		// Set the content in the JEditorPane.
		try {
			retrievedTweets.setText(content);
		} catch (Exception e){
			System.out.println("Image error!");
		}
		
		// Set the number of retrieved.
		nRetrieved.setText("Retrieved:" + PageRankGUI.actRetrieved);
	}
	
	/**
	 * Method that for a given query, makes a request to the solr and save in the
	 * static arrayList actRetrievedTweets all the tweets that are part of the 
	 * response.
	 */
	public void requestAndSaveRetrievedTweets(){
		
		// Get the query.
		String query = getQuerySelected();
		String value = getValueSelected();
		
		// Form the final query.
		String formedQuery = "";
		if (query.equals("")) formedQuery = value;
		else formedQuery = query.replace(":", ":" + value + "+");
		
		// Save the tweets that matches with the query.
		HandlerSolr.saveRetrievedTweets(formedQuery);
	}
	
	/**
	 * Method to get the query that corresponds with the checkboxs. 
	 */
	public String getQuerySelected(){
		String query = "";
		if (checkText.isSelected()) query += "text:";
		if (checkAuthor.isSelected()) query += "screen_name:";
		if (checkDescription.isSelected()) query += "description:";
		return query;
	}
	
	/**
	 * Method to get the value that corresponds to the searchBox field and
	 * the type query selected.
	 */
	public String getValueSelected(){
		String value = "";
		
		switch (PageRankGUI.actQueryType) {
		case 0: value = searchBox.getText().replace(" ","+"); break;
		case 1: value = searchBox.getText().replace(" ","+AND+"); break;
		case 2: value = "\"" + searchBox.getText().replace(" ","+") + "\""; break;
		default: System.out.println("ComboBoxOption not handled!"); break;
		}
		
		if (value.equals("")) value = "*";	
		
		return value;
	}	
}

