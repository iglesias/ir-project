/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/
package gui;

import javax.swing.JTabbedPane;


@SuppressWarnings("serial")
public class PanelTabbed extends JTabbedPane{

	/**
	 * Constants
	 */
	public static final String tab1Name = "PageRanks";
	public static final String tab2Name = "Search";
	
	public static final int tabSelected = 0;
	
	/**
	 * Declare components as attributes.
	 */
	private PanelTabPageRank pagerankPanel = new PanelTabPageRank();
	private PanelTabSearch searchPanel = new PanelTabSearch();
	
	/**
	 * Constructor
	 */
	public PanelTabbed(){
		
		// Add the different tabs.
		this.add(tab1Name, pagerankPanel);
		this.add(tab2Name, searchPanel);
		
		// Set the first as default.
		this.setSelectedIndex(tabSelected);
		
	}
	
	
}
