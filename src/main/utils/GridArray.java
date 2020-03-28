package utils;

import exceptions.GridOperationOutOfBoundsException;
import model.PositionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// capable of storing data in 2D grid format
// coordinate system has origin at top left
// x increases left to right
// y increases top to bottom
public class GridArray<T> {
    public static final String CONSTRUCTOR_EXCEPTION_MESSAGE = "Data list size does not match grid dimensions";

    private final int width;
    private final int height;
    private List<T> data;

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
    //          throws exception if data.size() != width * height
    public GridArray(int width, int height, List<T> data) throws IllegalArgumentException {
        if (data.size() == width * height) {
            this.width = width;
            this.height = height;
            this.data = data;
        } else {
            throw new IllegalArgumentException(CONSTRUCTOR_EXCEPTION_MESSAGE);
        }
    }

    // EFFECTS: create square GridArray of given side length with given data
    //          throws exception if data.size() != sideLength * sideLength
    public GridArray(int sideLength, List<T> data) throws IllegalArgumentException {
        this(sideLength, sideLength, data);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // EFFECTS: returns element at given x and y coordinates
    //          throws exception if coordinates are out of bounds
    public T get(int x, int y) throws GridOperationOutOfBoundsException {
        if (isInBounds(x, y)) {
            return data.get(coordinatesToListIndex(x, y));
        } else {
            throw new GridOperationOutOfBoundsException(x, y);
        }
    }

    // EFFECTS: returns element at given position
    //          throws exception if position is out of bounds
    public T get(PositionModel position) throws GridOperationOutOfBoundsException {
        if (isInBounds(position)) {
            return get(position.getX(), position.getY());
        } else {
            throw new GridOperationOutOfBoundsException(position);
        }

    }

    // TODO: document method
    public void set(int x, int y, T element) throws GridOperationOutOfBoundsException {
        if (isInBounds(x, y)) {
            data.set(coordinatesToListIndex(x, y), element);
        } else {
            throw new GridOperationOutOfBoundsException(x, y);
        }

    }

    // TODO: document method
    public void set(PositionModel position, T element) throws GridOperationOutOfBoundsException{
        if (isInBounds(position)) {
            set(position.getX(), position.getY(), element);
        } else {
            throw new GridOperationOutOfBoundsException(position);
        }
    }

    // EFFECTS: return true if area lies in bounds of grid
    //          area is out of bounds if origin or end corner are out of bounds
    //          origin = top left, end = bottom right
    public boolean isInBounds(int originX, int originY, int endX, int endY) {
        boolean validOriginX = (0 <= originX && originX < width);
        boolean validOriginY = (0 <= originY && originY < height);
        boolean validEndX = (0 <= endX && endX < width);
        boolean validEndY = (0 <= endY && endY < height);
        return validOriginX && validOriginY && validEndX && validEndY;
    }

    // EFFECTS: return true if position lies in bounds of grid
    public boolean isInBounds(PositionModel start, PositionModel end) {
        return isInBounds(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public boolean isInBounds(int posX, int posY) {
        return isInBounds(posX, posY, posX, posY);
    }

    public boolean isInBounds(PositionModel position) {
        return isInBounds(position, position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GridArray<?> gridArray = (GridArray<?>) o;
        return width == gridArray.width
                && height == gridArray.height
                && Objects.equals(data, gridArray.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, data);
    }

    // EFFECTS: converts positions to index number for "flattened" 2D grid list of given width
    // Example: if grid width = 5, positionToListIndex(2, 4) -> 14
    private int coordinatesToListIndex(int x, int y) {
        return y * width + x;
    }

    private List<T> makeNullList(int length) {
        List<T> nullList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            nullList.add(null);
        }
        return nullList;
    }

    // TODO: this might only be used in tests, see if I can safely remove it
    // REQUIRES: element is not null;
    // EFFECTS: returns true if element is in grid
    public boolean contains(T element) {
        return data.contains(element);
    }

    // EFFECTS: returns position of first occurrence of element in grid, or (-1, -1) if not present
    public PositionModel getPositionOfElement(T element) {
        int index = data.indexOf(element);
        if (index < 0) {
            return new PositionModel(-1, -1);
        } else {
            return new PositionModel(index % width, index / width);
        }
    }
}
