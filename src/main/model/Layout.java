package model;

import exceptions.GridOperationOutOfBoundsException;
import ui.SquareDisplayData;
import utils.GridArray;

import java.util.ArrayList;
import java.util.List;

// stores grid layout of given dimensions, can return state of square, and add other layouts on top of itself
public class Layout {

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
    GridArray<MazeSquare> layout;

    // EFFECTS: construct grid layout of given width and height with all squares empty
    public Layout(int width, int height) {
//        this.width = width;
//        this.height = height;
        List<MazeSquare> layoutEmpty = new ArrayList<>();
        for (int i = 0; i < (width * height); i++) {
            layoutEmpty.add(i, MazeSquare.EMPTY);
        }
        this.layout = new GridArray<>(width, height, layoutEmpty);
    }

    // EFFECTS: construct grid layout of given width and height using squares from preset layout
    public Layout(int width, int height, List<MazeSquare> presetLayout) {
//        this.width = width;
//        this.height = height;
        this.layout = new GridArray<>(width, height, presetLayout);
    }

    // EFFECTS: returns status of square at given position
    public MazeSquare getSquare(PositionModel position) throws GridOperationOutOfBoundsException {
        return layout.get(position);
    }

    // EFFECTS: return width of maze
    public int getWidth() {
        return layout.getWidth();
    }

    // EFFECTS: return width of maze
    public int getHeight() {
        return layout.getHeight();
    }

    // EFFECTS: return GridArray of SquareDisplayData to display the current layout
    public GridArray<SquareDisplayData> display() throws GridOperationOutOfBoundsException {
        GridArray<SquareDisplayData> displayData = new GridArray<>(getWidth(), getHeight());
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                PositionModel position = new PositionModel(x, y);
                SquareDisplayData squareDisplay = new SquareDisplayData(layout.get(position), new ArrayList<>());
                displayData.set(x, y, squareDisplay);
            }
        }
        return displayData;
    }

    // MODIFIES: this
    // EFFECTS: overwrites other layout on top of squares on this layout
    public void overwrite(PositionModel overwriteStart, Layout other) throws GridOperationOutOfBoundsException {
        PositionModel overwriteEnd = overwriteStart.add(
                new PositionModel(other.getWidth() - 1, other.getHeight() - 1));
        if (isInBounds(overwriteStart, overwriteEnd)) {
            for (int x = 0; x < other.getWidth(); x++) {
                for (int y = 0; y < other.getHeight(); y++) {
                    layout.set(
                            overwriteStart.add(new PositionModel(x, y)),
                            other.getSquare(new PositionModel(x, y)));
                }
            }
        } else {
            throw new GridOperationOutOfBoundsException(overwriteStart, overwriteEnd);
        }

    }

    // EFFECTS: return true if position lies in bounds of layout
    public boolean isInBounds(PositionModel position) {
        return layout.isInBounds(position);
    }

    // EFFECTS: return true if area lies in bounds of layout
    //          start = NW corner, end = SE corner
    public boolean isInBounds(PositionModel start, PositionModel end) {
        return layout.isInBounds(start, end);
    }
}
