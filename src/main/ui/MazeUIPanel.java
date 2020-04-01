package ui;

import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridArray;
import grid.GridIterator;
import grid.GridPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// responsible for displaying entire maze, passes on update requests to relevant squares
public class MazeUIPanel extends JPanel {
    // need to fine tune, so mazes of all sizes start square
    private static final int SQUARE_SIZE = 15;

    private Grid<MazeSquarePanel> panelGrid;
    private GameUI gameUI;

    // TODO: use trick from https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui
    //       (override getPreferredSize, constrain in GridBagLayout) so mazePanel stays square
    // REQUIRES: displayData is square (width = height)
    // EFFECTS: creates new MazeUIPanel with grid of maze squares of given side length
    public MazeUIPanel(Grid<SquareDisplayData> displayData, GameUI gameUI)
            throws GridPositionOutOfBoundsException {  // TODO: replace throw with actual handling
        int sideLength = displayData.getWidth();
        this.panelGrid = new GridArray<>(sideLength);
        this.gameUI = gameUI;
        setLayout(new GridLayout(sideLength, sideLength));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setPreferredSize(new Dimension(sideLength * SQUARE_SIZE, sideLength * SQUARE_SIZE));
        // TODO: iterateSimultaneously for GridArrays
        GridIterator<SquareDisplayData> displayIterator = displayData.gridCellIterator();
        GridIterator<MazeSquarePanel> panelIterator = panelGrid.gridCellIterator();
        while (displayIterator.hasNext()) {
            SquareDisplayData displaySquare = displayIterator.next();
            MazeSquarePanel panel = new MazeSquarePanel(displaySquare, this);
            panelIterator.next();
            panelIterator.set(panel);
            add(panel);
        }
//        for (int y = 0; y < sideLength; y++) {
//            for (int x = 0; x < sideLength; x++) {
//                MazeSquarePanel panel = new MazeSquarePanel(displayData.get(x, y), this);
//                panelGrid.set(x, y, panel);
//                add(panel);
//            }
//        }
        setVisible(true);
    }

    public void handleClick(ActionEvent e) {
        try {
            MazeSquarePanel clickedPanel = (MazeSquarePanel) e.getSource();
            GridPosition position = panelGrid.positionOf(clickedPanel);
            gameUI.handleClickAt(position);
        } catch (ClassCastException cce) {
            System.out.println("Click on maze panel could not be cast properly");
        } catch (GridPositionOutOfBoundsException ex) {
            ex.printStackTrace();  // TODO: properly catch this!
        }
    }

    // EFFECTS: updates maze display
    public void updateDisplay(Grid<SquareDisplayData> displayData) throws IllegalArgumentException {
        if (panelGrid.getWidth() != displayData.getWidth() || panelGrid.getHeight() != displayData.getHeight()) {
            throw new IllegalArgumentException(String.format(
                    "Display data: %d wide x %d high, Game panels: %d wide x %d high",
                    displayData.getWidth(), displayData.getHeight(),
                    panelGrid.getWidth(), panelGrid.getHeight()));
        }
        GridIterator<MazeSquarePanel> panelIterator = panelGrid.gridCellIterator();
        GridIterator<SquareDisplayData> displayIterator = displayData.gridCellIterator();
        while (displayIterator.hasNext()) {
            MazeSquarePanel panel = panelIterator.next();
            SquareDisplayData displaySquare = displayIterator.next();
            panel.updateDisplay(displaySquare);
        }
//        for (int x = 0; x < displayData.getWidth(); x++) {
//            for (int y = 0; y < displayData.getHeight(); y++) {
//                panelGrid.get(x, y).updateDisplay(displayData.get(x, y));
//            }
//        }
    }
}
