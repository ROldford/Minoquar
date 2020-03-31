package model;

import grid.Grid;
import grid.GridIterator;
import grid.GridPosition;
import ui.SquareDisplayData;
import grid.GridArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// stores grid layout of given dimensions, can return state of square, and add other layouts on top of itself
public class Layout implements Iterable<Layout.MazeSquare> {

    public enum MazeSquare {
        WALL,
        PASSAGE,
        EMPTY
    }

//    public static final char WALL_CHAR = "▓".charAt(0);
//    public static final char PASSAGE_CHAR = " ".charAt(0);
//    public static final char EMPTY_CHAR = "X".charAt(0);

//    int width;
//    int height;
    Grid<MazeSquare> grid;

    // EFFECTS: construct grid layout of given width and height with all squares empty
    //          TODO: document IllegalGridDataSizeException
    public Layout(int width, int height) {
//        this.width = width;
//        this.height = height;
        List<MazeSquare> layoutEmpty = new ArrayList<>();
        for (int i = 0; i < (width * height); i++) {
            layoutEmpty.add(i, MazeSquare.EMPTY);
        }
        this.grid = new GridArray<>(width, height, layoutEmpty);
    }

    // EFFECTS: construct grid layout of given width and height using squares from preset layout
    //          TODO: document IllegalGridDataSizeException
    public Layout(int width, int height, List<MazeSquare> presetLayout) {
//        this.width = width;
//        this.height = height;
        this.grid = new GridArray<>(width, height, presetLayout);
    }

    // EFFECTS: returns status of square at given position
    // TODO: document exceptions from Grid
    public MazeSquare getSquare(GridPosition position) {
        return grid.get(position);
    }

    // EFFECTS: return width of maze
    public int getWidth() {
        return grid.getWidth();
    }

    // EFFECTS: return width of maze
    public int getHeight() {
        return grid.getHeight();
    }

    // EFFECTS: return true if position lies in bounds of layout
    public boolean inBounds(GridPosition position) {
        return grid.inBounds(position);
    }

    // EFFECTS: return GridArray of SquareDisplayData to display the current layout
    public Grid<SquareDisplayData> display() {
        Grid<SquareDisplayData> displayData = new GridArray<>(getWidth(), getHeight());
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
    // EFFECTS: overwrites source layout on top of squares on this layout
    // TODO: rewrite to use GridArray subGrid (like List's subList)
    public void overwrite(GridPosition overwriteStart, Layout source) {
        // subgrid setup
        GridPosition overwriteEnd = overwriteStart.add(
                new GridPosition(source.getWidth() - 1, source.getHeight() - 1));
        Grid<MazeSquare> target = subLayout(overwriteStart, overwriteEnd);
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

    private Grid<MazeSquare> subLayout(GridPosition start, GridPosition end) {
        return grid.subGrid(start, end);
    }

    public Iterator<MazeSquare> iterator() {
        return grid.iterator();
    }

    public GridIterator<MazeSquare> gridCellIterator() {
        return grid.gridCellIterator();
    }

//    // EFFECTS: return true if area lies in bounds of layout
//    //          start = NW corner, end = SE corner
//    public boolean isInBounds(GridPosition start, GridPosition end) {
//        return grid.isInBounds(start, end);
//    }
}
