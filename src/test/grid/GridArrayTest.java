package grid;

import exceptions.IllegalGridDataSizeException;
import exceptions.GridPositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GridArrayTest {
    Grid<String> nullGrid;
    Grid<String> nullSquareGrid;
    Grid<String> stringGrid;
    Grid<Integer> intSquareGrid;

    private static GridPosition ORIGIN = new GridPosition(0, 0);
    private static String FAIL_ON_EXCEPTION = "Exception not expected";
    private static String FAIL_IF_NO_EXCEPTION = "Exception expected";

    @BeforeEach
    void beforeEach() {
        nullGrid = new GridArray<>(5 ,3);
        nullSquareGrid = new GridArray<>(5);
        stringGrid = new GridArray<>(5, 3,
                new ArrayList<>(Arrays.asList(
                        "A", "B", "C", "D", "E",
                        "F", "G", "H", "I", "J",
                        "K", "L", "M", "N", "O")));
        intSquareGrid = new GridArray<>(3,
                new ArrayList<>(Arrays.asList(
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9)));
    }

    @Test
    void testInit() {
        assertEquals(5, nullGrid.getWidth());
        assertEquals(3, nullGrid.getHeight());
        assertEquals(5, nullSquareGrid.getWidth());
        assertEquals(5, nullSquareGrid.getHeight());
        assertEquals(5, stringGrid.getWidth());
        assertEquals(3, stringGrid.getHeight());
        assertEquals(3, intSquareGrid.getWidth());
        assertEquals(3, intSquareGrid.getHeight());
    }

    @Test
    void testInitException() {
        constructorFailOnExceptionCase(3, 4, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9,
                0, 1, 2)));
        constructorFailOnExceptionCase(2, 2, new ArrayList<>(Arrays.asList(
                1, 2,
                3, 4)));
        constructorExceptionThrownCase(3, 4, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9,
                0, 1, 2, 3)));
        constructorExceptionThrownCase(2, 2, new ArrayList<>(Arrays.asList(
                1, 2,
                3)));
    }

    private <T> void constructorExceptionThrownCase(int width, int height, List<T> data) {
        String failIfNoException = String.format("%s, data size doesn't match grid", FAIL_IF_NO_EXCEPTION);
        try {
            if (width == height) {  // square grid
                new GridArray<>(width, data);
            } else {                // rectangle grid
                new GridArray<>(width, height, data);
            }
            fail(failIfNoException);
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    private <T> void constructorFailOnExceptionCase(int width, int height, List<T> data) {
        String failOnException = String.format("%s, data size matches grid", FAIL_ON_EXCEPTION);
        try {
            if (width == height) {  // square grid
                new GridArray<>(width, data);
            } else {                // rectangle grid
                new GridArray<>(width, height, data);
            }
        } catch (IllegalGridDataSizeException e) {
            fail(failOnException);
        }
    }

    @Test
    void testGet() {
        String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        try {
            assertNull(nullGrid.get(ORIGIN));
            assertNull(nullGrid.get(new GridPosition(4, 2)));
            assertNull(nullSquareGrid.get(ORIGIN));
            assertNull(nullSquareGrid.get(new GridPosition(4, 4)));
            assertEquals("A", stringGrid.get(ORIGIN));
            assertEquals("O", stringGrid.get(new GridPosition(4, 2)));
            assertEquals(1, intSquareGrid.get(ORIGIN));
            assertEquals(9, intSquareGrid.get(new GridPosition(2, 2)));
        } catch (GridPositionOutOfBoundsException e) {
            fail(failOnException);
        }

    }

    @Test
    void testGetException() {
        getExceptionThrownCase(-1, -1, stringGrid);
        getExceptionThrownCase(stringGrid.getWidth(), stringGrid.getHeight(), stringGrid);
        getExceptionThrownCase(new GridPosition(-1, -1), stringGrid);
        getExceptionThrownCase(new GridPosition(stringGrid.getWidth(), stringGrid.getHeight()), stringGrid);
    }

    private <T> void getExceptionThrownCase(int x, int y, Grid<T> grid) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.get(new GridPosition(x, y));
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    private <T> void getExceptionThrownCase(GridPosition position, Grid<T> grid) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.get(position);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testSet() throws GridPositionOutOfBoundsException {
        setFailOnExceptionCase(nullGrid, "test");
        setFailOnExceptionCase(nullSquareGrid, "test");
        setFailOnExceptionCase(stringGrid, "test");
        setFailOnExceptionCase(intSquareGrid, 99);
    }

    private <T> void setFailOnExceptionCase(Grid<T> gridArray, T expected) {
        String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        try {
            gridArray.set(ORIGIN, expected);
            assertEquals(expected, gridArray.get(ORIGIN));
            int cornerX = gridArray.getWidth() - 1;
            int cornerY = gridArray.getHeight() - 1;
            GridPosition corner = new GridPosition(cornerX, cornerY);
            gridArray.set(corner, expected);
            assertEquals(expected, gridArray.get(corner));
        } catch (GridPositionOutOfBoundsException e) {
            fail(failOnException);
        }
    }

    @Test
    void testSetException() {
        String testString = "A";
        setExceptionThrownCase(new GridPosition(-1, -1), stringGrid, testString);
        setExceptionThrownCase(new GridPosition(stringGrid.getWidth(), stringGrid.getHeight()), stringGrid, testString);
    }

//    private <T> void setExceptionThrownCase(int x, int y, Grid<T> grid, T element) {
//        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
//        try {
//            GridPosition position = new GridPosition(x, y);
//            grid.set(position, element);
//            fail(failIfNoException);
//        } catch (GridPositionOutOfBoundsException e) {
//            assertNotNull(e.getMessage());
//        }
//    }

    private <T> void setExceptionThrownCase(GridPosition position, Grid<T> grid, T element) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.set(position, element);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testSubGrid() {
        subGridFailOnExceptionCase(
                stringGrid, ORIGIN, new GridPosition(2, 1),
                new GridArray<String>(3, 2, Arrays.asList(
                        "A", "B", "C",
                        "F", "G", "H"
                )));
        subGridFailOnExceptionCase(
                stringGrid, ORIGIN, new GridPosition(1, 2),
                new GridArray<String>(2, 3, Arrays.asList(
                        "A", "B",
                        "F", "G",
                        "K", "L"
                )));
        subGridFailOnExceptionCase(
                intSquareGrid, ORIGIN, new GridPosition(1, 1),
                new GridArray<Integer>(2, 2, Arrays.asList(
                        1, 2,
                        4, 5
                )));
    }

    private <T> void subGridFailOnExceptionCase(
            Grid<T> grid,
            GridPosition start,
            GridPosition end,
            Grid<T> expected) {
        String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        try {
            Grid<T> subGrid = grid.subGrid(start, end);
            assertEquals(expected, subGrid);
        } catch (GridPositionOutOfBoundsException e) {
            fail(failOnException);
        }
    }

    @Test
    void testSubGridException() {
        String illegalArgumentException = "IllegalArgumentException";
        String gridPositionOutOfBoundsException = "GridPositionOutOfBoundsException";
        subGridExceptionThrownCase(
                stringGrid,
                new GridPosition(2, 1),
                ORIGIN,
                illegalArgumentException);
        subGridExceptionThrownCase(
                stringGrid,
                new GridPosition(-1, -1),
                ORIGIN,
                gridPositionOutOfBoundsException);
        subGridExceptionThrownCase(
                stringGrid,
                new GridPosition(4, 2),
                new GridPosition(5, 3),
                gridPositionOutOfBoundsException);
        subGridExceptionThrownCase(
                stringGrid,
                new GridPosition(-1, 2),
                new GridPosition(0, 3),
                gridPositionOutOfBoundsException);
        subGridExceptionThrownCase(
                stringGrid,
                new GridPosition(4, -1),
                new GridPosition(5, 0),
                gridPositionOutOfBoundsException);
    }

    private <T> void subGridExceptionThrownCase(
            Grid<T> grid,
            GridPosition start,
            GridPosition end,
            String expectedType) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.subGrid(start, end);
            fail(failIfNoException);
        } catch (IllegalArgumentException | GridPositionOutOfBoundsException e) {
            assertEquals(expectedType, e.getClass().getSimpleName());
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testBasicForEach() {
        // do basic for-each,
        // check no exceptions thrown
        basicForEachCase(nullGrid);
        basicForEachCase(nullSquareGrid);
        basicForEachCase(nullGrid);
        basicForEachCase(nullGrid);
    }

    private <T> void basicForEachCase(Grid<T> grid) {
        try {
            for (T element : grid) {
                System.out.println(element);
            }
        } catch (NoSuchElementException e) {
            fail("for each not terminating successfully");
        } catch (Exception e) {
            fail("Other exception not expected");
        }
    }

    @Test
    void testBasicIteration() {
        // use hasNext and next
        // test that hasNext = true and next throws no exceptions while in bounds
        // also check that next gives correct value
        // test that hasNext = false and next throws exception at end
        basicIterationCase(stringGrid,
                new ArrayList<>(Arrays.asList(
                        "A", "B", "C", "D", "E",
                        "F", "G", "H", "I", "J",
                        "K", "L", "M", "N", "O")));
        basicIterationCase(intSquareGrid,
                new ArrayList<>(Arrays.asList(
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9)));
        basicIterationCase(nullGrid,
                new ArrayList<>(Arrays.asList(
                        null, null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null)));
        basicIterationCase(nullSquareGrid,
                new ArrayList<>(Arrays.asList(
                        null, null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null)));
    }

    private <T> void basicIterationCase(Grid<T> grid, List<T> expected) {
        int size = grid.getWidth() * grid.getHeight();
        assertEquals(expected.size(), size);  // fails if expected entered incorrectly
        Iterator<T> iterator = grid.iterator();
        for (int i = 0; i < size; i++) {  // should loop until end of grid
            assertTrue(iterator.hasNext());
            T element = iterator.next();
            assertEquals(expected.get(i), element);
        }
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("next should throw NoSuchElementException at end of grid");
        } catch (NoSuchElementException e) {
            // expected exception
        }
    }

    // TODO: test set in iterator
    @Test
    void testIteratorSet() {
        iteratorSetCase(stringGrid, new ArrayList<>(Arrays.asList(
                "AA", "BB", "CC", "DD", "EE",
                "FF", "GG", "HH", "II", "JJ",
                "KK", "LL", "MM", "OO", "PP")));
        iteratorSetCase(intSquareGrid, new ArrayList<>(Arrays.asList(
                100, 101, 102,
                103, 104, 105,
                106, 107, 108)));
    }

    private <T> void iteratorSetCase(Grid<T> grid, List<T> newValues) {
        int size = grid.getWidth() * grid.getHeight();
        assertEquals(newValues.size(), size);  // fails if expected entered incorrectly
        GridIterator<T> gridIterator = grid.gridCellIterator();
        Iterator<T> newValuesIterator = newValues.iterator();
        // iterate through grid, set all cells to new values from input list
        try {
            gridIterator.set(newValues.get(0));
            fail("IllegalStateException was expected, because next() not yet called");
        } catch (IllegalStateException e) {
            // expected exception
        }
        while (gridIterator.hasNext()) {
            T newValue = newValuesIterator.next();
            gridIterator.next();
            gridIterator.set(newValue);
        }
        // iterate again, check that all cells match new values
        gridIterator = grid.gridCellIterator();
        newValuesIterator = newValues.iterator();
        while (gridIterator.hasNext()) {
            T expected = newValuesIterator.next();
            T actual = gridIterator.next();
            assertEquals(expected, actual);
        }
    }

    @Test
    void testGridSeriesIterator() {
        gridSeriesIteratorCase(stringGrid, new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")),
                new ArrayList<>(Arrays.asList("F", "G", "H", "I", "J")),
                new ArrayList<>(Arrays.asList("K", "L", "M", "N", "O")))));
        gridSeriesIteratorCase(intSquareGrid, new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                new ArrayList<>(Arrays.asList(7, 8, 9)))));
        gridSeriesIteratorCase(nullGrid, new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)))));
        gridSeriesIteratorCase(nullSquareGrid, new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, null, null, null)))));
    }

    private <T> void gridSeriesIteratorCase(Grid<T> grid, List<List<T>> expected) {
        int size = grid.getHeight();
        int rowLength = grid.getWidth();
        assertEquals(expected.size(), size);  // fails if expected entered incorrectly
        GridSeriesIterator<T> gridRowIterator = grid.gridRowIterator();
        for (int i = 0; i < size; i++) {
            assertTrue(gridRowIterator.hasNext());
            try {
                assertEquals(i - 1, gridRowIterator.previousIndex());
                assertEquals(i, gridRowIterator.nextIndex());
                List<T> row = gridRowIterator.next();
                assertEquals(i, gridRowIterator.previousIndex());
                assertEquals(i + 1, gridRowIterator.nextIndex());
                assertEquals(rowLength, row.size()); // fails if expected entered incorrectly
                assertEquals(expected.get(i), row);
            } catch (NoSuchElementException e) {
                fail("iteration terminated too early");
            }
        }
        assertFalse(gridRowIterator.hasNext());
        try {
            gridRowIterator.next();
            fail("next should throw NoSuchElementException at end of grid");
        } catch (NoSuchElementException e) {
            // expected exception
        }
    }

    @Test
    void testSubGridIteration() {
        // case 1
        GridPosition start = ORIGIN;
        GridPosition end = new GridPosition(2, 2);
        List<String> expectedStringCells = new ArrayList<>(Arrays.asList(
                "A", "B", "C",
                "F", "G", "H",
                "K", "L", "M"));
        List<List<String>> expectedStringRows = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("A", "B", "C")),
                new ArrayList<>(Arrays.asList("F", "G", "H")),
                new ArrayList<>(Arrays.asList("K", "L", "M"))));
        subGridBasicIterationCase(stringGrid, start, end, expectedStringCells, expectedStringRows);
        // case 2
        start = new GridPosition(1, 0);
        end = new GridPosition(2, 1);
        List<Integer> expectedIntCells = new ArrayList<>(Arrays.asList(
                2, 3,
                5, 6));
        List<List<Integer>> expectedIntRows = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(2, 3)),
                new ArrayList<>(Arrays.asList(5, 6))));
        subGridBasicIterationCase(intSquareGrid, start, end, expectedIntCells, expectedIntRows);
    }

    private <T> void subGridBasicIterationCase(
            Grid<T> grid,
            GridPosition start,
            GridPosition end,
            List<T> expectedCells,
            List<List<T>> expectedRows) {
        Grid<T> subGrid = grid.subGrid(start, end);
        basicIterationCase(subGrid, expectedCells);
        basicForEachCase(subGrid);
        gridSeriesIteratorCase(subGrid, expectedRows);
    }

    @Test
    void testSubGridIterationSet() {
        // case 1
        GridPosition start = ORIGIN;
        GridPosition end = new GridPosition(2, 2);
        List<String> newStringData = new ArrayList<>(Arrays.asList(
                "AA", "BB", "CC",
                "FF", "GG", "HH",
                "KK", "LL", "MM"));
        List<String> baseStringData = new ArrayList<>(Arrays.asList(
                "AA", "BB", "CC", "D", "E",
                "FF", "GG", "HH", "I", "J",
                "KK", "LL", "MM", "N", "O"));
        subGridIterationSetCase(stringGrid, start, end, newStringData, baseStringData);
        // case 2
        start = new GridPosition(1, 0);
        end = new GridPosition(2, 1);
        List<Integer> newIntData = new ArrayList<>(Arrays.asList(
                22, 33,
                55, 66));
        List<Integer> baseIntData = new ArrayList<>(Arrays.asList(
                1, 22, 33,
                4, 55, 66,
                7, 8, 9));
        subGridIterationSetCase(intSquareGrid, start, end, newIntData, baseIntData);
    }

    private <T> void subGridIterationSetCase(
            Grid<T> grid,
            GridPosition start,
            GridPosition end,
            List<T> newData,
            List<T> baseGridExpected) {
        assertEquals(
                grid.getWidth() * grid.getHeight(),
                baseGridExpected.size()); // fails if expected entered incorrectly
        Grid<T> subGrid = grid.subGrid(start, end);
        iteratorSetCase(subGrid, newData);
        Iterator<T> gridIterator = grid.iterator();
        Iterator<T> expectedIterator = baseGridExpected.iterator();
        while (gridIterator.hasNext()) {
            T expected = expectedIterator.next();
            T actual = gridIterator.next();
            assertEquals(expected, actual);
        }
    }

    @Test
    void testIsInBounds() {
        testCaseInBounds(nullGrid);
        testCaseInBounds(nullSquareGrid);
        testCaseInBounds(stringGrid);
        testCaseInBounds(intSquareGrid);
    }

    private void testCaseInBounds(Grid<?> gridArray) {
        int width = gridArray.getWidth();
        int height = gridArray.getHeight();
        assertTrue(gridArray.inBounds(ORIGIN));
        assertTrue(gridArray.inBounds(new GridPosition(width - 1, height - 1)));
        assertFalse(gridArray.inBounds(new GridPosition(width, 0)));
        assertFalse(gridArray.inBounds(new GridPosition(0, height)));
        assertFalse(gridArray.inBounds(new GridPosition(width, height)));
    }

    @Test
    void testEquals() {
        GridArray<Integer> gridOne = new GridArray<>(3, 3, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        )));
        GridArray<Integer> gridTwo = new GridArray<>(3, 3, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        )));
        GridArray<Integer> gridDataMismatch = new GridArray<>(3, 3, new ArrayList<>(Arrays.asList(
                9, 8, 7,
                6, 5, 4,
                3, 2, 1
        )));
        GridArray<Integer> gridWidthMismatch = new GridArray<>(4, 3, new ArrayList<>(Arrays.asList(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12
        )));
        GridArray<Integer> gridHeightMismatch = new GridArray<>(3, 4, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9,
                10, 11, 12
        )));
        String notDisplayData = "I'm not even the same object type!";
        assertEquals(gridOne, gridOne);
        assertEquals(gridOne.hashCode(), gridOne.hashCode());
        assertNotEquals(gridOne, null);
        assertNotEquals(gridOne, notDisplayData);
        assertEquals(gridOne, gridTwo);
        assertEquals(gridOne.hashCode(), gridTwo.hashCode());
        assertNotEquals(gridOne, gridDataMismatch);
        assertNotEquals(gridOne.hashCode(), gridDataMismatch.hashCode());
        assertNotEquals(gridOne, gridWidthMismatch);
        assertNotEquals(gridOne.hashCode(), gridWidthMismatch.hashCode());
        assertNotEquals(gridOne, gridHeightMismatch);
        assertNotEquals(gridOne.hashCode(), gridHeightMismatch.hashCode());
    }

    @Test
    void testContains() {
        assertTrue(intSquareGrid.contains(5));
        assertFalse(intSquareGrid.contains(0));
        assertTrue(stringGrid.contains("H"));
        assertFalse(stringGrid.contains("Z"));
    }

    @Test
    void testPositionOf() {
        GridPosition notInGrid = new GridPosition(-1, -1);
        GridPosition origin = new GridPosition(0, 0);
        assertEquals(origin, intSquareGrid.positionOf(1));
        assertEquals(new GridPosition(2, 2), intSquareGrid.positionOf(9));
        assertEquals(notInGrid, intSquareGrid.positionOf(0));
        assertEquals(origin, stringGrid.positionOf("A"));
        assertEquals(new GridPosition(4, 2), stringGrid.positionOf("O"));
        assertEquals(notInGrid, stringGrid.positionOf("Z"));
        GridArray<String> newStringGrid = new GridArray<>(2, 2, new ArrayList<>(Arrays.asList(
                "A", "B",
                "B", "A")));
        assertEquals(origin, newStringGrid.positionOf("A"));
    }
}