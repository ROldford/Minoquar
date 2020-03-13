package ui;

import model.GameEntity;
import model.PositionModel;
import utils.GridArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// responsible for displaying entire maze, passes on update requests to relevant squares
public class MazeUIPanel extends JPanel {
    // need to fine tune, so mazes of all sizes start square
    private static final int SQUARE_SIZE = 15;

    private GridArray<MazeSquarePanel> panelList;
    private GameUI gameUI;

    // TODO: use trick from https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui
    //       (override getPreferredSize, constrain in GridBagLayout) so mazePanel stays square

    // REQUIRES: displayData is square (width = height)
    // EFFECTS: creates new MazeUIPanel with grid of maze squares of given side length
    public MazeUIPanel(GridArray<SquareDisplayData> displayData, GameUI gameUI) {
        int sideLength = displayData.getWidth();
        this.panelList = new GridArray<>(sideLength);
        this.gameUI = gameUI;
        setLayout(new GridLayout(sideLength, sideLength));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setPreferredSize(new Dimension(sideLength * SQUARE_SIZE, sideLength * SQUARE_SIZE));
        // TODO: iterateSimultaneously for GridArrays
        for (int y = 0; y < sideLength; y++) {
            for (int x = 0; x < sideLength; x++) {
                MazeSquarePanel panel = new MazeSquarePanel(displayData.get(x, y), this);
                panelList.set(x, y, panel);
                add(panel);
            }
        }
        setVisible(true);
    }

    public void handleClick(ActionEvent e) {
        try {
            MazeSquarePanel clickedPanel = (MazeSquarePanel) e.getSource();
            PositionModel position = panelList.getPositionOfElement(clickedPanel);
            gameUI.handleClickAt(position);
        } catch (ClassCastException cce) {
            System.out.println("Click on maze panel could not be cast properly");
        }
    }

    // REQUIRES: displayData has same dimensions as panelList
    // EFFECTS: updates maze display
    public void updateDisplay(GridArray<SquareDisplayData> displayData) {
        for (int x = 0; x < displayData.getWidth(); x++) {
            for (int y = 0; y < displayData.getHeight(); y++) {
                panelList.get(x, y).updateDisplay(displayData.get(x, y));
            }
        }
    }
}
