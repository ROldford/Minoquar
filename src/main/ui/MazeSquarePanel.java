package ui;

import model.GameEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// responsible for displaying the maze square with any overlays, responds to update requests
public class MazeSquarePanel extends JButton {
    private SquareDisplayData displayData;
    private MazeUIPanel mazeUIPanel;

    public MazeSquarePanel(SquareDisplayData displayData, MazeUIPanel mazeUIPanel) {
        this.mazeUIPanel = mazeUIPanel;
        setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        setOpaque(true);
        addActionListener(new PanelPressListener());
        drawPanel(displayData);
    }

    // MODIFIES: this
    // EFFECTS: draws panel according to given display data
    private void drawPanel(SquareDisplayData displayData) {
        this.displayData = displayData;
        switch (displayData.getSquareStatus()) {
            case PASSAGE:
                setBackground(Color.lightGray);
                break;
            case WALL:
                setBackground(Color.black);
                break;
            default:
                // Should not get this!
                // TODO: add exception for this?
                setBackground(Color.red);
                break;
        }
        repaint();
    }

    // EFFECT: updates panel display if given display data is new
    public void updateDisplay(SquareDisplayData displayData) {
        if (this.displayData != displayData) {
            drawPanel(displayData);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<GameEntity.EntityType> entities = displayData.getEntityTypes();
        Color savedColor = g.getColor();
        // needs fine tuning to work with grid size
        // TODO: make margin sizing responsive to grid size
        int entityMargin = 2;
        int entityWidth = getWidth() - 2 * entityMargin;
        int entityHeight = getHeight() - 2 * entityMargin;
        if (entities.contains(GameEntity.EntityType.TREASURE)) {
            g.setColor(Color.yellow);
            g.fillRect(entityMargin, entityMargin, entityWidth, entityHeight);
        }
        if (entities.contains(GameEntity.EntityType.HERO)) {
            g.setColor(Color.blue);
            g.fillOval(entityMargin, entityMargin, entityWidth, entityHeight);
        }
        if (entities.contains(GameEntity.EntityType.MINOTAUR)) {
            g.setColor(Color.red);
            int bottomMargin = getHeight() - entityMargin;
            int rightMargin = getWidth() - entityMargin;
            int[] pointsX = new int[]{getWidth() / 2, entityMargin, rightMargin};
            int[] pointsY = new int[]{entityMargin, bottomMargin, bottomMargin};
            g.fillPolygon(pointsX, pointsY, 3);
        }
    }

    class PanelPressListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println(e.getSource());  // can use e.getSource to check which panel was clicked
            mazeUIPanel.handleClick(e);
        }
    }
}
