package de.thunderfrog.dir2list;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


public class MainApp {
    private JTextField txtPath;
    private JButton scanButton;
    private JList lstFolders;
    private JButton createListButton;
    private JPanel panelMain;
    private JLabel labelFoldersCount;
    private JLabel labelVersion;
    private DefaultListModel PathModel = new DefaultListModel();
    public static ArrayList<String> Folders = new ArrayList<String>();
    private String Filename;


    public MainApp() {

        labelVersion.setText("1.0.0");

        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    PathModel.clear();
                    Files.list(new File(txtPath.getText()).toPath())
                            .forEach(path -> {
                                PathModel.addElement(path);
                                Folders.add(path.toString());
                                // System.out.println(path.toString());
                            });
                    lstFolders.setModel(PathModel);
                    int FolderSize = PathModel.getSize();
                    labelFoldersCount.setText(String.valueOf(FolderSize));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        createListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame DialogFrame = new JFrame("Enter Filename");
                Filename = JOptionPane.showInputDialog(DialogFrame,"Filename:");
                // Check Filename Empty
                if(!Filename.equals("")){
                    try {
                        FileWriter FolderWriter = new FileWriter(Filename + ".txt");
                        // File Header
                        FolderWriter.write("dir2list Folders\n\n");
                        // Write Array to File
                        for (String folder : Folders) {
                            FolderWriter.write(folder + "\n");
                        }
                        // FileWrite Close
                        FolderWriter.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Please Enter Filename");
                }


            }
        });
    }

    public static void main(String[] args){
        JFrame MainFrame = new JFrame("dir2List");
        MainFrame.setContentPane(new MainApp().panelMain);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.setLocationByPlatform(true);

        MainFrame.setSize(350,400);
        MainFrame.setResizable(false);

        MainFrame.setVisible(true);
    }

}
