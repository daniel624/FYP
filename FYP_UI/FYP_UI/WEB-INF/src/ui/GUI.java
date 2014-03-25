package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {
	
	public GUI() {
		// constructor
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Simple GUI");
		guiFrame.setSize(500, 500);
		guiFrame.setLocationRelativeTo(null);
		
		String[] fruitOptions = {"Apple", "Apricot", "Banana"
		         ,"Cherry", "Date", "Kiwi", "Orange", "Pear", "Strawberry"};
		        
		String[] vegOptions = {"Asparagus", "Beans", "Broccoli", "Cabbage"
		         , "Carrot", "Celery", "Cucumber", "Leek", "Mushroom"
		         , "Pepper", "Radish", "Shallot", "Spinach", "Swede"
		         , "Turnip"};
		
		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("Fruits:");
		JComboBox fruits = new JComboBox(fruitOptions);
		comboPanel.add(comboLbl);
		comboPanel.add(fruits);
		
		final JPanel listPanel = new JPanel();
		listPanel.setVisible(false);
		JLabel listLbl = new JLabel("Vegetables:");
		JList vegs = new JList(vegOptions);
		vegs.setLayoutOrientation(JList.HORIZONTAL_WRAP);		          
		listPanel.add(listLbl);
		listPanel.add(vegs);
		
		JButton vegFruitBut = new JButton( "Fruit or Veg");
		vegFruitBut.addActionListener(new ActionListener()
		{
		   @Override
		   public void actionPerformed(ActionEvent event)
		   {
		     //When the fruit of veg button is pressed
		     //the setVisible value of the listPanel and
		     //comboPanel is switched from true to 
		     //value or vice versa.
		     listPanel.setVisible(!listPanel.isVisible());
		     comboPanel.setVisible(!comboPanel.isVisible());

		    }
		});
		
		guiFrame.add(comboPanel, BorderLayout.NORTH);
		guiFrame.add(listPanel, BorderLayout.CENTER);
		guiFrame.add(vegFruitBut,BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GUI();
	}

}
