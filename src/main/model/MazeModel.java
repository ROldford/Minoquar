package model;

import exceptions.GridPositionOutOfBoundsException;
import exceptions.InvalidMazeSaveDataException;
import grid.Grid;
import grid.GridPosition;
import ui.SquareDisplayData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// represents game as maze level, stores layout and other maze data
// allows verification of legal moves
public class MazeModel {
    private MazeLayoutModel mazeLayout;
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
    public MazeModel(String name, MazeSizeModel.MazeSize size) {
        this.name = name;
        this.mazeLayout = MazeLayoutModel.createRandomMaze(size);
        this.pastGameOutcomes = new ArrayList<>();
    }

    //EFFECTS: Constructs maze with given name, size, game outcome history and layout data
    public MazeModel(String name,
                     MazeSizeModel.MazeSize size,
                     List<String> savedOutcomeHistory,
                     List<String> savedLayout) throws InvalidMazeSaveDataException {
        this.name = name;
        this.mazeLayout = MazeLayoutModel.createMazeFromMazeContent(size, savedLayout);
        this.pastGameOutcomes = parseSavedOutcomes(savedOutcomeHistory);
    }

    // EFFECTS: returns name of maze
    public String getName() {
        return name;
    }

    // EFFECTS: returns name of maze's size
    public String getSizeName() {
        return MazeSizeModel.getSizeName(mazeLayout.getSize());
    }

    // EFFECTS: returns length of maze's sides
    public int getSideLength() {
        return MazeSizeModel.getSideLength(mazeLayout.getSize());
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
    public GridPosition getTreasurePosition() {
        return mazeLayout.getTreasurePosition();
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    public GridPosition getMinotaurStartPosition() {
        return mazeLayout.getMinotaurStartPosition();
    }

    // valid = entity can be there = is PASSAGE
    public boolean isPositionValid(GridPosition position) {
        return mazeLayout.getSquare(position) == MazeLayoutModel.MazeSquare.PASSAGE;
    }

    // EFFECTS: returns true if move follows proper movement rules, false otherwise
    // TODO: document exceptions
    public boolean isMoveValid(GridPosition start, GridPosition end) throws GridPositionOutOfBoundsException {
        boolean samePosition = start.equals(end);
        boolean orthogonal = areSquaresOrthogonal(start, end);
        boolean endOnWall = mazeLayout.getSquare(end) == MazeLayoutModel.MazeSquare.WALL;
        if (samePosition || !orthogonal || endOnWall) {
            return false;
        } else {
            return areAllTheseSquaresTheSame(getSquaresBetween(start, end));
        }
    }

    // EFFECTS: returns list of squares between start and end (exclusive)
    // TODO: document exceptions
    private List<MazeLayoutModel.MazeSquare> getSquaresBetween(GridPosition start, GridPosition end)
            throws GridPositionOutOfBoundsException, IllegalArgumentException {
        // TODO: refactor to use MazeLayout subGrid and iteration
        List<MazeLayoutModel.MazeSquare> betweenList = new ArrayList<>();
        validateGetSquaresBetweenParams(start, end);

        // test if start > end, if so, output needs reversing
        if (end.getX() > start.getX() || end.getY() > start.getY()) { // no reverse needed
            Grid<MazeLayoutModel.MazeSquare> area = mazeLayout.getArea(start, end);
            for (MazeLayoutModel.MazeSquare square : area) {
                betweenList.add(square);
            }
            betweenList.remove(betweenList.size() - 1);
            betweenList.remove(0);
        } else {
            Grid<MazeLayoutModel.MazeSquare> area = mazeLayout.getArea(end, start);
            for (MazeLayoutModel.MazeSquare square : area) {
                betweenList.add(square);
            }
            Collections.reverse(betweenList);
            betweenList.remove(betweenList.size() - 1);
            betweenList.remove(0);
        }

//        int deltaX = end.getX() - start.getX();
//        int deltaY = end.getY() - start.getY();
//        if (deltaX == 0) {
//            int sign = Integer.signum(deltaY);
//            for (int i = start.getY() + sign; Math.abs(i - end.getY()) > 0; i += sign) {
//                betweenList.add(getSquare(new GridPosition(start.getX(), i)));
//            }
//        } else {
//            int sign = Integer.signum(deltaX);
//            for (int i = start.getX() + sign; Math.abs(i - end.getX()) > 0; i += sign) {
//                betweenList.add(getSquare(new GridPosition(i, start.getY())));
//            }
//        }

        return betweenList;
    }

    private void validateGetSquaresBetweenParams(GridPosition start, GridPosition end)
            throws GridPositionOutOfBoundsException, IllegalArgumentException {
        if (!(mazeLayout.inBounds(start) && mazeLayout.inBounds(end))) {
            throw new GridPositionOutOfBoundsException(
                    String.format("Start: %d, %d, End: %d, %d",
                            start.getX(), start.getY(), end.getX(), end.getY()));
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException(
                    String.format("Start and end are same position: %d, %d",
                            start.getX(), start.getY()));
        }
        if (!(start.getX() == end.getX() || start.getY() == end.getY())) {
            throw new IllegalArgumentException(
                    String.format("Start %d, %d > End: %d, %d",
                            start.getX(), start.getY(), end.getX(), end.getY()));
        }
    }

    // EFFECTS: returns list of valid move endpoints from given start position in given direction
    public List<GridPosition> getValidMoves(GridPosition start, Direction direction)
            throws GridPositionOutOfBoundsException {
        List<GridPosition> squaresInDirection = getPositionsInDirection(start, direction);
        List<GridPosition> validMoves = new ArrayList<>();
        for (GridPosition possibleEnd : squaresInDirection) {
            if (isMoveValid(start, possibleEnd)) {
                validMoves.add(possibleEnd);
            }
        }
        return validMoves;
    }

    // EFFECTS: returns grid positions from start to edge of board in given direction
    private List<GridPosition> getPositionsInDirection(GridPosition start, MazeModel.Direction direction)
            throws GridPositionOutOfBoundsException {
        if (!mazeLayout.inBounds(start)) {
            throw new GridPositionOutOfBoundsException(
                    String.format("Start position: %d, %d", start.getX(), start.getY()));
        }
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

    // TODO: document this
    private List<Outcome> parseSavedOutcomes(List<String> savedOutcomeHistory) throws InvalidMazeSaveDataException {
        List<Outcome> outcomes = new ArrayList<>();
        for (String line : savedOutcomeHistory) {
            for (String letter : line.split("")) {
                if (letter.equals(WIN_LETTER)) {
                    outcomes.add(Outcome.WIN);
                } else if (letter.equals(LOSS_LETTER)) {
                    outcomes.add(Outcome.LOSS);
                } else {
                    throw new InvalidMazeSaveDataException("Saved game history has invalid letter");
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
    public Grid<SquareDisplayData> displayMaze() {
        return mazeLayout.display();
    }

    // EFFECTS: returns maze's data in save file format (see Reader)
    public List<String> getSaveData() {
        List<String> saveData = new ArrayList<>();
        saveData.add(name);
        saveData.add(getSizeCode());
        int totalGameOnThisMaze = getTotalPlays();
        saveData.add(Integer.toString(totalGameOnThisMaze));
        for (int i = 0; i * 100 < totalGameOnThisMaze; i++) {
            saveData.add(getPastGameOutcomeSaveData(i));
        }
        saveData.addAll(mazeLayout.getSaveData());
        return saveData;
    }

    private boolean areSquaresOrthogonal(GridPosition start, GridPosition end) {
        return start.getX() == end.getX() || start.getY() == end.getY();
    }

    // EFFECTS: returns true if all squares in list are the same type
    private boolean areAllTheseSquaresTheSame(List<MazeLayoutModel.MazeSquare> squares) {
        return squares.isEmpty() || Collections.frequency(squares, squares.get(0)) == squares.size();
    }

    // EFFECTS: returns maze's size code
    private String getSizeCode() {
        return MazeSizeModel.getSizeCode(mazeLayout.getSize());
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
