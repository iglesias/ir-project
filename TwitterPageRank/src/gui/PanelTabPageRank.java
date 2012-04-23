/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelTabPageRank extends JPanel{
	
	/**
	 * Declare components as attributes.
	 */
	private PanelSelectionUniverse selectionUniverse;
	private PanelSelectionMethod selectionMethod;
	private PanelShowPageRank showPagerank;
	private PanelBlackList showBlackList;

	public PanelTabPageRank(){
		
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		selectionUniverse = new PanelSelectionUniverse();		
		selectionMethod = new PanelSelectionMethod();
		showBlackList = new PanelBlackList();
		showPagerank = new PanelShowPageRank();
		
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		selectionUniverse.setMinimumSize(new Dimension(50,50));
		selectionMethod.setMinimumSize(new Dimension(50,50));
		showBlackList.setMinimumSize(new Dimension(50,50));
		
		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------
		GridBagConstraints infoUniverse = new GridBagConstraints();
		infoUniverse.gridx = 0;
		infoUniverse.gridy = 0;
		infoUniverse.weightx = 1.0;
		infoUniverse.weighty = 0.0;
		infoUniverse.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoMethod = new GridBagConstraints();
		infoMethod.gridx = 1;
		infoMethod.gridy = 0;
		infoMethod.weightx = 1.0;
		infoMethod.weighty = 0.0;
		infoMethod.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoBlack = new GridBagConstraints();
		infoBlack.gridx = 2;
		infoBlack.gridy = 0;
		infoBlack.weightx = 1.0;
		infoBlack.weighty = 0.0;
		infoBlack.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoPagerank = new GridBagConstraints();
		infoPagerank.gridx = 0;
		infoPagerank.gridy = 1;
		infoPagerank.weightx = 1.0;
		infoPagerank.weighty = 1.0;
		infoPagerank.fill = GridBagConstraints.BOTH;
		infoPagerank.gridwidth = 3;

		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		this.add(selectionUniverse,infoUniverse);
		this.add(selectionMethod,infoMethod);
		this.add(showBlackList,infoBlack);
		this.add(showPagerank,infoPagerank);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------
		
	}
	
}
