package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControlPanel extends JPanel {
    public static final String DEFAULT_MESSAGE_LABEL = "Your move!";
    public static final String BAD_MOVE_MESSAGE_LABEL = "That's not a valid move. Try again!";
    public static final String GAME_LOSS_MESSAGE_LABEL = "Sorry, you lost";
    public static final String GAME_WIN_MESSAGE_LABEL = "You win!";

    private JButton quitButton;
    private JLabel messageLabel;
    private GameUI gameUI;

    private static final String QUIT_BUTTON_LABEL = "Quit Game";
    private static final EmptyBorder MESSAGE_LABEL_PADDING = new EmptyBorder(0, 5, 0, 5);

    public GameControlPanel(GameUI gameUI) {
        this.gameUI = gameUI;
        setLayout(new BorderLayout());
        // set up components
        this.quitButton = setUpQuitButton();
        this.messageLabel = setUpMessageLabel();
        // add components
        add(quitButton, BorderLayout.WEST);
        add(messageLabel, BorderLayout.EAST);
    }

    private JButton setUpQuitButton() {
        String label = QUIT_BUTTON_LABEL;
        quitButton = new JButton(label);
        QuitButtonListener quitButtonListener = new QuitButtonListener();
        quitButton.setActionCommand(label);
        quitButton.addActionListener(quitButtonListener);
        return quitButton;
    }

    private JLabel setUpMessageLabel() {
        JLabel messageLabel = new JLabel(DEFAULT_MESSAGE_LABEL);
        messageLabel.setBorder(MESSAGE_LABEL_PADDING);
        return messageLabel;
    }

    public void updateMessageLabel(String message) {
        this.messageLabel.setText(message);
    }

    public void resetMessageLabel() {
        this.messageLabel.setText(DEFAULT_MESSAGE_LABEL);
    }

    class QuitButtonListener implements ActionListener {

        // EFFECTS: quits game and goes back to menu UI
        @Override
        public void actionPerformed(ActionEvent e) {
            gameUI.handleGameQuit();
        }
    }
}
