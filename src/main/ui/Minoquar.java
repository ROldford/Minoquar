package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Minoquar extends JFrame {
    private MenuUI menuUI;
    private GameUI gameUI;

    // EFFECTS: create new Minoquar app, set up app window and starting UI
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
        this.menuUI = new MenuUI(this);
        this.gameUI = new GameUI(this);
        add(menuUI);
        pack();
        setVisible(true);
    }

    // EFFECTS: complete any actions that need to be done when app is closed (i.e. file save)
    private void exitProcedure() {
        menuUI.saveMazes();
        dispose();
        System.exit(0);
    }

    // MODIFIES: this
    // EFFECTS: swaps UI to game mode
    public void swapToGameUI() {
        swapComponentsAndResizeUI(menuUI, gameUI);
    }

    // MODIFIES: this
    // EFFECTS: swaps UI to menu mode
    public void swapToMenuUI() {
        swapComponentsAndResizeUI(gameUI, menuUI);
    }

    // MODIFIES: this
    // EFFECTS: swaps visible UI component from current to next, resizes UI to match new component size
    private void swapComponentsAndResizeUI(
            JComponent current,
            JComponent next) {
        remove(current);
        add(next);
        pack();
    }

    // EFFECTS: starts app
    public static void main(String[] args) {
        Minoquar appMainWindow = new Minoquar();
    }
}
