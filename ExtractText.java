import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExtractText {
 
    public static void main(String[] args) {

        //create GUI object and call method
        CreateGUI gui = new CreateGUI();
        gui.createGUI();
    }
}

class CreateGUI implements ActionListener {      
    
    String file;

    JButton selectButton = new JButton("Select PDF File"); 
    JButton extractButton = new JButton("Extract Data"); 
    JTextField wordTextField = new JTextField(20); 
    
    void createGUI() {

        JFrame frame = new JFrame("PDF Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel();        
        
        selectButton.addActionListener(this); 
        extractButton.addActionListener(this); 
        
        JTextArea textArea = new JTextArea(15,30);
        textArea.setEditable(false);
        textArea.setText("This program will extract any lines that" +
            " contain the desired words from a PDF file." + 
            " \n\nOnce you've selected the PDF file and entered the " +
            "desired words to search for, click the Extract Data button." +
            "\n\nThe new text file will be saved to the current directory " + 
            "as whatever the search word was plus \".txt\". For example, " +
            "if the search word was \"10-28-21\", the file will be saved as " +
            " \"10-28-21.txt\"\n\n**This program will only work with PDF files**");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JTextField selectTextField = new JTextField(20);
        selectTextField.setEditable(false);

        final JLabel courtLabel = new JLabel("Search Word");

        panel.add(selectButton);
        panel.add(selectTextField);
        panel.add(courtLabel);
        panel.add(wordTextField);
        panel.add(extractButton);
        panel.add(textArea);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    //action events for clicking buttons
    public void actionPerformed(ActionEvent e) { 
        //select file
        if(e.getSource() == selectButton) {
            System.out.println("Clicked selectButton");
            FileDialog dialog = new FileDialog((Frame)null, "Select File to Open"); 
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);
            file = dialog.getFile();
            System.out.println(file + " chosen.");
        }
        //extract lines with entered text
        if(e.getSource() == extractButton) {
            //create object and call method to obtain desired text from pdf
            ReadPDFData getInfo = new ReadPDFData();
            getInfo.readData(wordTextField.getText(), file);
            System.out.println(wordTextField.getText()); 
        }
    }
}

class ReadPDFData {
    void readData(String selectedText, String file) {  

        try {
            //load all text from pdf
            PDDocument doc = PDDocument.load(new File(file));
            String text = new PDFTextStripper().getText(doc);;
            
            //create file to write data to
            String currentWorkingDirectory = System.getProperty("user.dir");
            System.out.println(currentWorkingDirectory + "\\" + selectedText + ".txt");
            FileWriter myWriter = new FileWriter(currentWorkingDirectory + "\\" + selectedText + ".txt");

            //split text by new lines into array	    
            String lines[] = text.split("\\r?\\n");
            
            //get desired lines from pdf
            for(int index = 0; index < lines.length; index++) { 
                if(lines[index].contains(selectedText)) {  
                    myWriter.write(lines[index] + "\n");
	            }
	        }
	        myWriter.close();
	        System.out.println("File successfully written.");
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
