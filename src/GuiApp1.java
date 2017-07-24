//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class GuiApp1 {
	static TwoWaySerialComm CommA;
    //Note: Typically the main method will be in a
    //separate class. As this is a simple one class
    //example it's all in the one class.
    public static void main(String[] args) {
        
        new GuiApp1();
        CommA = new TwoWaySerialComm(); 
        try
        {
           CommA.connect("COM4");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public GuiApp1()
    {
        JFrame guiFrame = new JFrame();
        
        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("DROC Lighting");
        guiFrame.setSize(300,125);
      
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        
        //The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel redlbl = new JLabel("Red:");
        JLabel greenlbl = new JLabel("Green:");
        JLabel bluelbl = new JLabel("Blue:");
        
        JTextField redval = new JTextField();
        redval.setPreferredSize(new Dimension( 50, 24 ));
        JTextField greenval = new JTextField();
        greenval.setPreferredSize(new Dimension( 50, 24 ));
        JTextField blueval = new JTextField();
        blueval.setPreferredSize(new Dimension( 50, 24 ));

        comboPanel.add(redlbl);
        comboPanel.add(redval);
        comboPanel.add(greenlbl);
        comboPanel.add(greenval);
        comboPanel.add(bluelbl);
        comboPanel.add(blueval);
        
        JButton initColor = new JButton( "Colorize"); 
        //The ActionListener class is used to handle the
        //event that happens when the user clicks the button.
        //As there is not a lot that needs to happen we can 
        //define an anonymous inner class to make the code simpler.
        initColor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
               //When the fruit of veg button is pressed
               //the setVisible value of the listPanel and
               //comboPanel is switched from true to 
               //value or vice versa.
            	
            	//LIGHTING
            	if (redval.getText().isEmpty() || greenval.getText().isEmpty() || blueval.getText().isEmpty()) {
            		JOptionPane.showMessageDialog(null, "All Values Must Be Filled.", "Error", JOptionPane.PLAIN_MESSAGE);
            	} else {
            	System.out.println("Red: " + redval.getText() + " Green: " + greenval.getText() + " Blue: " + blueval.getText() + "\n");
            	CommA.setRgbSet(1);
            	try {
					CommA.setRGB(redval.getText() + " " + greenval.getText() + " " + blueval.getText() + " " + "256");
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	}
            }
        });
        
        //The JFrame uses the BorderLayout layout manager.
        guiFrame.add(comboPanel, BorderLayout.NORTH);
        guiFrame.add(initColor,BorderLayout.SOUTH);
        
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }
    
}