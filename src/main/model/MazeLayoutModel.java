package model;

import exceptions.GridPositionOutOfBoundsException;
import exceptions.InvalidMazeSaveDataException;
import grid.*;
import persistence.Reader;
import ui.SquareDisplayData;
import utils.Utilities;

import java.util.*;

// responsible for creating maze-specific layout (i.e. with static QR elements)
public class MazeLayoutModel implements Iterable<MazeLayoutModel.MazeSquare> {
    public enum MazeSquare {
        WALL,
        PASSAGE,
        EMPTY
    }

    private static final MazeSquare W = MazeSquare.WALL;
    private static final MazeSquare P = MazeSquare.PASSAGE;
    public static final Grid<MazeSquare> FINDER_PATTERN = new GridArray<>(7, 7, new ArrayList<>(Arrays.asList(
            W, W, W, W, W, W, W,
            W, P, P, P, P, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, W, W, W, P, W,
            W, P, P, P, P, P, W,
            W, W, W, W, W, W, W)));
    public static final Grid<MazeSquare> FINDER_MARGIN_VERTICAL = new GridArray<>(1, 8,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P, P)));
    public static final Grid<MazeSquare> FINDER_MARGIN_HORIZONTAL = new GridArray<>(7, 1,
            new ArrayList<>(Arrays.asList(P, P, P, P, P, P, P)));
    public static final Grid<MazeSquare> ALIGNMENT_PATTERN = new GridArray<>(5, 5, new ArrayList<>(Arrays.asList(
            W, W, W, W, W,
            W, P, P, P, W,
            W, P, W, P, W,
            W, P, P, P, W,
            W, W, W, W, W)));
    public static final Grid<MazeSquare> DARK_MODULE = new GridArray<>(1, 1,
            new ArrayList<>(Collections.singletonList(W)));
    public static final double PERCENT_WALL = 0.4;

    private Grid<MazeSquare> grid;
    private MazeSizeModel.MazeSize size;

    // EFFECTS: Construct empty maze layout (only EMPTY squares) of given size
    private MazeLayoutModel(MazeSizeModel.MazeSize mazeSize) {
//        super(MazeSizeModel.getSideLength(mazeSize), MazeSizeModel.getSideLength(mazeSize));
        int sideLength = MazeSizeModel.getSideLength(mazeSize);
        List<MazeSquare> layoutEmpty = new ArrayList<>();
        for (int i = 0; i < (sideLength * sideLength); i++) {
            layoutEmpty.add(i, MazeSquare.EMPTY);
        }
        this.grid = new GridArray<>(sideLength, sideLength, layoutEmpty);
        this.size = mazeSize;
    }

    // MODIFIES: this
    // EFFECTS: create random maze layout of given size
    // TODO: document IncompleteMazeException
    public static MazeLayoutModel createRandomMaze(MazeSizeModel.MazeSize size) {
        MazeLayoutModel randomMaze = new MazeLayoutModel(size);
        randomMaze.addQRCodeElements();
        randomMaze.fillRemainingSquares();
        return randomMaze;
    }

    // MODIFIES: this
    // EFFECTS: create maze layout of given size from maze content list
    //          TODO: document Exceptions
    public static MazeLayoutModel createMazeFromMazeContent(MazeSizeModel.MazeSize size, List<String> savedLayout)
            throws InvalidMazeSaveDataException {
        int sideLength = MazeSizeModel.getSideLength(size);
        MazeLayoutModel newMaze = new MazeLayoutModel(size);
        List<MazeSquare> parsedLayoutData = parseSavedLayout(savedLayout);
        Grid<MazeSquare> mazeLayout = null;
        try {
            mazeLayout = new GridArray<>(sideLength, sideLength, parsedLayoutData);
        } catch (IllegalArgumentException e) {
            throw new InvalidMazeSaveDataException(e.getMessage(), e);
        }
        newMaze.overwrite(new GridPosition(0, 0), mazeLayout);
        return newMaze;
    }

    // EFFECTS: returns this maze layout's size
    public MazeSizeModel.MazeSize getSize() {
        return size;
    }

    public int getSideLength() {
        return MazeSizeModel.getSideLength(size);
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

    // EFFECTS: return true if position lies in bounds of layout
    public boolean inBounds(GridPosition position) {
        return grid.inBounds(position);
    }

    // EFFECTS: returns status of square at given position
    // TODO: document exceptions from Grid
    public MazeSquare getSquare(GridPosition position) {
        return grid.get(position);
    }

    // TODO: document exceptions
    public Grid<MazeSquare> getArea(GridPosition start, GridPosition end) {
        Grid<MazeSquare> subGrid = getSubGrid(start, end);
        // not sharing subGrid directly to limit access to underlying implementation
        List<MazeSquare> subGridList = new ArrayList<>();
        for (MazeSquare square : subGrid) {
            subGridList.add(square);
        }
        return new GridArray<>(subGrid.getWidth(), subGrid.getHeight(), subGridList);
    }

    // EFFECTS: returns maze layout's data in save file format (see Reader)
    // TODO: document exceptions
    public List<String> getSaveData() {
        List<String> saveData = new ArrayList<>();
        GridSeriesIterator<MazeSquare> gridRowIterator = grid.gridRowIterator();
        while (gridRowIterator.hasNext()) {
            List<MazeSquare> gridRow = gridRowIterator.next();
            StringBuilder saveDataRow = new StringBuilder();
            for (MazeSquare gridSquare : gridRow) {
                if (gridSquare == MazeSquare.WALL) {
                    saveDataRow.append(Reader.SAVE_FILE_WALL);
                } else {
                    saveDataRow.append(Reader.SAVE_FILE_PASSAGE);
                }
            }
            saveData.add(saveDataRow.toString());
        }
//        for (int y = 0; y < MazeSizeModel.getSideLength(size); y++) {
//            StringBuilder row = new StringBuilder();
//            for (int x = 0; x < MazeSizeModel.getSideLength(size); x++) {
//                MazeSquare square = null;
//                try {
//                    square = getSquare(new GridPosition(x, y));
//                } catch (GridPositionOutOfBoundsException e) {
//                    throw new IncorrectGridIterationException(x, y);
//                }
//                if (square == MazeSquare.WALL) {
//                    row.append(Reader.SAVE_FILE_WALL);
//                } else  {
//                    row.append(Reader.SAVE_FILE_PASSAGE);
//                }
//            }
//            saveData.add(row.toString());
//        }
        return saveData;
    }

    // EFFECTS: return GridArray of SquareDisplayData to display the current layout
    public Grid<SquareDisplayData> display() {
        Grid<SquareDisplayData> displayData = new GridArray<>(getSideLength(), getSideLength());
        GridIterator<MazeSquare> gridIterator = grid.gridCellIterator();
        GridIterator<SquareDisplayData> displayIterator = displayData.gridCellIterator();
        while (gridIterator.hasNext()) {
            MazeSquare gridSquare = gridIterator.next();
//            SquareDisplayData displaySquare = displayIterator.next();
//            displaySquare = new SquareDisplayData(gridSquare);
            displayIterator.next();
            displayIterator.set(new SquareDisplayData(gridSquare));
        }
//        GridArray<SquareDisplayData> displayData = new GridArray<>(getWidth(), getHeight());
//        for (int x = 0; x < getWidth(); x++) {
//            for (int y = 0; y < getHeight(); y++) {
//                GridPosition position = new GridPosition(x, y);
//                SquareDisplayData squareDisplay = null;
//                try {
//                    squareDisplay = new SquareDisplayData(grid.get(position), new ArrayList<>());
//                    displayData.set(x, y, squareDisplay);
//                } catch (GridPositionOutOfBoundsException e) {
//                    throw new IncorrectGridIterationException(position);
//                }
//            }
//        }
        return displayData;
    }

    // MODIFIES: this
    // EFFECTS: adds static QR code elements to maze layout (finder/alignment/timing patterns)
    // TODO: document exceptions
    private void addQRCodeElements() {
        addFinderPatterns();
        addFinderMargins();
        addAlignmentPattern();
        addTimingPatterns();
        addDarkModule();
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at top-left, top-right, and bottom-left corners
    // TODO: document exceptions
    private void addFinderPatterns() {
        List<GridPosition> finderStartPositions = MazeSizeModel.getFinderPatternPositions(size);
        for (GridPosition position : finderStartPositions) {
            addFinderPattern(position);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds finder patterns to maze layout at given position (starting at top-left corner)
    private void addFinderPattern(GridPosition corner) {
        overwrite(corner, FINDER_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds margins around finder patterns in maze layout
    // TODO: document exceptions
    private void addFinderMargins() {
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
    private void addAlignmentPattern() {
        overwrite(MazeSizeModel.getAlignPatternPosition(size), ALIGNMENT_PATTERN);
    }

    // MODIFIES: this
    // EFFECTS: adds timing patterns to maze layout between finder patterns
    // TODO: document exceptions
    private void addTimingPatterns() {
        int length = MazeSizeModel.getTimingPatternLength(size);
        List<GridPosition> positions = MazeSizeModel.getTimingPatternPositions();
        // build preset for timing pattern
        List<MazeSquare> pattern = buildTimingPattern(length);
        overwrite(positions.get(0), new GridArray<>(1, length, pattern));
        overwrite(positions.get(1), new GridArray<>(length, 1, pattern));
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
    private void addDarkModule() {
        overwrite(MazeSizeModel.getDarkModulePosition(size), DARK_MODULE);
    }

    // MODIFIES: this
    // EFFECTS: overwrites source layout on top of squares on this layout
    // TODO: document GridPositionOutOfBoundsException
    public void overwrite(GridPosition overwriteStart, Grid<MazeSquare> source) {
        // subgrid setup
        GridPosition overwriteEnd = overwriteStart.add(
                new GridPosition(source.getWidth() - 1, source.getHeight() - 1));
        Grid<MazeSquare> target = getSubGrid(overwriteStart, overwriteEnd);
        // iteration over source and target
        GridIterator<MazeSquare> targetIterator = target.gridCellIterator();
        GridIterator<MazeSquare> sourceIterator = source.gridCellIterator();
        while (sourceIterator.hasNext()) {
            MazeSquare sourceSquare = sourceIterator.next();
//            MazeSquare targetSquare = targetIterator.next();
            targetIterator.next();
            targetIterator.set(sourceSquare);
        }


//        if (isInBounds(overwriteStart, overwriteEnd)) {
//            for (int x = 0; x < other.getWidth(); x++) {
//                for (int y = 0; y < other.getHeight(); y++) {
//                    try {
//                        grid.set(
//                                overwriteStart.add(new GridPosition(x, y)),
//                                other.getSquare(new GridPosition(x, y)));
//                    } catch (GridPositionOutOfBoundsException e) {
//                        throw new IncorrectGridIterationException(x, y);
//                    }
//                }
//            }
//        } else {
//            throw new GridPositionOutOfBoundsException("out of bounds");
//        }
    }

    private Grid<MazeSquare> getSubGrid(GridPosition start, GridPosition end) {
        return grid.subGrid(start, end);
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

    public Iterator<MazeSquare> iterator() {
        return grid.iterator();
    }

    public GridIterator<MazeSquare> gridCellIterator() {
        return grid.gridCellIterator();
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
                    throw new InvalidMazeSaveDataException(String.format("Invalid character: %s", ch));
                }
            }
        }
        return layoutData;
    }
}
