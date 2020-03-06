package model;

import java.util.*;

// represents game as maze level, stores layout and other maze data
// allows verification of legal moves
public class MazeModel {
    private MazeBoardModel mazeBoard;
    private String name;

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    // EFFECTS: Constructs random maze with given name and size
    public MazeModel(String name, MazeSizeModel.MazeSize size) {
        this.name = name;
        this.mazeBoard = new MazeBoardModel(size);
    }

    //EFFECTS: Constructs maze with given name, size and layout data
    public MazeModel(String name, MazeSizeModel.MazeSize size, List<String> savedLayout) {
        this.name = name;
        this.mazeBoard = new MazeBoardModel(size, savedLayout);
    }

    // EFFECTS: returns name of maze
    public String getName() {
        return name;
    }

    // EFFECTS: returns name of maze's size
    public String getSizeName() {
        return MazeSizeModel.getSizeName(mazeBoard.getSize());
    }

    // EFFECTS: returns length of maze's sides
    public int getSideLength() {
        return MazeSizeModel.getSideLength(mazeBoard.getSize());
    }

    // REQUIRES: start and end are within maze size
    //           start and end are on same orthogonal line
    //           start and end are not same position
    // EFFECTS: returns true if move follows proper movement rules, false otherwise
    public boolean isMoveValid(PositionModel start, PositionModel end) {
        if (mazeBoard.getSquare(end) == MazeLayoutModel.MazeSquare.WALL) {
            return false;
        } else {
            return areSquaresIdentical(mazeBoard.getSquaresBetween(start, end));
        }
    }

    // EFFECTS: returns list of valid move endpoints from given start position in given direction
    public List<PositionModel> getValidMoves(PositionModel start, Direction direction) {
        List<PositionModel> squaresInDirection = mazeBoard.getSquaresInDirection(start, direction);
        List<PositionModel> validMoves = new ArrayList<>();
        for (PositionModel possibleEnd : squaresInDirection) {
            if (isMoveValid(start, possibleEnd)) {
                validMoves.add(possibleEnd);
            }
        }
        return validMoves;
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public PositionModel getTreasurePosition() {
        return mazeBoard.getTreasurePosition();
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    public PositionModel getMinotaurStartPosition() {
        return mazeBoard.getMinotaurStartPosition();
    }

    // EFFECTS: return list of strings to display the current maze
    public List<String> displayMaze() {
        return mazeBoard.display();
    }

    // EFFECTS: returns maze's data in save file format (see Reader)
    public List<String> getSaveData() {
        List<String> saveData = new ArrayList<>();
        saveData.add(name);
        saveData.add(getSizeCode());
        saveData.addAll(mazeBoard.getSaveData());
        return saveData;
    }

    // EFFECTS: returns true if all squares in list are the same type
    private boolean areSquaresIdentical(List<MazeLayoutModel.MazeSquare> squares) {
        return squares.isEmpty() || Collections.frequency(squares, squares.get(0)) == squares.size();  //stub
    }

    // EFFECTS: returns maze's size code
    private String getSizeCode() {
        return MazeSizeModel.getSizeCode(mazeBoard.getSize());
    }

    // EFFECTS: returns string describing maze
    @Override
    public String toString() {
        return String.format("%s - %s (%dx%d)", name, getSizeName(), getSideLength(), getSideLength());
    }
}
