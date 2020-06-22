package de.thunderfrog.dir2list;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    private static String buildVersion = "1.0.0";
    private static String githubProject = "https://github.com/VanKhaos/dir2list.JavaGUI";


    public MainApp() {

        labelVersion.setText(buildVersion);

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
                JFrame DialogFrame = new JFrame();
                JFileChooser fileChooser = new JFileChooser();

                FileFilter txtFilter = new FileTypeFilter(".txt", "Text Document");
                fileChooser.addChoosableFileFilter(txtFilter);

                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogTitle("Save dir2list File");

                int userSelection = fileChooser.showSaveDialog(DialogFrame);

                if(userSelection == JFileChooser.APPROVE_OPTION){
                    try {
                        File Filename = fileChooser.getSelectedFile();
                        FileWriter FolderWriter = new FileWriter(Filename.getAbsolutePath());
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
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame MainFrame = new JFrame("dir2List");
        MainFrame.setContentPane(new MainApp().panelMain);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.setLocationByPlatform(true);

        // Top Menu Bar
        JMenuBar menuBar = new JMenuBar();
        // First Menu
        JMenu menuFile = new JMenu("Info & Exit");
        // Dropdown
        JMenuItem menuItemAbout = new JMenuItem("About");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        // Add Dropdown
        menuFile.add(menuItemAbout);
        menuFile.add(menuItemExit);
        // Dropdown ActionListener
        menuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JLabel label = new JLabel();
                Font font = label.getFont();

                // create some css from the label's font
                StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
                style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
                style.append("font-size:" + font.getSize() + "pt;");

                JEditorPane Pane = new JEditorPane(
                        "text/html",
                        "<html><body style=\"" + style + "\">" +
                          "dir2list Java GUI<br>" +
                          "Version: " + buildVersion + "<br>" +
                          "© 2020 VanKhaos (ThunderFrog MEDIA)<br>" +
                          "<a href=\"#\">Visit Project on GitHub</a>" +
                          "</body></html>"
                );

                // handle link events
                Pane.addHyperlinkListener(new HyperlinkListener()
                {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent e)
                    {
                        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                        try {

                            Desktop.getDesktop().browse(new URI(githubProject));

                        } catch (IOException | URISyntaxException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
                Pane.setEditable(false);
                Pane.setBackground(label.getBackground());
                // show
                JOptionPane.showMessageDialog(null, Pane);
            }
        });
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        // Add Menu to Menu Bar
        menuBar.add(menuFile);
        // Add Menu Bar to Frame
        MainFrame.setJMenuBar(menuBar);



        MainFrame.setSize(350,400);
        MainFrame.setResizable(false);

        MainFrame.setVisible(true);
    }

}
