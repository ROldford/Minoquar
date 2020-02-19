package model;

import java.util.*;

// represents game as maze level, stores layout and other maze data
// allows verification of legal moves
public class MazeModel {
    private MazeBoardModel mazeBoard;
    private String name;

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

    // EFFECTS: returns true if all squares in list are the same type
    private boolean areSquaresIdentical(List<MazeLayoutModel.MazeSquare> squares) {
        return squares.isEmpty() || Collections.frequency(squares, squares.get(0)) == squares.size();  //stub
    }

    // EFFECTS: return list of strings to display the current maze
    public List<String> displayMaze() {
        return mazeBoard.display();
    }

    // EFFECTS: returns name of maze's size
    public String getSizeName() {
        return MazeSizeModel.getSizeName(mazeBoard.getSize());
    }

    // EFFECTS: returns length of maze's sides
    public int getSideLength() {
        return MazeSizeModel.getSideLength(mazeBoard.getSize());
    }

    // EFFECTS: returns maze's size code
    private String getSizeCode() {
        return MazeSizeModel.getSizeCode(mazeBoard.getSize());
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public PositionModel getTreasurePosition() {
        return mazeBoard.getTreasurePosition();
    }

    // EFFECTS: returns maze's data in save file format (see Reader)
    public List<String> getSaveData() {
        List<String> saveData = new ArrayList<>();
        saveData.add(name);
        saveData.add(getSizeCode());
        saveData.addAll(mazeBoard.getSaveData());
        return saveData;
    }


}
