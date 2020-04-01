package grid;

import exceptions.GridPositionOutOfBoundsException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractGrid<T> implements Grid<T> {

    protected AbstractGrid() {
    }

    // Query Operations

    public abstract int getWidth();

    public abstract int getHeight();

    public boolean contains(T element) {
        Iterator<T> iterator = iterator();
        if (element == null) {
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    return true;
                }
            }
        } else {
            while (iterator.hasNext()) {
                if (element.equals(iterator.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inBounds(GridPosition position) {
        boolean inBoundsX = position.getX() >= 0 && position.getX() < getWidth();
        boolean inBoundsY = position.getY() >= 0 && position.getY() < getHeight();
        return inBoundsX && inBoundsY;
    }


    // Comparison and hashing

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Grid)) {
            return false;
        }
        GridSeriesIterator<T> myGSI = gridRowIterator();
        GridSeriesIterator<?> otherGSI = ((Grid<?>) o).gridRowIterator();
        while (myGSI.hasNext() && otherGSI.hasNext()) {
            List<T> myRow = myGSI.next();
            List<?> otherRow = otherGSI.next();
            if (myRow == null) {
                if (otherRow != null) {
                    return false;
                }
            } else {
                if (!myRow.equals(otherRow)) {
                    return false;
                }
            }
        }
        return !(myGSI.hasNext() || otherGSI.hasNext());  // both grids end at same number of rows
    }

    public int hashCode() {
        int hashCode = 1;
        GridSeriesIterator<T> iterator = gridRowIterator();
        while (iterator.hasNext()) {
            List<T> row = iterator.next();
            hashCode = 31 * hashCode + (row == null ? 0 : row.hashCode());
        }
        return hashCode;
    }


    // Positional Access Operations

    public abstract T get(GridPosition position) throws GridPositionOutOfBoundsException;

    public void set(GridPosition position, T element) throws GridPositionOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    // implementation must not allow manipulation of returned list to change size of grid row!
    // TODO: change this from abstract, move implementation out of SubGrid.getRow() to here
    public abstract List<T> getRow(int rowIndex) throws GridPositionOutOfBoundsException;
    // future development: getColumn


    // Search Operations

    public GridPosition positionOf(T element) {
        GridSeriesIterator<T> iterator = gridRowIterator();
        while (iterator.hasNext()) {
            int indexY = iterator.nextIndex();
            int indexX = iterator.next().indexOf(element);
            if (indexX != -1) {
                return new GridPosition(indexX, indexY);
            }
        }
        return new GridPosition(-1, -1);
    }


    // Iterators

    public Iterator<T> iterator() {
        return gridCellIterator();
    }

    // iterates by cells, from top left cell to bottom right, wraps from row end to next row start
    public GridIterator<T> gridCellIterator() {
        return new GridCellItr();
    }

    // iterates by row, top to bottom, useful when row end is meaningful
    public GridSeriesIterator<T> gridRowIterator() {
        return new GridRowItr();
    }

    private class GridCellItr implements GridIterator<T> {
        private GridPosition cursor;  // index of element to be returned by next call to next()
        private GridPosition lastReturned;  // index of element returned by most recent next()

        public GridCellItr() {
            cursor = new GridPosition(0, 0);
            lastReturned = new GridPosition(-1, -1);
        }

        public boolean hasNext() {
            int linearizedIndex = cursor.getY() * getWidth() + cursor.getX();
            int linearizedSize = getWidth() * getHeight();
            return linearizedIndex < linearizedSize;
        }

        public T next() throws NoSuchElementException {
            try {
                GridPosition i = cursor;
                T next = get(i);
                lastReturned = i;
                if (cursor.getX() >= getWidth() - 1) {
                    cursor = new GridPosition(0, cursor.getY() + 1);
                } else {
                    cursor = cursor.add(new GridPosition(1, 0));
                }
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public void set(T element) throws IllegalStateException {
            if (lastReturned.getX() < 0 && lastReturned.getY() < 0) {
                throw new IllegalStateException();
            }

            AbstractGrid.this.set(lastReturned, element);
        }
    }

    private class GridRowItr implements GridSeriesIterator<T> {
        int cursor = 0;
        int lastReturned = -1;

        public boolean hasNext() {
            return cursor != getHeight();
        }

        public List<T> next() throws NoSuchElementException {
            try {
                int i = cursor;
                List<T> next = getRow(i);
                lastReturned = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }
    }


    // View

    // subGrid is area bounded by start and end inclusive
    public Grid<T> subGrid(GridPosition start, GridPosition end)
            throws GridPositionOutOfBoundsException, IllegalArgumentException {
        return new SubGrid(this, start, end);
    }

    class SubGrid extends AbstractGrid<T> {
        private final AbstractGrid<T> backingGrid;
        private final GridPosition offset;
        private final int width;
        private final int height;

        SubGrid(AbstractGrid<T> grid, GridPosition start, GridPosition end)
                throws GridPositionOutOfBoundsException, IllegalArgumentException {
            if (start.getX() < 0 || start.getY() < 0) {
                throw new GridPositionOutOfBoundsException(
                        String.format("start = %d, %d", start.getX(), start.getY()));
            }
            if (end.getX() >= grid.getWidth() || end.getY() >= grid.getHeight()) {
                throw new GridPositionOutOfBoundsException(
                        String.format("end = %d, %d", end.getX(), end.getY()));
            }
            // TODO: try this with AbstractGrid.this.boundsCheck()
            if (start.getX() > end.getX() || start.getY() > end.getY()) {
                throw new IllegalArgumentException(
                        String.format("end(%d, %d) > start(%d, %d)",
                                end.getX(), end.getY(), start.getX(), start.getY()));
            }

            backingGrid = grid;
            offset = start;
            width = end.getX() - start.getX() + 1;
            height = end.getY() - start.getY() + 1;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public T get(GridPosition position) throws GridPositionOutOfBoundsException {
            boundsCheck(position);
            return backingGrid.get(offset.add(position));
        }

        @Override
        public void set(GridPosition position, T element) throws GridPositionOutOfBoundsException {
            boundsCheck(position);
            backingGrid.set(offset.add(position), element);
        }

        public List<T> getRow(int rowIndex) throws GridPositionOutOfBoundsException {
            GridPosition rowStart = new GridPosition(0, rowIndex);
            GridPosition rowEnd = new GridPosition(getWidth() - 1, rowIndex);
            Grid<T> rowView = subGrid(rowStart, rowEnd);
            List<T> rowList = new ArrayList<>();
//            for (T element : rowView) {
//                rowList.add(element);
//            }
            for (T element : rowView) {
                rowList.add(element);
            }
            return rowList;
        }

        public Iterator<T> iterator() {
            return gridCellIterator();
        }

        public GridIterator<T> gridCellIterator() {
            return new GridCellItr();
        }

        public GridSeriesIterator<T> gridRowIterator() {
            return new GridRowItr();
        }

        private class GridCellItr implements GridIterator<T> {
            private GridPosition cursor = new GridPosition(0, 0);
            private GridPosition lastReturned = new GridPosition(-1, -1);

            public boolean hasNext() {
                int linearizedIndex = cursor.getY() * getWidth() + cursor.getX();
                int linearizedSize = getWidth() * getHeight();
                return linearizedIndex < linearizedSize;
            }

            public T next() throws NoSuchElementException {
                try {
                    GridPosition i = cursor;
//                    T next = get(offset.add(i));
                    T next = get(i);
                    lastReturned = i;
                    if (cursor.getX() >= getWidth() - 1) {  // end of row
                        cursor = new GridPosition(0, cursor.getY() + 1);
                    } else {
                        cursor = cursor.add(new GridPosition(1, 0));
                    }
                    return next;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            public void set(T element) throws IllegalStateException {
                if (lastReturned.getX() < 0 && lastReturned.getY() < 0) {
                    throw new IllegalStateException();
                }

                SubGrid.this.set(lastReturned, element);
            }
        }

        private class GridRowItr implements GridSeriesIterator<T> {
            int cursor = 0;
            int lastReturned = -1;

            public boolean hasNext() {
                return cursor != getHeight();
            }

            public List<T> next() throws NoSuchElementException {
                try {
                    int i = cursor;
                    List<T> next = getRow(i);
                    lastReturned = i;
                    cursor = i + 1;
                    return next;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            public int nextIndex() {
                return cursor;
            }

            public int previousIndex() {
                return cursor - 1;
            }
        }

        public Grid<T> subGrid(GridPosition start, GridPosition end)
                throws GridPositionOutOfBoundsException, IllegalStateException {
            return new SubGrid(this, start, end);
        }
    }

    // Helpers

    // TODO: do I need to remove this here and just implement it in the concrete classes?
    // protected because child classes may need to use this when overriding methods or implementing abstracts
    protected void boundsCheck(GridPosition position) throws GridPositionOutOfBoundsException {
        if (!inBounds(position)) {
            throw new GridPositionOutOfBoundsException(outOfBoundsMessage(position));
        }
    }

    protected String outOfBoundsMessage(GridPosition position) {
        return String.format(
                "Position: (%d, %d), Dimension: %d wide x %d high",
                position.getX(), position.getY(), getWidth(), getHeight());
    }
}
