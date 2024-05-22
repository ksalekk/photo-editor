package views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Class for menubar, that contains file and edit actions (open, save, exit, undo, redo)
 */
public class MainWindowMenubar extends JMenuBar {
    public JMenuItem open;
    public JMenuItem save;
    public JMenuItem exit;

    public JMenuItem undo;
    public JMenuItem redo;

    public MainWindowMenubar() {
        add(fileMenuSetup());
        add(editMenuSetup());
    }

    private JMenu fileMenuSetup() {
        JMenu fileMenu = new JMenu("File");

        open = new JMenuItem("Open...", UIManager.getIcon("FileView.directoryIcon"));
        save = new JMenuItem("Save As...", UIManager.getIcon("FileView.floppyDriveIcon"));
        exit = new JMenuItem("Exit");

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(new JSeparator());
        fileMenu.add(exit);

        return fileMenu;
    }

    private JMenu editMenuSetup() {
        JMenu editMenu = new JMenu("Edit");

        undo = new JMenuItem("Undo",  UIManager.getIcon("AbstractUndoableEdit.undoText"));
        redo = new JMenuItem("Redo", UIManager.getIcon("AbstractUndoableEdit.undoText"));

        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));


        editMenu.add(undo);
        editMenu.add(redo);

        return editMenu;
    }
}
