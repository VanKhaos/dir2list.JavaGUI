package de.thunderfrog.dir2list.controller;

import de.thunderfrog.dir2list.filter.FileTypeFilter;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;

public class DirController {
    private JTextField txtPath;
    private JButton scanButton;
    private JList<java.nio.file.Path> lstFolders;
    private JButton createListButton;
    private JPanel panelMain;
    private JLabel labelFoldersCount;
    private JLabel labelVersion;
    private DefaultListModel<java.nio.file.Path> PathModel = new DefaultListModel<>();
    public static ArrayList<String> Folders = new ArrayList<>();
    private String Filename;


    public void init() {

        Properties properties = new Properties();
        try {
            InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("project.properties");
            properties.load(input);

        } catch (IOException io) {
            io.printStackTrace();
        }
        labelVersion.setText(properties.getProperty("version"));
        scanButton.addActionListener(e -> {
            try {
                PathModel.clear();
                Files.list(new File(txtPath.getText()).toPath())
                        .forEach(path -> {
                            PathModel.addElement(path);
                            Folders.add(path.toString());
                        });
                lstFolders.setModel(PathModel);
                int FolderSize = PathModel.getSize();
                labelFoldersCount.setText(String.valueOf(FolderSize));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        createListButton.addActionListener(e -> {
            JFrame DialogFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();

            FileFilter txtFilter = new FileTypeFilter(".txt", "Text Document");
            fileChooser.addChoosableFileFilter(txtFilter);

            fileChooser.setAcceptAllFileFilterUsed(false);

            fileChooser.setDialogTitle("Save dir2list File");

            int userSelection = fileChooser.showSaveDialog(DialogFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
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
        });
        JFrame MainFrame = new JFrame("dir2List");
        MainFrame.setContentPane(panelMain);
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
        menuItemAbout.addActionListener(ev -> {
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
                            "Version: " + properties.getProperty("version") + "<br>" +
                            "© 2020 VanKhaos (ThunderFrog MEDIA)<br>" +
                            "<a href=\"#\">Visit Project on GitHub</a>" +
                            "</body></html>"
            );

            // handle link events
            Pane.addHyperlinkListener(e -> {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                    try {

                        Desktop.getDesktop().browse(new URI(properties.getProperty("url")));

                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }

            });
            Pane.setEditable(false);
            Pane.setBackground(label.getBackground());
            // show
            JOptionPane.showMessageDialog(null, Pane);
        });
        menuItemExit.addActionListener(ev -> System.exit(0));
        // Add Menu to Menu Bar
        menuBar.add(menuFile);
        // Add Menu Bar to Frame
        MainFrame.setJMenuBar(menuBar);


        MainFrame.setSize(350, 400);
        MainFrame.setResizable(false);

        MainFrame.setVisible(true);
    }
}
