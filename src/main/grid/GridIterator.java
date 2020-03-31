package grid;

import java.util.Iterator;

// TODO: document
public interface GridIterator<T> extends Iterator<T> {
    // iterates Grid without positional data
    // use GridSeriesIterator if positional data matters

    boolean hasNext();

    T next();

    // replaces last element returned by next()
    // must call next() first for this to work
    void set(T element);

}
