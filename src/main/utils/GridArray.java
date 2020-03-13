package utils;

import model.PositionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// capable of storing data in 2D grid format
// coordinate system has origin at top left
// x increases left to right
// y increases top to bottom
public class GridArray<T> {
    private int width;
    private int height;
    private List<T> data;

    // EFFECTS: create GridArray of given dimensions with all null elements
    public GridArray(int width, int height) {
        setDimensions(width, height);
        this.data = makeNullList(width * height);
    }

    // EFFECTS: create square GridArray of given side length with all null elements
    public GridArray(int sideLength) {
        setDimensions(sideLength, sideLength);
        this.data = makeNullList(sideLength * sideLength);
    }

    // REQUIRES: data.size() = width * height
    // EFFECTS: create GridArray of given dimensions with given data
    public GridArray(int width, int height, List<T> data) {
        setDimensions(width, height);
        this.data = data;
    }

    // REQUIRES: data.size() = sideLength * sideLength
    // EFFECTS: create square GridArray of given side length with given data
    public GridArray(int sideLength, List<T> data) {
        setDimensions(sideLength, sideLength);
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // REQUIRES: x and y are in grid bounds
    // EFFECTS: returns element at given x and y coordinates
    public T get(int x, int y) {
        return data.get(coordinatesToListIndex(x, y));
    }

    // REQUIRES: position is in grid bounds
    // EFFECTS: returns element at given position
    public T get(PositionModel position) {
        return get(position.getX(), position.getY());
    }

    public void set(int x, int y, T element) {
        data.set(coordinatesToListIndex(x, y), element);
    }

    public void set(PositionModel position, T element) {
        set(position.getX(), position.getY(), element);
    }

    // EFFECTS: return true if x and y lie in bounds of grid
    public boolean isInBounds(int x, int y) {
        boolean validX = (0 <= x && x < width);
        boolean validY = (0 <= y && y < height);
        return validX && validY;
    }

    // TODO: use this to throw exceptions
    // EFFECTS: return true if position lies in bounds of grid
    public boolean isInBounds(PositionModel position) {
        return isInBounds(position.getX(), position.getY());
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

    // REQUIRES: position is not outside of layout
    // EFFECTS: converts positions to index number for "flattened" 2D grid list of given width
    // Example: if grid width = 5, positionToListIndex(2, 4) -> 14
    private int coordinatesToListIndex(int x, int y) {
        return y * width + x;
    }

    // MODIFIES: this
    // EFFECTS: sets grid array dimensions
    private void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private List<T> makeNullList(int length) {
        List<T> nullList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            nullList.add(null);
        }
        return nullList;
    }

    // REQUIRES: element is not null;
    // EFFECTS: returns true if element is in grid
    public boolean contains(T element) {
        return data.contains(element);
    }

    // REQUIRES: element is not null
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
