package model;

import java.util.ArrayList;
import java.util.List;

// stores maze layout, allows querying of maze locations
public class MazeBoardModel {

    private MazeLayoutModel mazeLayout;

    // EFFECTS: Construct random board (only PASSAGE) of given size
    public MazeBoardModel(MazeSizeModel.MazeSize size) {
        this.mazeLayout = MazeLayoutModel.createRandomMaze(size);
    }

    //EFFECTS: Constructs maze with given size and layout data
    public MazeBoardModel(MazeSizeModel.MazeSize size, List<String> savedLayout) {
        this.mazeLayout = MazeLayoutModel.createMazeFromMazeContent(size, savedLayout);
    }

    // EFFECTS: return this maze board's size
    public MazeSizeModel.MazeSize getSize() {
        return mazeLayout.getSize();
    }

    // EFFECTS: return state of board square at given position
    public MazeLayoutModel.MazeSquare getSquare(PositionModel position) {
        return mazeLayout.getSquare(position);
    }

    // REQUIRES: start and end are within maze size
    //           start and end are on same orthogonal line
    //           start and end are not same position
    // EFFECTS: returns list of squares between start and end (exclusive)
    public List<MazeLayoutModel.MazeSquare> getSquaresBetween(PositionModel start, PositionModel end) {
        List<MazeLayoutModel.MazeSquare> betweenList = new ArrayList<>();
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();
        if (deltaX == 0) {
            int sign = Integer.signum(deltaY);
            for (int i = start.getY() + sign; Math.abs(i - end.getY()) > 0; i += sign) {
                betweenList.add(getSquare(new PositionModel(start.getX(), i)));
            }
        } else {
            int sign = Integer.signum(deltaX);
            for (int i = start.getX() + sign; Math.abs(i - end.getX()) > 0; i += sign) {
                betweenList.add(getSquare(new PositionModel(i, start.getY())));
            }
        }
        return betweenList;
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public PositionModel getTreasurePosition() {
        return mazeLayout.getTreasurePosition();
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    public PositionModel getMinotaurStartPosition() {
        return mazeLayout.getMinotaurStartPosition();
    }

    // EFFECTS: return list of strings to display the current maze board
    public List<String> display() {
        return mazeLayout.display();
    }

    // EFFECTS: returns maze board's data in save file format (see Reader)
    public List<String> getSaveData() {
        return mazeLayout.getSaveData();
    }

    // EFFECTS: returns squares from start to edge of board in given direction
    public List<PositionModel> getSquaresInDirection(PositionModel start, MazeModel.Direction direction) {
        PositionModel increment;
        List<PositionModel> squares = new ArrayList<>();
        if (direction == MazeModel.Direction.UP) {
            increment = new PositionModel(0, -1);
        } else if (direction == MazeModel.Direction.DOWN) {
            increment = new PositionModel(0, 1);
        } else if (direction == MazeModel.Direction.LEFT) {
            increment = new PositionModel(-1, 0);
        } else {
            increment = new PositionModel(1, 0);
        }
        PositionModel possibleEnd = start.add(increment);
        while (mazeLayout.isInBounds(possibleEnd)) {
            squares.add(possibleEnd);
            possibleEnd = possibleEnd.add(increment);
        }
        return squares; //stub
    }
}
