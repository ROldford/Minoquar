package grid;

import exceptions.GridPositionOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// capable of storing data in 2D grid format
// coordinate system has origin at top left
// x increases left to right
// y increases top to bottom
public class GridArray<T> extends AbstractGrid<T> {
    private final int width;
    private final int height;
    private List<T> data;


    // Constructors

    // EFFECTS: create GridArray of given dimensions with all null elements
    public GridArray(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = makeNullList(width * height);
    }

    // EFFECTS: create square GridArray of given side length with all null elements
    public GridArray(int sideLength) {
        this(sideLength, sideLength);
    }

    // EFFECTS: create GridArray of given dimensions with given data
    //          throws exception if data.size() != width * height  TODO: test for exception throw
    public GridArray(int width, int height, List<T> data) throws IllegalArgumentException {
        if (data.size() == width * height) {
            this.width = width;
            this.height = height;
            this.data = data;
        } else {
            throw new IllegalArgumentException(
                    String.format("For data list size %d in %dx%d grid", data.size(), width, height));
        }
    }

    // EFFECTS: create square GridArray of given side length with given data
    //          throws exception if data.size() != sideLength * sideLength
    public GridArray(int sideLength, List<T> data) throws IllegalArgumentException { // TODO: test for exception throw?
        this(sideLength, sideLength, data);
    }


    // Query Operations

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // TODO: this might only be used in tests, see if I can safely remove it
    // REQUIRES: element is not null;
    // EFFECTS: returns true if element is in grid
    public boolean contains(T element) {
        return data.contains(element);
    }


    // Positional Access Operations

//    // EFFECTS: returns element at given x and y coordinates
//    //          throws exception if coordinates are out of bounds
//    public T get(int x, int y) throws GridPositionOutOfBoundsException {
////        if (isInBounds(x, y)) {
////            return data.get(coordinatesToListIndex(x, y));
////        } else {
////            throw new GridPositionOutOfBoundsException(outOfBoundsMessage(x, y));
////        }
//        return get(new GridPosition(x, y));
//    }

    // EFFECTS: returns element at given position
    //          throws exception if position is out of bounds
    public T get(GridPosition position) throws GridPositionOutOfBoundsException {
//        if (isInBounds(position)) {
//            return get(position.getX(), position.getY());
//        } else {
//            throw new GridPositionOutOfBoundsException(outOfBoundsMessage(position));
//        }
        boundsCheck(position);
        return data.get(positionToListIndex(position));
    }

//    // TODO: document method
//    public void set(int x, int y, T element) throws GridPositionOutOfBoundsException {
////        if (isInBounds(x, y)) {
////            data.set(coordinatesToListIndex(x, y), element);
////        } else {
////            throw new GridPositionOutOfBoundsException(outOfBoundsMessage(x, y));
////        }
//        set(new GridPosition(x, y), element);
//    }

    // TODO: document method
    public void set(GridPosition position, T element) throws GridPositionOutOfBoundsException {
//        if (isInBounds(position)) {
//            set(position.getX(), position.getY(), element);
//        } else {
//            throw new GridPositionOutOfBoundsException(outOfBoundsMessage(position));
//        }
        boundsCheck(position);
        data.set(positionToListIndex(position), element);
    }

    public List<T> getRow(int rowIndex) {
        int rowStart = positionToListIndex(new GridPosition(0, rowIndex));
        int rowEnd = positionToListIndex(new GridPosition(getWidth() - 1, rowIndex));
        List<T> rowView = data.subList(rowStart, rowEnd + 1);
        return new ArrayList<>(rowView);  // making new ArrayList so removals have no effect
    }


    // Search Operations

    // EFFECTS: returns position of first occurrence of element in grid, or (-1, -1) if not present
    @Override
    public GridPosition positionOf(T element) {
        int index = data.indexOf(element);
        if (index < 0) {
            return new GridPosition(-1, -1);
        } else {
            return new GridPosition(index % width, index / width);
        }
    }


    // View

//    // TODO: document method?
//    public GridArray<T> subGrid(GridPosition start, GridPosition end)
//            throws GridPositionOutOfBoundsException, IllegalArgumentException {
//        return null; //stub
//    }

    // Helpers

    // EFFECTS: converts positions to index number for "flattened" 2D grid list of given width
    // Example: if grid width = 5, positionToListIndex(2, 4) -> 14
    private int positionToListIndex(GridPosition position) {
        int x = position.getX();
        int y = position.getY();
        return y * width + x;
    }

    private List<T> makeNullList(int length) {
        List<T> nullList = new ArrayList<>();
        // TODO: find a better way to do this (iterator? some repeat list method in Collections?)
        for (int i = 0; i < length; i++) {
            nullList.add(null);
        }
        return nullList;
    }

//    // EFFECTS: return true if area lies in bounds of grid
//    //          area is out of bounds if origin or end corner are out of bounds
//    //          origin = top left, end = bottom right
//    public boolean isInBounds(int originX, int originY, int endX, int endY) {
//        boolean validOriginX = (0 <= originX && originX < width);
//        boolean validOriginY = (0 <= originY && originY < height);
//        boolean validEndX = (0 <= endX && endX < width);
//        boolean validEndY = (0 <= endY && endY < height);
//        return validOriginX && validOriginY && validEndX && validEndY;
//    }
//
//    // EFFECTS: return true if position lies in bounds of grid
//    public boolean isInBounds(GridPosition start, GridPosition end) {
//        return isInBounds(start.getX(), start.getY(), end.getX(), end.getY());
//    }
//
//    public boolean isInBounds(int posX, int posY) {
//        return isInBounds(posX, posY, posX, posY);
//    }
//
//    public boolean isInBounds(GridPosition position) {
//        return isInBounds(position, position);
//    }
//
//    // TODO: document
//    private String outOfBoundsMessage(int posX, int posY) {
//        return String.format(
//                "Position: (%d, %d), Bounds: (0, 0) to (%d, %d)",
//                posX,
//                posY,
//                width - 1,
//                height - 1);
//    }
//
//    // TODO: document
//    private String outOfBoundsMessage(GridPosition position) {
//        return outOfBoundsMessage(position.getX(), position.getY());
//    }
//
//    // TODO: document
//    private String outOfBoundsMessage(int startX, int startY, int endX, int endY) {
//        return String.format(
//                "Area: (%d, %d) to (%d, %d), Bounds: (0, 0) to (%d, %d)",
//                startX,
//                startY,
//                endX,
//                endY,
//                width - 1,
//                height - 1);
//    }
//
//    // TODO: document
//    private String outOfBoundsMessage(GridPosition start, GridPosition end) {
//        return outOfBoundsMessage(start.getX(), start.getY(), end.getX(), end.getY());
//    }

//    private class SubGridArray<T> extends GridArray<T> {
//
//    }
}
