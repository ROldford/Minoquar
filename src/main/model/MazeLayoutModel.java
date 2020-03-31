package model;

import exceptions.*;
import grid.GridIterator;
import grid.GridPosition;
import persistence.Reader;
import utils.Utilities;

import java.util.*;

// responsible for creating maze-specific layout (i.e. with static QR elements)
public class MazeLayoutModel extends Layout {
    private static final MazeSquare W = MazeSquare.WALL;
    private static final MazeSquare P = MazeSquare.PASSAGE;
    public static final Layout FINDER_PATTERN = new Layout(7, 7, new ArrayList<>(Arrays.asList(
            W, W, W, W, W, W, W,
            W, P, P, P, P, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, P, P, P, P, W,
            W, W, W, W, W, W, W)));
    public static final Layout FINDER_MARGIN_VERTICAL = new Layout(1, 8,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P, P)));
    public static final Layout FINDER_MARGIN_HORIZONTAL = new Layout(7, 1,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P)));
    public static final Layout ALIGNMENT_PATTERN = new Layout(5, 5, new ArrayList<>(Arrays.asList(
            W, W, W, W, W,
            W, P, P, P, W,
            W, P, W, P, W,
            W, P, P, P, W,
            W, W, W, W, W)));
    public static final Layout DARK_MODULE = new Layout(1, 1,
            new ArrayList<>(Collections.singletonList(W)));
    public static final double PERCENT_WALL = 0.4;

    private MazeSizeModel.MazeSize size;

    // EFFECTS: Construct empty maze layout (only EMPTY squares) of given size
    private MazeLayoutModel(MazeSizeModel.MazeSize mazeSize) {
        super(MazeSizeModel.getSideLength(mazeSize), MazeSizeModel.getSideLength(mazeSize));
        this.size = mazeSize;
    }

    // MODIFIES: this
    // EFFECTS: create random maze layout of given size
    // TODO: document exceptions (should not normally be thrown)
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize size)
            throws GridPositionOutOfBoundsException, IncorrectGridIterationException, IncompleteMazeException {
        MazeLayoutModel randomMaze = new MazeLayoutModel(size);
        randomMaze.addQRCodeElements();
        randomMaze.fillRemainingSquares();
        if (isMazeComplete(randomMaze)) {
            return randomMaze;
        } else {
            throw new IncompleteMazeException("Maze has empty squares");
        }
    }

    private static boolean isMazeComplete(MazeLayoutModel randomMaze) throws IncorrectGridIterationException {
        int sideLength = randomMaze.getWidth();
        if (sideLength != randomMaze.getHeight()) {  // ensuring only square mazes
            return false;
        } else {
            for (int x = 0; x < sideLength; x++) {
                for (int y = 0; y < sideLength; y++) {
                    GridPosition position = new GridPosition(x, y);
                    try {
                        if (randomMaze.getSquare(position) == MazeSquare.EMPTY) {
                            return false;  // EMPTY squares are illegal
                        }
                    } catch (GridPositionOutOfBoundsException e) {
                        throw new IncorrectGridIterationException(position);
                    }
                }
            }
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: create maze layout of given size from maze content list
    //          TODO: document Exceptions
    public static MazeLayoutModel createMazeFromMazeContent(MazeSizeModel.MazeSize size, List<String> savedLayout)
            throws IncorrectGridIterationException,
            GridPositionOutOfBoundsException,
            IncompleteMazeException,
            InvalidMazeSaveDataException {
        int sideLength = MazeSizeModel.getSideLength(size);
        MazeLayoutModel newMaze = new MazeLayoutModel(size);
        List<MazeSquare> parsedLayoutData = parseSavedLayout(savedLayout);
        Layout mazeLayout = new Layout(sideLength, sideLength, parsedLayoutData);
        newMaze.overwrite(new GridPosition(0, 0), mazeLayout);
        if (isMazeComplete(newMaze)) {
            return newMaze;
        } else {
            throw new IncompleteMazeException("Maze has empty squares");
        }
    }

    // EFFECTS: returns this maze layout's size
    public MazeSizeModel.MazeSize getSize() {
        return size;
    }

    // EFFECTS: returns position of treasure in maze layout
    //          located in top right corner passage of alignment pattern
    public GridPosition getTreasurePosition() {
        return MazeSizeModel.getTreasurePosition(size);
    }

    // EFFECTS: gets minotaur start position (PASSAGE square closest to center)
    //          uses breadth first search, starting from middle square
    //          returns position of (-1, -1) if no valid start can be found
    //          (This should NEVER happen in a real maze!)
    public GridPosition getMinotaurStartPosition() {
        Queue<GridPosition> todo = new LinkedList<>();
        todo.add(new GridPosition(MazeSizeModel.getSideLength(size) / 2, MazeSizeModel.getSideLength(size) / 2));
        List<GridPosition> done = new ArrayList<>();
        while (todo.size() > 0) {
            GridPosition checking = todo.remove();
            try {  // shouldn't check out of bounds
                if (getSquare(checking) == MazeSquare.PASSAGE) {
                    return checking;
                } else {
                    done.add(checking);
                    // this should never return out of bounds
                    List<GridPosition> neighbors = findNeighbourPositions(checking);
                    for (GridPosition position : neighbors) {
                        if (!todo.contains(position) && !done.contains(position)) {
                            todo.add(position);
                        }
                    }
                }
            } catch (GridPositionOutOfBoundsException e) {  // skipping out of bounds should be safe, but log anyway
                System.out.printf("Skipped out of bounds position: %d, %d", checking.getX(), checking.getY());
                e.printStackTrace();
            }
        }
        return new GridPosition(-1, -1);
    }

    // EFFECTS: returns all valid orthogonal neighbors of the given position
    private List<GridPosition> findNeighbourPositions(GridPosition center) {
        List<GridPosition> neighbours = new ArrayList<>();
        List<GridPosition> offsets = new ArrayList<>(Arrays.asList(
                new GridPosition(0, -1),
                new GridPosition(0, 1),
                new GridPosition(-1, 0),
                new GridPosition(1, 0)));
        for (GridPosition offset : offsets) {
            GridPosition neighbour = center.add(offset);
            if (inBounds(neighbour)) {
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

    // EFFECTS: returns maze layout's data in save file format (see Reader)
    // TODO: document exceptions
    public List<String> getSaveData() throws IncorrectGridIterationException {
        List<String> saveData = new ArrayList<>();
        for (int y = 0; y < MazeSizeModel.getSideLength(size); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < MazeSizeModel.getSideLength(size); x++) {
                MazeSquare square = null;
                try {
                    square = getSquare(new GridPosition(x, y));
                } catch (GridPositionOutOfBoundsException e) {
                    throw new IncorrectGridIterationException(x, y);
                }
                if (square == MazeSquare.WALL) {
                    row.append(Reader.SAVE_FILE_WALL);
                } else  {
                    row.append(Reader.SAVE_FILE_PASSAGE);
                }
            }
            saveData.add(row.toString());
        }
        return saveData;
    }

    // MODIFIES: this
    // EFFECTS: adds static QR code elements to maze layout (finder/alignment/timing patterns)
    // TODO: document exceptions
    private void addQRCodeElements() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        addFinderPatterns();
        addFinderMargins();
        addAlignmentPattern();
        addTimingPatterns();
        addDarkModule();
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at top-left, top-right, and bottom-left corners
    // TODO: document exceptions
    private void addFinderPatterns() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        List<GridPosition> finderStartPositions = MazeSizeModel.getFinderPatternPositions(size);
        for (GridPosition position : finderStartPositions) {
            addFinderPattern(position);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at given position (starting at top-left corner)
    private void addFinderPattern(GridPosition corner)
            throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        overwrite(corner, FINDER_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds margins around finder patterns in maze layout
    // TODO: document exceptions
    private void addFinderMargins() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        List<GridPosition> marginStartPositions = MazeSizeModel.getFinderMarginPositions(size);
        for (int i = 0; i < marginStartPositions.size(); i++) {
            // even index is vertical margin, odd is horizontal margin
            if (Utilities.isEven(i)) {
                overwrite(marginStartPositions.get(i), FINDER_MARGIN_VERTICAL);
            } else {
                overwrite(marginStartPositions.get(i), FINDER_MARGIN_HORIZONTAL);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds alignment pattern to maze layout at appropriate location for maze size
    // TODO: document exceptions
    private void addAlignmentPattern() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        overwrite(MazeSizeModel.getAlignPatternPosition(size), ALIGNMENT_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds timing patterns to maze layout between finder patterns
    // TODO: document exceptions
    private void addTimingPatterns() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        int length = MazeSizeModel.getTimingPatternLength(size);
        List<GridPosition> positions = MazeSizeModel.getTimingPatternPositions();
        // build preset for timing pattern
        List<MazeSquare> pattern = buildTimingPattern(length);
        overwrite(positions.get(0), new Layout(1, length, pattern));
        overwrite(positions.get(1), new Layout(length, 1, pattern));
    }

    // REQUIRES: length is odd
    // EFFECTS: returns list of squares matching QR code timing patterns
    //          alternating between wall and passage, starts and ends with wall
    private List<MazeSquare> buildTimingPattern(int length) {
        List<MazeSquare> pattern = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (Utilities.isEven(i)) {
                pattern.add(MazeSquare.WALL);
            } else {
                pattern.add(MazeSquare.PASSAGE);
            }
        }
        return pattern;
    }

    // MODIFIES: this
    // EFFECTS: adds one "dark square" (WALL) to maze layout near bottom-left finder pattern
    // TODO: document exceptions
    private void addDarkModule() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        overwrite(MazeSizeModel.getDarkModulePosition(size), DARK_MODULE);
    }

    // MODIFIES: this
    // EFFECTS: fills remaining EMPTY squares in maze layout with WALL or PASSAGE
    //          random chance of either, based on PERCENT_WALL
    // TODO: document exceptions
    private void fillRemainingSquares() {
        GridIterator<MazeSquare> gridIterator = grid.gridCellIterator();
        while (gridIterator.hasNext()) {
            MazeSquare mazeSquare = gridIterator.next();
            if (mazeSquare == MazeSquare.EMPTY) {
                if (Math.random() < PERCENT_WALL) {
                    gridIterator.set(MazeSquare.WALL);
                } else {
                    gridIterator.set(MazeSquare.PASSAGE);
                }
            }
        }
//        for (int x = 0; x < grid.getWidth(); x++) {
//            for (int y = 0; y < grid.getHeight(); y++) {
//                if (grid.get(x, y) == MazeSquare.EMPTY) {
//                    if (Math.random() < PERCENT_WALL) {
//                        grid.set(x, y, MazeSquare.WALL);
//                    } else {
//                        grid.set(x, y, MazeSquare.PASSAGE);
//                    }
//                }
//            }
//        }
    }

    // REQUIRES: layout only consists of SAVE_FILE_WALL and SAVE_FILE_PASSAGE
    // EFFECTS: converts saved layout (in list of string format) to maze layout format
    // TODO: document exceptions
    private static List<MazeSquare> parseSavedLayout(List<String> savedLayout) throws InvalidMazeSaveDataException {
        List<MazeSquare> layoutData = new ArrayList<>();
        for (String row : savedLayout) {
            for (char ch : row.toCharArray()) {
                if (ch == Reader.SAVE_FILE_WALL) {
                    layoutData.add(MazeSquare.WALL);
                } else if (ch == Reader.SAVE_FILE_PASSAGE) {
                    layoutData.add(MazeSquare.PASSAGE);
                } else {
                    throw new InvalidMazeSaveDataException(ch);
                }
            }
        }
        return layoutData;
    }
}
