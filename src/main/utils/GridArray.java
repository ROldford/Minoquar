package utils;

import model.PositionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// capable of storing data in 2D grid format
public class GridArray<T> {
    // TODO: implement Iterable
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

    public T get(int x, int y) {
        return data.get(coordinatesToListIndex(x, y));
    }

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
}
