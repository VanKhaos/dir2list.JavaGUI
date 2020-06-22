package de.thunderfrog.dir2list.dir2list;

import de.thunderfrog.dir2list.controller.DirController;

import javax.swing.*;


public class MainApp {

    public static void main(String[] args) {
        DirController dirController = new DirController();
        dirController.init();
    }
}
