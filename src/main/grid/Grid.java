package grid;

import java.util.Iterator;
import java.util.List;

public interface Grid<T> extends Iterable<T> {
    // TODO: document all this!

    // Query Operations

    int getWidth();

    int getHeight();

    boolean contains(T element);

    boolean inBounds(GridPosition position);

    Iterator<T> iterator();


    // Comparison and hashing

    // equals is true if:
    //      also a grid
    //      same dimensions
    //      elements at same positions are equal
    boolean equals(Object o);

    // hash code defined as result of following calculation
        //  int hashCode = 1;
        //  while (hasNextRow()) {
        //      List<T> row = nextRow();
        //      hashCode = 31*hashCode + (t==null ? 0 : row.hashCode());
        //  }
    int hashCode();


    // Positional Access Operations

    T get(GridPosition position);

    void set(GridPosition position, T element);


    // Search Operations

    GridPosition positionOf(T element);


    // Iterators

    GridIterator<T> gridCellIterator();

    GridSeriesIterator<T> gridRowIterator();

    // View

    Grid<T> subGrid(GridPosition start, GridPosition end);
}
