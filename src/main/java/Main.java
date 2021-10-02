import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main extends JFrame {

    private String papka;
    private String path2;
    private File currentDirectory;
    JLabel currentDir;

    public Main() {
        currentDirectory = new File(System.getProperty("user.home"));

        Component component = this;

        setTitle("First app");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(500, 500, 700, 200);
        setLayout(new GridLayout(2, 3));

        JButton buttonHandleThis = new JButton("склеить документы");
        buttonHandleThis.addActionListener(e -> {
            try {
                handlePdf(papka, path2);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JButton buttonFileExplorer1 = new JButton("задать папку");
        buttonFileExplorer1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(currentDirectory);
                int result = fileChooser.showOpenDialog(component);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    papka = selectedFile.getAbsolutePath();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                }
                currentDirectory = fileChooser.getCurrentDirectory();
                currentDir.setText(currentDirectory.getAbsolutePath());
            }
        });

        JButton buttonFileExplorer2 = new JButton("выбрать файлы");
        buttonFileExplorer2.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentDirectory);
            int result = fileChooser.showOpenDialog(component);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                path2 = selectedFile.getAbsolutePath();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
            currentDirectory = fileChooser.getCurrentDirectory();
            currentDir.setText(currentDirectory.getAbsolutePath());
        });

        currentDir = new JLabel(this.currentDirectory.getAbsolutePath());
        add(buttonHandleThis);
        add(buttonFileExplorer1);
        add(buttonFileExplorer2);
        add(currentDir);

        setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        new Main();
    }

    public static void handlePdf(String path1, String path2) throws IOException {

        File directory = new File(path1);
        List<File> pdfFiles = Arrays.stream(directory.listFiles())
                .filter(file -> file.getName().contains(".pdf"))
                .collect(Collectors.toList());

        File file2 = new File(path2);

        for (File file1 : pdfFiles) {
            PDDocument document1 = PDDocument.load(file1);
            PDDocument document2 = PDDocument.load(file2);

            PDPageTree pages = document2.getPages();

            for (PDPage page : pages) {
                document1.addPage(page);
            }
            //Saving the document
            document1.save(file1.getAbsolutePath());
            //Closing the document
            document1.close();
            document2.close();
        }

    }

}