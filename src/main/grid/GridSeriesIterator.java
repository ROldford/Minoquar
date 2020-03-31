package grid;

import java.util.Iterator;
import java.util.List;

public interface GridSeriesIterator<T> extends Iterator<List<T>> {
    // iterates grid by series (row by default)
    // has cursor, can iterate forwards and backwards

    boolean hasNext();

    List<T> next();

//    boolean hasPrevious();
//
//    List<T> previous();

    int nextIndex();

    int previousIndex();
}
