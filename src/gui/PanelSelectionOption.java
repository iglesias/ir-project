/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


@SuppressWarnings("serial")
public class PanelSelectionOption extends JPanel{

	/**
	 * Constants.
	 */
    public final int width = 50;
    public final int height = 50;
	
	public static final String Method_0 = "PowerIteration";
	public static final String Method_1 = "MonteCarlo1";
	public static final String Method_2 = "MonteCarlo2";
	public static final String Method_3 = "MonteCarlo3";
	public static final String Method_4 = "MonteCarlo4";
	public static final String Method_5 = "MonteCarlo5";
		
	/**
	 * Declare components as attributes.
	 */
	private JLabel title;
	final JList<String> optionList;
	
	/**
	 * Management of elements.
	 */
	private DefaultListModel<String> listModel;  // Management of elements.
	private ListSelectionModel selectionModel;   // Management of selected elements.
	
	/**
	 * Constructor.
	 */
	public PanelSelectionOption() {
				
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		// Create the model to management the elements.
		listModel = new DefaultListModel<String>(); 
		optionList = new JList<String>(listModel);
		
		listModel.addElement("all");
		listModel.addElement("followers");
		
		selectionModel = optionList.getSelectionModel(); 
		
		// Create the components.
		title = new JLabel("Options:");  // Title
		JScrollPane scrollOptions = new JScrollPane(optionList);
		
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
		this.add(scrollOptions,infoScroll);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------
		this.selectionModel.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged (ListSelectionEvent e){
				if (!e.getValueIsAdjusting() && (optionList.getSelectedValue()!=null)){
					PageRankGUI.actOption = (String) optionList.getSelectedValue();
					System.out.println("Selected : " + PageRankGUI.actOption);
					if ((PageRankGUI.actUniverse!=null) && (PageRankGUI.actMethod!=null) &&
						 (PageRankGUI.actOption !=null)){
						PanelShowPageRank.loadPageRank();
					}
				}
			}
		});
		
        
	}
	
}