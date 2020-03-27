package utils;

import exceptions.GridPositionOutOfBoundsException;
import model.PositionModel;
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

    private static PositionModel ORIGIN = new PositionModel(0, 0);
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
        constructorFailOnExceptionCase(3, 2, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6)));
        constructorFailOnExceptionCase(2, 2, new ArrayList<>(Arrays.asList(
                1, 2,
                3, 4)));
        constructorExceptionThrownCase(3, 2, new ArrayList<>(Arrays.asList(
                1, 2, 3,
                4, 5, 6, 7)));
        constructorExceptionThrownCase(2, 2, new ArrayList<>(Arrays.asList(
                1, 2,
                3)));
    }

    private <T> void constructorExceptionThrownCase(int width, int height, List<T> data) {
        String failIfNoException = String.format("%s, data size doesn't match grid", FAIL_IF_NO_EXCEPTION);
        try {
            if (width == height) {  // square grid
                GridArray<T> grid = new GridArray<>(width, data);
            } else {                // rectangle grid
                GridArray<T> grid = new GridArray<>(width, height, data);
            }
            fail(failIfNoException);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    GridArray.CONSTRUCTOR_EXCEPTION_MESSAGE,
                    e.getMessage());
        }
    }

    private <T> void constructorFailOnExceptionCase(int width, int height, List<T> data) {
        String failOnException = String.format("%s, data size matches grid", FAIL_ON_EXCEPTION);
        try {
            if (width == height) {  // square grid
                GridArray<T> grid = new GridArray<>(width, data);
            } else {                // rectangle grid
                GridArray<T> grid = new GridArray<>(width, height, data);
            }
        } catch (IllegalArgumentException e) {
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
        outOfBoundExceptionThrownCase(-1, -1, stringGrid);
        outOfBoundExceptionThrownCase(stringGrid.getWidth(), stringGrid.getHeight(), stringGrid);
        outOfBoundExceptionThrownCase(new PositionModel(-1, -1), stringGrid);
        outOfBoundExceptionThrownCase(new PositionModel(stringGrid.getWidth(), stringGrid.getHeight()), stringGrid);
    }

    private void outOfBoundExceptionThrownCase(int x, int y, GridArray grid) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        String expectedExceptionMessage = String.format(
                GridPositionOutOfBoundsException.OUT_OF_BOUNDS_EXCEPTION_MESSAGE_TEMPLATE,
                x, y);
        try {
            grid.get(new PositionModel(x, y));
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    private void outOfBoundExceptionThrownCase(PositionModel position, GridArray grid) {
        outOfBoundExceptionThrownCase(position.getX(), position.getY(), grid);
    }

    @Test
    void testSet() {
        testCaseSet(nullGrid, "test");
        testCaseSet(nullSquareGrid, "test");
        testCaseSet(stringGrid, "test");
        testCaseSet(intSquareGrid, 99);
    }

    private <T> void testCaseSet(GridArray<T> gridArray, T testData) {
        gridArray.set(ORIGIN, testData);
        assertEquals(testData, gridArray.get(ORIGIN));
        int cornerX = gridArray.getWidth() - 1;
        int cornerY = gridArray.getHeight() - 1;
        gridArray.set(cornerX, cornerY, testData);
        assertEquals(testData, gridArray.get(cornerX, cornerY));
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
        assertTrue(gridArray.isInBounds(ORIGIN));
        assertTrue(gridArray.isInBounds(width - 1, height - 1));
        assertFalse(gridArray.isInBounds(width, 0));
        assertFalse(gridArray.isInBounds(0, height));
        assertFalse(gridArray.isInBounds(width, height));
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
        PositionModel notInGrid = new PositionModel(-1, -1);
        PositionModel origin = new PositionModel(0, 0);
        assertEquals(origin, intSquareGrid.getPositionOfElement(1));
        assertEquals(new PositionModel(2, 2), intSquareGrid.getPositionOfElement(9));
        assertEquals(notInGrid, intSquareGrid.getPositionOfElement(0));
        assertEquals(origin, stringGrid.getPositionOfElement("A"));
        assertEquals(new PositionModel(4, 2), stringGrid.getPositionOfElement("O"));
        assertEquals(notInGrid, stringGrid.getPositionOfElement("Z"));
        GridArray<String> newStringGrid = new GridArray<>(2, 2, new ArrayList<>(Arrays.asList(
                "A", "B",
                "B", "A")));
        assertEquals(origin, newStringGrid.getPositionOfElement("A"));
    }
}