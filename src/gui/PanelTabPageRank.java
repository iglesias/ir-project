/***********************************************************************
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
	private PanelSelectionOption selectionOption;
	private PanelShowPageRank showPagerank;

	public PanelTabPageRank(){
		
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		selectionUniverse = new PanelSelectionUniverse();		
		selectionMethod = new PanelSelectionMethod();
		selectionOption = new PanelSelectionOption();
		showPagerank = new PanelShowPageRank();
		
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		selectionUniverse.setSize(new Dimension(125,150));
		selectionUniverse.setMinimumSize(new Dimension(125,150));
		selectionMethod.setSize(new Dimension(125,150));
		selectionMethod.setMinimumSize(new Dimension(125,150));
		selectionOption.setSize(new Dimension(125,150));
		selectionOption.setMinimumSize(new Dimension(125,150));

		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------
		GridBagConstraints infoUniverse = new GridBagConstraints();
		infoUniverse.gridx = 0;
		infoUniverse.gridy = 0;
		infoUniverse.weightx = 0.0;
		infoUniverse.weighty = 0.0;
		infoUniverse.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoMethod = new GridBagConstraints();
		infoMethod.gridx = 0;
		infoMethod.gridy = 1;
		infoMethod.weightx = 0.0;
		infoMethod.weighty = 0.0;
		infoMethod.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoOption = new GridBagConstraints();
		infoOption.gridx = 0;
		infoOption.gridy = 2;
		infoOption.weightx = 0.0;
		infoOption.weighty = 0.0;
		infoOption.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoPagerank = new GridBagConstraints();
		infoPagerank.gridx = 1;
		infoPagerank.gridy = 0;
		infoPagerank.weightx = 1.0;
		infoPagerank.weighty = 1.0;
		infoPagerank.fill = GridBagConstraints.BOTH;
		infoPagerank.gridheight = 3;

		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		this.add(selectionUniverse,infoUniverse);
		this.add(selectionMethod,infoMethod);
		this.add(selectionOption,infoOption);
		this.add(showPagerank,infoPagerank);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------
		
	}
	
}
