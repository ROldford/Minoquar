package model;

import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridPosition;
import ui.SquareDisplayData;

import java.util.ArrayList;
import java.util.List;

// stores maze layout, allows querying of maze locations
public class MazeBoardModel {

    private MazeLayoutModel mazeLayout;

    // EFFECTS: Construct random board (only PASSAGE) of given size
    public MazeBoardModel(MazeSizeModel.MazeSize size) throws Exception {
        this.mazeLayout = MazeLayoutModel.createRandomMaze(size);
    }

    // EFFECTS: Constructs maze with given size and layout data
    //          TODO: document IllegalArgumentException
    public MazeBoardModel(MazeSizeModel.MazeSize size, List<String> savedLayout) throws Exception {
        this.mazeLayout = MazeLayoutModel.createMazeFromMazeContent(size, savedLayout);
    }

    // EFFECTS: return this maze board's size
    public MazeSizeModel.MazeSize getSize() {
        return mazeLayout.getSize();
    }

    // EFFECTS: return state of board square at given position
    public MazeLayoutModel.MazeSquare getSquare(GridPosition position) throws GridPositionOutOfBoundsException {
        return mazeLayout.getSquare(position);
    }

    // REQUIRES: start and end are within maze size
    //           start and end are on same orthogonal line
    //           start and end are not same position
    // EFFECTS: returns list of squares between start and end (exclusive)
    public List<MazeLayoutModel.MazeSquare> getSquaresBetween(GridPosition start, GridPosition end)
            throws GridPositionOutOfBoundsException {
        List<MazeLayoutModel.MazeSquare> betweenList = new ArrayList<>();
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();
        if (deltaX == 0) {
            int sign = Integer.signum(deltaY);
            for (int i = start.getY() + sign; Math.abs(i - end.getY()) > 0; i += sign) {
                betweenList.add(getSquare(new GridPosition(start.getX(), i)));
            }
        } else {
            int sign = Integer.signum(deltaX);
            for (int i = start.getX() + sign; Math.abs(i - end.getX()) > 0; i += sign) {
                betweenList.add(getSquare(new GridPosition(i, start.getY())));
            }
        }
        return betweenList;
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public GridPosition getTreasurePosition() {
        return mazeLayout.getTreasurePosition();
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    public GridPosition getMinotaurStartPosition() {
        return mazeLayout.getMinotaurStartPosition();
    }

    // EFFECTS: return list of strings to display the current maze board
    public Grid<SquareDisplayData> display() {
        return mazeLayout.display();
    }

    // EFFECTS: returns maze board's data in save file format (see Reader)
    public List<String> getSaveData() {
        return mazeLayout.getSaveData();
    }

    // EFFECTS: returns squares from start to edge of board in given direction
    public List<GridPosition> getSquaresInDirection(GridPosition start, MazeModel.Direction direction) {
        GridPosition increment;
        List<GridPosition> squares = new ArrayList<>();
        if (direction == MazeModel.Direction.UP) {
            increment = new GridPosition(0, -1);
        } else if (direction == MazeModel.Direction.DOWN) {
            increment = new GridPosition(0, 1);
        } else if (direction == MazeModel.Direction.LEFT) {
            increment = new GridPosition(-1, 0);
        } else {
            increment = new GridPosition(1, 0);
        }
        GridPosition possibleEnd = start.add(increment);
        while (mazeLayout.inBounds(possibleEnd)) {
            squares.add(possibleEnd);
            possibleEnd = possibleEnd.add(increment);
        }
        return squares; //stub
    }
}
