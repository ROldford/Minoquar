package model;

import java.util.*;

// represents game as maze level, stores layout and other maze data
// allows verification of legal moves
public class MazeModel {
    private MazeLayoutModel mazeLayout;
    private String name;

    // EFFECTS: Constructs random maze with given name and size
    public MazeModel(String name, MazeSizeModel.MazeSize size) {
        this.name = name;
        this.mazeLayout = MazeLayoutModel.createRandomMaze(size);
    }

    // EFFECTS: returns name of maze
    public String getName() {
        return name;
    }

    // REQUIRES: start and end are within maze size
    //           start and end are on same orthagonal line
    //           start and end are not same position
    // EFFECTS: returns true if move follows proper movement rules, false otherwise
    public boolean isMoveValid(PositionModel start, PositionModel end) {
        if (mazeLayout.getSquare(end) == MazeLayoutModel.MazeSquare.WALL) {
            return false;
        } else {
            return areSquaresIdentical(mazeLayout.getSquaresBetween(start, end));
        }
    }

    // EFFECTS: returns true if all squares in list are the same type
    private boolean areSquaresIdentical(List<MazeLayoutModel.MazeSquare> squares) {
        return squares.isEmpty() || Collections.frequency(squares, squares.get(0)) == squares.size();  //stub
    }

    public String getSize() {
        return mazeLayout.getMazeSize();
    }

    public int getSideLength() {
        return mazeLayout.getMazeSideLength();
    }
}
