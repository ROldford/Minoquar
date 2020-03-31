package grid;

import exceptions.GridPositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class AbstractGridTest {
    private class MockGrid extends AbstractGrid<Integer> {

        @Override
        public int getWidth() {
            return 3;
        }

        @Override
        public int getHeight() {
            return 1;
        }

        @Override
        public Integer get(GridPosition position) {
            boundsCheck(position);
            return position.getX();
        }

        @Override
        public List<Integer> getRow(int rowIndex) {
            return new ArrayList<>(Arrays.asList(1, 2, 3));
        }
    }

    private class MockNullGrid extends AbstractGrid<Object> {

        @Override
        public int getWidth() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 3;
        }

        @Override
        public Object get(GridPosition position) {
            return null;
        }

        @Override
        public List<Object> getRow(int rowIndex) {
            return null;
        }
    }

    private MockGrid mockGrid;
    private MockNullGrid mockNullGrid;

    @BeforeEach
    void beforeEach() {
        mockGrid = new MockGrid();
        mockNullGrid = new MockNullGrid();
    }

    @Test
    void testSetIsUnsupported() {
        try {
            mockGrid.set(new GridPosition(0, 0), 0);
            fail("AbstractGrid.set should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    void testEqualsNullRow() {
        Grid<Integer> intSquareGrid = new GridArray<>(3,
                new ArrayList<>(Arrays.asList(
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9)));
        assertFalse(mockNullGrid.equals(intSquareGrid));
    }
}
