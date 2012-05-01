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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


@SuppressWarnings("serial")
public class PanelSelectionMethod extends JPanel{

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
	final JList<String> methodList;
	
	/**
	 * Management of elements.
	 */
	private DefaultListModel<String> listModel;  // Management of elements.
	private ListSelectionModel selectionModel;   // Management of selected elements.
	
	/**
	 * Constructor.
	 */
	public PanelSelectionMethod() {
				
		// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		// Create the model to management the elements.
		listModel = new DefaultListModel<String>(); 
		methodList = new JList<String>(listModel);
		
		listModel.addElement(Method_0);
		listModel.addElement(Method_1);
		listModel.addElement(Method_2);
		listModel.addElement(Method_3);
		listModel.addElement(Method_4);
		listModel.addElement(Method_5);
		
		selectionModel = methodList.getSelectionModel(); 
		
		// Create the components.
		title = new JLabel("Methods:");  // Title
		JScrollPane scrollUniverses = new JScrollPane(methodList);
		
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
				if (!e.getValueIsAdjusting() && (methodList.getSelectedValue()!=null)){
					PageRankGUI.actMethod = (String) methodList.getSelectedValue();
					System.out.println("Selected : " + PageRankGUI.actMethod);
					if ((PageRankGUI.actUniverse!=null) && (PageRankGUI.actMethod!=null)){
						PanelShowPageRank.loadPageRank();
					}
				}
			}
		});
		
        
	}
	
}
