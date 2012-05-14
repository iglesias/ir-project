/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class PanelSelectionUniverse extends JPanel{

	/**
	 * Constants.
	 */
    public final int width = 50;
    public final int height = 50;
		
	/**
	 * Declare components as attributes.
	 */
	private JLabel title;
	private JList<String> universeList;
	
	/**
	 * Management of elements.
	 */
	private DefaultListModel<String> listModel;          // Management of elements.
	private ListSelectionModel selectionModel;   // Management of selected elements.
	
	/**
	 * Constructor.
	 */
	public PanelSelectionUniverse() {
				
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		// Create the model to management the elements.
		listModel = new DefaultListModel<String>(); 
		loadUniverses();
		
		universeList = new JList<String>(listModel);
		
		selectionModel = universeList.getSelectionModel(); 
		
		// Create the components.
		title = new JLabel("Universes:");  
		JScrollPane scrollUniverses = new JScrollPane(universeList);
		
		// ----------------------------------------------------------------------------
		//                        DEFINE LAYOUT ADMINISTRATOR
		// ----------------------------------------------------------------------------
		this.setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(width,height));
		this.setMaximumSize(new Dimension(width,height));
		
		// ----------------------------------------------------------------------------
		//                         DEFINE GRIDBAGCONSTRAINTS
		// ----------------------------------------------------------------------------
		GridBagConstraints infoTitle = new GridBagConstraints();
		infoTitle.insets = new Insets(3,3,3,3);
		infoTitle.gridx = 0;
		infoTitle.gridy = 0;
		infoTitle.weightx = 1.0;
		infoTitle.weighty = 0.0;
		infoTitle.anchor = GridBagConstraints.NORTHWEST;
		
		GridBagConstraints infoScroll = new GridBagConstraints();
		infoScroll.gridx = 0;
		infoScroll.gridy = 1;
		infoScroll.weightx = 1.0;
		infoScroll.weighty = 1.0;
		infoScroll.fill = GridBagConstraints.BOTH;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		this.add(title,infoTitle);
		this.add(scrollUniverses,infoScroll);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------
		this.selectionModel.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged (ListSelectionEvent e){
				if (!e.getValueIsAdjusting() && (universeList.getSelectedValue()!=null)){
					PageRankGUI.actUniverse = (String) universeList.getSelectedValue();
					System.out.println("Selected : " + PageRankGUI.actUniverse);
					if ((PageRankGUI.actUniverse!=null) && (PageRankGUI.actMethod!=null) &&
						(PageRankGUI.actOption!=null)){
						PanelShowPageRank.loadPageRank();
					}
				}
			}
		});
	}	
	
	/**
	 * Method that loads all the directories (universes).
	 */
	public void loadUniverses(){
		File[] list = HandlerGUI.getElementsIn(PageRankGUI.rootPath);
		if (list != null)
			for (int i=0; i<list.length; i++)
				if (list[i].isDirectory())
					listModel.addElement(list[i].getName());
	}
}
