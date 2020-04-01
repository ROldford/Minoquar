package model;

import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridArray;
import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.SquareDisplayData;
import utils.Utilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LayoutTest {
    private static String FAIL_ON_EXCEPTION = "Exception not expected";
    private static String FAIL_IF_NO_EXCEPTION = "Exception expected";

    Layout singleSquare;
    Layout timingHorizontal;
    Layout timingVertical;
    Layout finderPattern;
    Layout alignmentPattern;
    Layout fiveByThreeRectangle;
    Layout fiveByThreeEmpty;
    Layout.MazeSquare p;
    Layout.MazeSquare w;

    @BeforeEach
    public void beforeEach() {
        this.p = Layout.MazeSquare.PASSAGE;
        this.w = Layout.MazeSquare.WALL;
        try {
            this.singleSquare = new Layout(1, 1,
                    new ArrayList<>(Collections.singletonList(w)));
            this.timingHorizontal = new Layout(5, 1,
                    new ArrayList<>(Arrays.asList(
                            w, p, w, p, w
                    )));
            this.timingVertical = new Layout(1, 5,
                    new ArrayList<>(Arrays.asList(
                            w, p, w, p, w
                    )));
            this.finderPattern = new Layout(7, 7,
                    new ArrayList<>(Arrays.asList(
                            w, w, w, w, w, w, w,
                            w, p, p, p, p, p, w,
                            w, p, w, w, w, p, w,
                            w, p, w, w, w, p, w,
                            w, p, w, w, w, p, w,
                            w, p, p, p, p, p, w,
                            w, w, w, w, w, w, w
                    )));
            this.alignmentPattern = new Layout(5, 5,
                    new ArrayList<>(Arrays.asList(
                            w, w, w, w, w,
                            w, p, p, p, w,
                            w, p, w, p, w,
                            w, p, p, p, w,
                            w, w, w, w, w
                    )));
            this.fiveByThreeRectangle = new Layout(5, 3,
                    new ArrayList<>(Arrays.asList(
                            w, w, w, w, w,
                            w, p, p, p, w,
                            w, w, w, w, w
                    )));
        } catch (IllegalArgumentException e) {
            fail(String.format("%s, data size matches grid", FAIL_ON_EXCEPTION));
        }
        try {
            this.fiveByThreeEmpty = new Layout(5, 3);
        } catch (IllegalArgumentException e) {
            fail(String.format("%s, empty grid should always be constructed with correct number of squares", FAIL_ON_EXCEPTION));
        }
    }

    @Test
    public void testInit() {
        List<Layout> layouts = new ArrayList<>(Arrays.asList(
                singleSquare, timingHorizontal, timingVertical,
                finderPattern, alignmentPattern, fiveByThreeRectangle,
                fiveByThreeEmpty));
        List<Integer> widths = new ArrayList<>(Arrays.asList(
                1, 5, 1, 7, 5, 5, 5));
        List<Integer> heights = new ArrayList<>(Arrays.asList(
                1, 1, 5, 7, 5, 3, 3));
        Utilities.iterateSimultaneously(
                widths, layouts,
                (Integer width, Layout layout) -> assertEquals(width, layout.getWidth()));
        Utilities.iterateSimultaneously(
                heights, layouts,
                (Integer height, Layout layout) -> assertEquals(height, layout.getHeight()));
    }

    @Test
    public void testInitException() {
        String failIfNoException = String.format("%s, data size doesn't match grid", FAIL_IF_NO_EXCEPTION);
        int width = 2;
        int height = 3;
        List<Layout.MazeSquare> badLayoutData = new ArrayList<>(Arrays.asList(
                p, w, p,
                p, w));
        try {
            new Layout(width, height, badLayoutData);
            fail(failIfNoException);
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSquare() {
        try {
            assertEquals(w, singleSquare.getSquare(new GridPosition(0, 0)));
            for (int i = 0; i < 5; i++) {
                if (Utilities.isEven(i)) {
                    assertEquals(w, alignmentPattern.getSquare(new GridPosition(i, i)));
                    assertEquals(w, timingHorizontal.getSquare(new GridPosition(i, 0)));
                    assertEquals(w, timingVertical.getSquare(new GridPosition(0, i)));
                } else {
                    assertEquals(p, alignmentPattern.getSquare(new GridPosition(i, i)));
                    assertEquals(p, timingHorizontal.getSquare(new GridPosition(i, 0)));
                    assertEquals(p, timingVertical.getSquare(new GridPosition(0, i)));
                }
            }
            for (int x = 0; x < fiveByThreeEmpty.getWidth(); x++) {
                for (int y = 0; y < fiveByThreeEmpty.getHeight(); y++) {
                    assertEquals(Layout.MazeSquare.EMPTY, fiveByThreeEmpty.getSquare(new GridPosition(x, y)));
                }
            }
        } catch (GridPositionOutOfBoundsException e) {
            String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        }
    }

    @Test
    public void testGetSquareException() {
        getSquareExceptionThrownCases(fiveByThreeRectangle);
        getSquareExceptionThrownCases(finderPattern);
        getSquareExceptionThrownCases(alignmentPattern);
    }

    private void getSquareExceptionThrownCases(Layout layout) {
        GridPosition nwCorner = new GridPosition(-1, -1);
        GridPosition neCorner = new GridPosition(layout.getWidth(), -1);
        GridPosition swCorner = new GridPosition(-1, layout.getHeight());
        GridPosition seCorner = new GridPosition(layout.getWidth(), layout.getHeight());
        getSquareExceptionThrownCase(nwCorner, layout);
        getSquareExceptionThrownCase(neCorner, layout);
        getSquareExceptionThrownCase(swCorner, layout);
        getSquareExceptionThrownCase(seCorner, layout);
    }

    private void getSquareExceptionThrownCase(GridPosition position, Layout layout) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        try {
            layout.getSquare(position);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetArea() {
        GridPosition start = new GridPosition(0, 0);
        GridPosition end = new GridPosition(2,2);
        List<Layout.MazeSquare> expected = new ArrayList<>(Arrays.asList(
                w, w, w,
                w, p, p,
                w, p, w));
        getAreaCase(finderPattern, start, end, expected);
        start = new GridPosition(2, 0);
        end = new GridPosition(3, 2);
        expected = new ArrayList<>(Arrays.asList(
                w, w,
                p, p,
                w, w));
        getAreaCase(fiveByThreeRectangle, start, end, expected);
    }

    private void getAreaCase(
            Layout layout,
            GridPosition start,
            GridPosition end,
            List<Layout.MazeSquare> expected) {
        Layout area = null;
        try {
            area = layout.getArea(start, end);
        } catch (GridPositionOutOfBoundsException e) {
            fail(generateFailMessage(true, "input positions should be in bounds"));
        } catch (IllegalArgumentException e) {
            fail(generateFailMessage(true, "input positions should be in correct order"));
        }
        assertEquals(expected.size(), area.getWidth() * area.getHeight());  // sanity check for expected
        Iterator<Layout.MazeSquare> areaIterator = area.iterator();
        Iterator<Layout.MazeSquare> expectedIterator = expected.iterator();
        while (areaIterator.hasNext()) {
            Layout.MazeSquare actualSquare = areaIterator.next();
            Layout.MazeSquare expectedSquare = expectedIterator.next();
            assertEquals(expectedSquare, actualSquare);
        }
    }

    @Test
    public void testGetAreaException() {
        getAreaExceptionCase(finderPattern);
        getAreaExceptionCase(fiveByThreeRectangle);
    }

    private void getAreaExceptionCase(Layout layout) {
        int width = layout.getWidth();
        int height = layout.getHeight();
        // out of bounds, start @ -1, -1
        // out of bounds, end @ width, height
        // illegal argument, end before start
        try {
            layout.getArea(new GridPosition(-1, -1), new GridPosition(0, 0));
            fail(generateFailMessage(false, "start is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            // expected
        }
        try {
            layout.getArea(new GridPosition(width - 1, height - 1), new GridPosition(width, height));
            fail(generateFailMessage(false, "start is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            // expected
        }
        try {
            layout.getArea(new GridPosition(1, 1), new GridPosition(0, 0));
            fail(generateFailMessage(false, "start is out of bounds"));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDisplay() {
        SquareDisplayData wall = new SquareDisplayData(w);
        SquareDisplayData pass = new SquareDisplayData(p);
        SquareDisplayData empty = new SquareDisplayData(Layout.MazeSquare.EMPTY);
        Grid<SquareDisplayData> expected = new GridArray<>(5, 5,
                new ArrayList<>(Arrays.asList(
                        wall, wall, wall, wall, wall,
                        wall, pass, pass, pass, wall,
                        wall, pass, wall, pass, wall,
                        wall, pass, pass, pass, wall,
                        wall, wall, wall, wall, wall)));
        Grid<SquareDisplayData> actual = alignmentPattern.display();
        assertEquals(expected, actual);
        expected = new GridArray<>(5, 3,
                new ArrayList<>(Arrays.asList(
                        empty, empty, empty, empty, empty,
                        empty, empty, empty, empty, empty,
                        empty, empty, empty, empty, empty)));
        actual = fiveByThreeEmpty.display();
        assertEquals(expected, actual);
    }

    @Test
    public void testOverwrite() {
        String failOnBoundsException = String.format("%s, overlay is in grid bounds", FAIL_ON_EXCEPTION);
        String failOnGridIterException = String.format("%s, iteration should be correct", FAIL_ON_EXCEPTION);
        List<Layout.MazeSquare> patternList = new ArrayList<>(Arrays.asList(
                w, p,
                p, w
        ));
        GridPosition startPosition = new GridPosition(2, 1);
        List<GridPosition> patternPositions = new ArrayList<>(Arrays.asList(
                startPosition.add(new GridPosition(0, 0)),
                startPosition.add(new GridPosition(1, 0)),
                startPosition.add(new GridPosition(0, 1)),
                startPosition.add(new GridPosition(1, 1))
        ));
        Layout twoByTwoPattern = new Layout(2, 2, patternList);
        try {
            fiveByThreeEmpty.overwrite(startPosition, twoByTwoPattern);
        } catch (GridPositionOutOfBoundsException e) {
            fail(failOnBoundsException);
        }
        Utilities.iterateSimultaneously(
                patternList, patternPositions,
                (Layout.MazeSquare patternSquare, GridPosition position) -> {
                    try {
                        assertEquals(
                                patternSquare, fiveByThreeEmpty.getSquare(position));
                    } catch (GridPositionOutOfBoundsException e) {
                        e.printStackTrace();
                        fail(String.format("Position checked (%d, %d) out of bounds",
                                position.getX(), position.getY()));
                    }
                });
    }

    @Test
    public void testOverwriteThrowException() {
        Layout twoByTwoPattern = new Layout(2, 2, new ArrayList<>(Arrays.asList(
                w, p,
                p, w
        )));
        overwriteExceptionThrownCase(new GridPosition(-1, -1), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new GridPosition(-1, 2), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new GridPosition(4, -1), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new GridPosition(4, 2), twoByTwoPattern, fiveByThreeEmpty);
    }

    private void overwriteExceptionThrownCase(GridPosition cornerPosition, Layout overwriter, Layout overwritten) {
        String failIfNoException = String.format("%s, overlay region out of bounds", FAIL_IF_NO_EXCEPTION);
        String failOnGridIterException = String.format("%s, iteration should be correct", FAIL_ON_EXCEPTION);
        try {
            overwritten.overwrite(cornerPosition, overwriter);
            fail(failIfNoException);
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testIsInBounds() {
        GridPosition start = new GridPosition(0, 0);
        GridPosition end = new GridPosition(
                fiveByThreeEmpty.getWidth() - 1,
                fiveByThreeEmpty.getHeight() - 1);
        GridPosition outsideStart = new GridPosition(-1, -1);
        GridPosition outsideEnd = new GridPosition(fiveByThreeEmpty.getWidth(), fiveByThreeEmpty.getHeight());
        assertTrue(fiveByThreeEmpty.inBounds(start));
        assertTrue(fiveByThreeEmpty.inBounds(end));
        assertFalse(fiveByThreeEmpty.inBounds(outsideStart));
        assertFalse(fiveByThreeEmpty.inBounds(outsideEnd));
    }

    // TODO: refactor all tests to use this
    private String generateFailMessage(boolean failOnException, String reason) {
        if (failOnException) {
            return String.format("%s, %s", FAIL_ON_EXCEPTION, reason);
        } else {
            return String.format("%s, %s", FAIL_IF_NO_EXCEPTION, reason);
        }
    }
}
