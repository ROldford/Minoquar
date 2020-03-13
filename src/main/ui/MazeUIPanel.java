package ui;

import utils.GridArray;

import javax.swing.*;
import java.awt.*;

// responsible for displaying entire maze, passes on update requests to relevant squares
public class MazeUIPanel extends JPanel {
    // need to fine tune, so mazes of all sizes start square
    private static final int SQUARE_SIZE = 15;

    private GridArray<MazeSquarePanel> panelList;

    // TODO: use trick from https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui
    //       (override getPreferredSize, constrain in GridBagLayout) so mazePanel stays square

    // REQUIRES: displayData is square (width = height)
    // EFFECTS: creates new MazeUIPanel with grid of maze squares of given side length
    public MazeUIPanel(GridArray<SquareDisplayData> displayData) {
        int sideLength = displayData.getWidth();
        this.panelList = new GridArray<>(sideLength);
        setLayout(new GridLayout(sideLength, sideLength));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setPreferredSize(new Dimension(sideLength * SQUARE_SIZE, sideLength * SQUARE_SIZE));
        // TODO: iterateSimultaneously for GridArrays
        for (int y = 0; y < sideLength; y++) {
            for (int x = 0; x < sideLength; x++) {
                MazeSquarePanel panel = new MazeSquarePanel(displayData.get(x, y));
                panelList.set(x, y, panel);
                add(panel);
            }
        }
        setVisible(true);
    }
}
