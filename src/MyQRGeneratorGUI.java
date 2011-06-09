package gilbert.marty.util.qrcode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class MyQRGeneratorGUI {

    JTextField outputDirText;
    JTextField inputFileText;

    JButton convert;
    JButton browseOutputButton;
    JButton browseCSVButton;
    JButton submitButton;
    JButton closeButton;

    JFrame frame;

    JRadioButton contactsButton;
    JRadioButton calendarButton;
    JRadioButton textButton;
    JRadioButton urlButton;



    public static void main(String args[]){
        MyQRGeneratorGUI g = new MyQRGeneratorGUI();
        g.go();
    }

    public void go(){
        frame = new JFrame("Marty's QR Code Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel csvPanel = new JPanel();
        //csvPanel.setLayout(new BoxLayout(csvPanel,BoxLayout.Y_AXIS));
        csvPanel.add(new JLabel("CSV File Location"));

        inputFileText = new JTextField(20);
        inputFileText.setEditable(false);
        csvPanel.add(inputFileText);

        browseCSVButton = new JButton("Browse");
        browseCSVButton.addActionListener(new CSVButtonListener());
        csvPanel.add(browseCSVButton);

        JPanel outputPanel = new JPanel();
        //outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
        JLabel outputDirLabel = new JLabel("Output Location");
        //JLabel outputDirLabel = new JLabel("Output Folder");
        outputDirLabel.setMinimumSize(outputDirLabel.getPreferredSize());
        outputDirLabel.setMaximumSize(outputDirLabel.getPreferredSize());
        outputDirLabel.setPreferredSize(outputDirLabel.getPreferredSize());
        outputPanel.add(outputDirLabel);

        outputDirText = new JTextField(20);
        outputDirText.setEditable(false);
        outputPanel.add(outputDirText);

        browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(new BrowseOutputButtonListener());
        outputPanel.add(browseOutputButton);

        JPanel optionsPanel = new JPanel();
        //optionsPanel.setLayout(new BoxLayout(optionsPanel,BoxLayout.Y_AXIS));
        optionsPanel.setLayout(new GridLayout(0,2));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Target Type"));

        contactsButton = new JRadioButton("Contacts");
        optionsPanel.add(contactsButton);

        calendarButton = new JRadioButton("Calendar");
        optionsPanel.add(calendarButton);

        textButton = new JRadioButton("Text");
        textButton.setEnabled(false);
        optionsPanel.add(textButton);

        urlButton = new JRadioButton("URL");
        urlButton.setEnabled(false);
        optionsPanel.add(urlButton);


        ButtonGroup buttons = new ButtonGroup();
        buttons.add(contactsButton);
        buttons.add(calendarButton);
        buttons.add(urlButton);
        buttons.add(textButton);
        

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input/Output"));

        //csvPanel.setMaximumSize(new Dimension(400,75));
        //outputPanel.setMaximumSize(new Dimension(,75));
        inputPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        inputPanel.add(csvPanel,JPanel.RIGHT_ALIGNMENT);
        inputPanel.add(outputPanel,JPanel.RIGHT_ALIGNMENT);
        inputPanel.add(optionsPanel);

        JPanel buttonPanel = new JPanel();
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        buttonPanel.add(submitButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(new CloseButtonListener());
        buttonPanel.add(closeButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setSize(495,245);
        frame.setVisible(true);

    }

    private File outputDir = null;
    private void selectOutputDir(){
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = c.showOpenDialog(frame);
        if(retVal == JFileChooser.APPROVE_OPTION){
            outputDir = c.getSelectedFile(); 
            outputDirText.setText(outputDir.getPath());
        }

    }

    private File inputFile = null;
    private void selectCSVFile(){
        JFileChooser c = new JFileChooser();
        int retVal = c.showOpenDialog(frame);
        if(retVal == JFileChooser.APPROVE_OPTION){
            inputFile = c.getSelectedFile();
            inputFileText.setText(inputFile.getPath());
        }

    }

    class CSVButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            selectCSVFile();
        }
    }

    class BrowseOutputButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            selectOutputDir();
        }
    }

    class SubmitButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class CloseButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
}
