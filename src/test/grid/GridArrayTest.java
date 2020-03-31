package grid;

import exceptions.IllegalGridDataSizeException;
import exceptions.GridPositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridArrayTest {
    GridArray<String> nullGrid;
    GridArray<String> nullSquareGrid;
    GridArray<String> stringGrid;
    GridArray<Integer> intSquareGrid;

    private static GridPosition ORIGIN = new GridPosition(0, 0);
    private static String FAIL_ON_EXCEPTION = "Exception not expected";
    private static String FAIL_IF_NO_EXCEPTION = "Exception expected";

    // TODO: add tests for iteration!

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
            assertNull(nullGrid.get(4, 2));
            assertNull(nullSquareGrid.get(ORIGIN));
            assertNull(nullSquareGrid.get(4, 4));
            assertEquals("A", stringGrid.get(ORIGIN));
            assertEquals("O", stringGrid.get(4, 2));
            assertEquals(1, intSquareGrid.get(ORIGIN));
            assertEquals(9, intSquareGrid.get(2, 2));
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

    private <T> void getExceptionThrownCase(int x, int y, GridArray<T> grid) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.get(x, y);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    private <T> void getExceptionThrownCase(GridPosition position, GridArray<T> grid) {
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

    private <T> void setFailOnExceptionCase(GridArray<T> gridArray, T expected) {
        String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        try {
            gridArray.set(ORIGIN, expected);
            assertEquals(expected, gridArray.get(ORIGIN));
            int cornerX = gridArray.getWidth() - 1;
            int cornerY = gridArray.getHeight() - 1;
            gridArray.set(cornerX, cornerY, expected);
            assertEquals(expected, gridArray.get(cornerX, cornerY));
        } catch (GridPositionOutOfBoundsException e) {
            fail(failOnException);
        }
    }

    @Test
    void testSetException() {
        String testString = "A";
        setExceptionThrownCase(-1, -1, stringGrid, testString);
        setExceptionThrownCase(stringGrid.getWidth(), stringGrid.getHeight(), stringGrid, testString);
        setExceptionThrownCase(new GridPosition(-1, -1), stringGrid, testString);
        setExceptionThrownCase(new GridPosition(stringGrid.getWidth(), stringGrid.getHeight()), stringGrid, testString);
    }

    private <T> void setExceptionThrownCase(int x, int y, GridArray<T> grid, T element) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            grid.set(x, y, element);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    private <T> void setExceptionThrownCase(GridPosition position, GridArray<T> grid, T element) {
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
            GridArray<T> grid,
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
    void testIsInBounds() {
        testCaseInBounds(nullGrid);
        testCaseInBounds(nullSquareGrid);
        testCaseInBounds(stringGrid);
        testCaseInBounds(intSquareGrid);
    }

    private void testCaseInBounds(GridArray<?> gridArray) {
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
    void testGetPositionOfElement() {
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