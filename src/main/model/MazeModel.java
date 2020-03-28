package model;

import exceptions.GridOperationOutOfBoundsException;
import ui.SquareDisplayData;
import utils.GridArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// represents game as maze level, stores layout and other maze data
// allows verification of legal moves
public class MazeModel {
    private MazeBoardModel mazeBoard;
    private String name;
    // using a list to allow other ways of parsing history (i.e. last 5 games, win streak, etc.)
    private List<Outcome> pastGameOutcomes;

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public enum Outcome {
        WIN,
        LOSS
    }

    private static final String WIN_LETTER = "W";
    private static final String LOSS_LETTER = "L";

    // EFFECTS: Constructs random maze with given name and size
    public MazeModel(String name, MazeSizeModel.MazeSize size) throws GridOperationOutOfBoundsException {
        this.name = name;
        this.mazeBoard = new MazeBoardModel(size);
        this.pastGameOutcomes = new ArrayList<>();
    }

    //EFFECTS: Constructs maze with given name, size, game outcome history and layout data
    public MazeModel(String name,
                     MazeSizeModel.MazeSize size,
                     List<String> savedOutcomeHistory,
                     List<String> savedLayout) throws GridOperationOutOfBoundsException {
        this.name = name;
        this.mazeBoard = new MazeBoardModel(size, savedLayout);
        this.pastGameOutcomes = parseSavedOutcomes(savedOutcomeHistory);
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

    // EFFECTS: returns number of wins on this maze
    public int getWins() {
        return Collections.frequency(pastGameOutcomes, Outcome.WIN);
    }

    // EFFECTS: returns number of losses on this maze
    public int getLosses() {
        return Collections.frequency(pastGameOutcomes, Outcome.LOSS);
    }

    // EFFECTS: returns number of times this maze has been played
    public int getTotalPlays() {
        return pastGameOutcomes.size();
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

    // REQUIRES: start and end are within maze bounds
    // EFFECTS: returns true if move follows proper movement rules, false otherwise
    public boolean isMoveValid(PositionModel start, PositionModel end) throws GridOperationOutOfBoundsException {
        boolean samePosition = start.equals(end);
        boolean orthogonal = areSquaresOrthogonal(start, end);
        boolean endOnWall = mazeBoard.getSquare(end) == Layout.MazeSquare.WALL;
        if (samePosition || !orthogonal || endOnWall) {
            return false;
        } else {
            return areTheseSquaresIdentical(mazeBoard.getSquaresBetween(start, end));
        }
    }

    // EFFECTS: returns list of valid move endpoints from given start position in given direction
    public List<PositionModel> getValidMoves(PositionModel start, Direction direction)
            throws GridOperationOutOfBoundsException {
        List<PositionModel> squaresInDirection = mazeBoard.getSquaresInDirection(start, direction);
        List<PositionModel> validMoves = new ArrayList<>();
        for (PositionModel possibleEnd : squaresInDirection) {
            if (isMoveValid(start, possibleEnd)) {
                validMoves.add(possibleEnd);
            }
        }
        return validMoves;
    }

    // EFFECTS:
    private List<Outcome> parseSavedOutcomes(List<String> savedOutcomeHistory) {
        List<Outcome> outcomes = new ArrayList<>();
        for (String line : savedOutcomeHistory) {
            for (String letter : line.split("")) {
                if (letter.equals(WIN_LETTER)) {
                    outcomes.add(Outcome.WIN);
                } else {
                    outcomes.add(Outcome.LOSS);
                }
            }
        }
        return outcomes;
    }

    // EFFECTS: adds outcome of a game on this maze to start of outcomes list (most recent games first)
    public void registerOutcome(Outcome outcome) {
        pastGameOutcomes.add(0, outcome);
    }

    // EFFECTS: return list of strings to display the current maze
    public GridArray<SquareDisplayData> displayMaze() throws GridOperationOutOfBoundsException {
        return mazeBoard.display();
    }

    // EFFECTS: returns maze's data in save file format (see Reader)
    public List<String> getSaveData() throws GridOperationOutOfBoundsException {
        List<String> saveData = new ArrayList<>();
        saveData.add(name);
        saveData.add(getSizeCode());
        int totalGameOnThisMaze = getTotalPlays();
        saveData.add(Integer.toString(totalGameOnThisMaze));
        for (int i = 0; i * 100 < totalGameOnThisMaze; i++) {
            saveData.add(getPastGameOutcomeSaveData(i));
        }
        saveData.addAll(mazeBoard.getSaveData());
        return saveData;
    }

    private boolean areSquaresOrthogonal(PositionModel start, PositionModel end) {
        return start.getX() == end.getX() || start.getY() == end.getY();
    }

    // EFFECTS: returns true if all squares in list are the same type
    private boolean areTheseSquaresIdentical(List<MazeLayoutModel.MazeSquare> squares) {
        return squares.isEmpty() || Collections.frequency(squares, squares.get(0)) == squares.size();  //stub
    }

    // EFFECTS: returns maze's size code
    private String getSizeCode() {
        return MazeSizeModel.getSizeCode(mazeBoard.getSize());
    }

    // EFFECTS: returns a line of past game outcome history using given index
    //          each line is 100 games, sorted most recent game first
    private String getPastGameOutcomeSaveData(int lineIndex) {
        int start = lineIndex * 100;
        int end = (lineIndex + 1) * 100;
        if (end >= pastGameOutcomes.size()) {
            end = pastGameOutcomes.size();
        }
        List<Outcome> subList = pastGameOutcomes.subList(start, end);
        final Function<Outcome, String> outcomeToString;
        outcomeToString = (Outcome outcome) -> outcome == Outcome.WIN
                ? WIN_LETTER
                : LOSS_LETTER;
        return subList.stream()
                .map(outcomeToString)
                .collect(Collectors.joining(""));
    }

    // EFFECTS: returns string describing maze
    @Override
    public String toString() {
        return String.format("%s - %s (%dx%d) - Games played: %d, Wins: %d, Losses: %d",
                name, getSizeName(), getSideLength(), getSideLength(), getTotalPlays(), getWins(), getLosses());
    }
}
