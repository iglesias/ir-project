/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class PanelBlackList extends JPanel{

	/**
	 * Constants.
	 */
    public static final int width = 50;
    public static final int height = 50;
		
	/**
	 * Declare components as attributes.
	 */
	private JLabel title;
	private JTextField text;
	private JButton buttonAdd;
	private JButton buttonDel;
	final JList<String> blackList;
	
	/**
	 * Management of elements.
	 */
	private DefaultListModel<String> listModel;  // Management of elements.
	@SuppressWarnings("unused")
	private ListSelectionModel selectionModel;   // Management of selected elements.
	
    public PanelBlackList(){
		
    	// ----------------------------------------------------------------------------
		//                          CREATE THE COMPONENTS
		// ----------------------------------------------------------------------------
		// Create the model to management the elements.
		listModel = new DefaultListModel<String>(); 
		loadBlackList();
		
		blackList = new JList<String>(listModel);
		
		selectionModel = blackList.getSelectionModel(); 
		
		// Create the components.
		title = new JLabel("Black List:");  // Title
		text = new JTextField(20);
		buttonAdd = new JButton("+");
		buttonDel = new JButton("-");
		JScrollPane scrollBlack = new JScrollPane(blackList);
		
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
		infoScroll.gridwidth = 3;
		infoScroll.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoText = new GridBagConstraints();
		infoText.gridx = 0;
		infoText.gridy = 2;
		infoText.weightx = 1.0;
		infoText.weighty = 0.0;
		infoText.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoAdd = new GridBagConstraints();
		infoAdd.insets = new Insets(3,3,3,1);
		infoAdd.gridx = 1;
		infoAdd.gridy = 2;
		infoAdd.weightx = 0.0;
		infoAdd.weighty = 0.0;
		infoAdd.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints infoDel = new GridBagConstraints();
		infoDel.insets = new Insets(3,1,3,3);
		infoDel.gridx = 2;
		infoDel.gridy = 2;
		infoDel.weightx = 0.0;
		infoDel.weighty = 0.0;
		infoDel.fill = GridBagConstraints.BOTH;
		
		// ----------------------------------------------------------------------------
		//                    ADD THE COMPONENTS TO THE CONTAINER
		// ----------------------------------------------------------------------------
		this.add(title,infoTitle);
		this.add(scrollBlack,infoScroll);
		this.add(text,infoText);
		this.add(buttonAdd,infoAdd);
		this.add(buttonDel,infoDel);
		
		// ----------------------------------------------------------------------------
		//                              EVENTS HANDLERS
		// ----------------------------------------------------------------------------
		this.buttonAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (!listModel.contains(text.getText()))
					listModel.addElement(text.getText());
			}
		});
		
		this.buttonDel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (listModel.contains(text.getText()))
					listModel.removeElement(text.getText());
			}
		});
		
	}

	/**
	 * Method that loads all the users in the black list.
	 */
	public void loadBlackList(){
		ArrayList<String> list = HandlerGUI.getContentBlackList(PageRankGUI.blackListPath);
		if (list != null)
			for (int i=0; i<list.size(); i++)
				listModel.addElement(list.get(i));
	}
}
