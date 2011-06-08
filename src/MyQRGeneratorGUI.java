package gilbert.marty.util.qrcode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MyQRGeneratorGUI {

    JTextField outputdir;
    JTextField csvFile;

    JButton convert;
    JButton browseOutputButton;
    JButton browseCSVButton;

    JFrame frame;

    public static void main(String args[]){
        MyQRGeneratorGUI g = new MyQRGeneratorGUI();
        g.go();
    }

    public void go(){
        frame = new JFrame("Marty's QR Code Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel csvPanel = new JPanel();

        csvPanel.add(new JLabel("CSV File Location"));

        csvFile = new JTextField(10);
        csvFile.setEditable(false);
        csvPanel.add(csvFile);


        browseCSVButton = new JButton("Browse");
        //browseCSVButton.addActionListener(new CSVButtonListener());
        csvPanel.add(browseCSVButton);


        JPanel outputPanel = new JPanel();

        outputPanel.add(new JLabel("Image Output Directory"));
 
        outputdir = new JTextField(10);
        outputdir.setEditable(false);
        outputPanel.add(outputdir);

        browseOutputButton = new JButton("Browse");
        //browseOutputButton.addActionListener(new BrowseOutputButtonListener());
        outputPanel.add(browseOutputButton);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));

        csvPanel.setMaximumSize(new Dimension(600,75));
        outputPanel.setMaximumSize(new Dimension(600,75));
        inputPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        inputPanel.add(csvPanel,JPanel.RIGHT_ALIGNMENT);
        inputPanel.add(outputPanel,JPanel.RIGHT_ALIGNMENT);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        //frame.getContentPane().add(outputPanel, BorderLayout.SOUTH);

        frame.setSize(600,150);
        frame.setVisible(true);

    }

}
