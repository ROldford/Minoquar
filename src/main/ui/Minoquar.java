package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Minoquar extends JFrame{
    private MenuUI menuUI;

    public Minoquar() {
        setTitle("Minoquar");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });
        this.menuUI = new MenuUI();
        add(menuUI);
        pack();
        setVisible(true);
    }

    private void exitProcedure() {
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        Minoquar appMainWindow = new Minoquar();
    }
}
