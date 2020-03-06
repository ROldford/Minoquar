package ui;

import javax.swing.*;

public class Minoquar {
    private static void createAndShowGUI() {
        // Create and set up window
        JFrame frame = new JFrame("Minoquar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up content pane
        JComponent newContentPane = new MenuUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // Display window
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
