package model;

import exceptions.GridOperationOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.SquareDisplayData;
import utils.GridArray;
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
            fail(String.format("%s, empty grid should always get properly sized data", FAIL_ON_EXCEPTION));
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
        try {
            new Layout(2, 3, new ArrayList<>(Arrays.asList(
                    p, w, p,
                    p, w)));
            fail(failIfNoException);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    GridArray.CONSTRUCTOR_EXCEPTION_MESSAGE,
                    e.getMessage());
        }
    }

    @Test
    public void testGetSquare() {
        try {
            assertEquals(w, singleSquare.getSquare(new PositionModel(0, 0)));
            for (int i = 0; i < 5; i++) {
                if (Utilities.isEven(i)) {
                    assertEquals(w, alignmentPattern.getSquare(new PositionModel(i, i)));
                    assertEquals(w, timingHorizontal.getSquare(new PositionModel(i, 0)));
                    assertEquals(w, timingVertical.getSquare(new PositionModel(0, i)));
                } else {
                    assertEquals(p, alignmentPattern.getSquare(new PositionModel(i, i)));
                    assertEquals(p, timingHorizontal.getSquare(new PositionModel(i, 0)));
                    assertEquals(p, timingVertical.getSquare(new PositionModel(0, i)));
                }
            }
            for (int x = 0; x < fiveByThreeEmpty.getWidth(); x++) {
                for (int y = 0; y < fiveByThreeEmpty.getHeight(); y++) {
                    assertEquals(Layout.MazeSquare.EMPTY, fiveByThreeEmpty.getSquare(new PositionModel(x, y)));
                }
            }
        } catch (GridOperationOutOfBoundsException e) {
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
        PositionModel nwCorner = new PositionModel(-1, -1);
        PositionModel neCorner = new PositionModel(layout.getWidth(), -1);
        PositionModel swCorner = new PositionModel(-1, layout.getHeight());
        PositionModel seCorner = new PositionModel(layout.getWidth(), layout.getHeight());
        getSquareExceptionThrownCase(nwCorner, layout);
        getSquareExceptionThrownCase(neCorner, layout);
        getSquareExceptionThrownCase(swCorner, layout);
        getSquareExceptionThrownCase(seCorner, layout);
    }

    private void getSquareExceptionThrownCase(PositionModel position, Layout layout) {
        String failIfNoException = String.format("%s, checking out of bounds", FAIL_IF_NO_EXCEPTION);
        String expectedExceptionMessage = String.format(
                GridOperationOutOfBoundsException.MESSAGE_TEMPLATE_POSITION,
                position.getX(), position.getY());
        try {
            layout.getSquare(position);
            fail(failIfNoException);
        } catch (GridOperationOutOfBoundsException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test
    public void testDisplay() {
        SquareDisplayData wall = new SquareDisplayData(w);
        SquareDisplayData pass = new SquareDisplayData(p);
        SquareDisplayData empty = new SquareDisplayData(Layout.MazeSquare.EMPTY);
        String failOnException = String.format("%s, display() should never check out of bounds", FAIL_ON_EXCEPTION);
        try {
            GridArray<SquareDisplayData> expected = new GridArray<>(5, 5,
                    new ArrayList<>(Arrays.asList(
                            wall, wall, wall, wall, wall,
                            wall, pass, pass, pass, wall,
                            wall, pass, wall, pass, wall,
                            wall, pass, pass, pass, wall,
                            wall, wall, wall, wall, wall)));
            GridArray<SquareDisplayData> actual = alignmentPattern.display();
            assertEquals(expected, actual);
        } catch (GridOperationOutOfBoundsException e) {
            fail(failOnException);
        }
        try {
            GridArray<SquareDisplayData> expected = new GridArray<>(5, 3,
                    new ArrayList<>(Arrays.asList(
                            empty, empty, empty, empty, empty,
                            empty, empty, empty, empty, empty,
                            empty, empty, empty, empty, empty)));
            GridArray<SquareDisplayData> actual = fiveByThreeEmpty.display();
            assertEquals(expected, actual);
        } catch (GridOperationOutOfBoundsException e) {
            fail(failOnException);
        }
    }

    @Test
    public void testOverwrite() {
        String failOnException = String.format("%s, checking in grid bounds", FAIL_ON_EXCEPTION);
        List<Layout.MazeSquare> patternList = new ArrayList<>(Arrays.asList(
                w, p,
                p, w
        ));
        PositionModel startPosition = new PositionModel(2, 1);
        List<PositionModel> patternPositions = new ArrayList<>(Arrays.asList(
                startPosition.add(new PositionModel(0, 0)),
                startPosition.add(new PositionModel(1, 0)),
                startPosition.add(new PositionModel(0, 1)),
                startPosition.add(new PositionModel(1, 1))
        ));
        Layout twoByTwoPattern = new Layout(2, 2, patternList);
        try {
            fiveByThreeEmpty.overwrite(startPosition, twoByTwoPattern);
        } catch (GridOperationOutOfBoundsException e) {
            fail(failOnException);
        }
        Utilities.iterateSimultaneously(
                patternList, patternPositions,
                (Layout.MazeSquare patternSquare, PositionModel position) -> {
                    try {
                        assertEquals(
                                patternSquare, fiveByThreeEmpty.getSquare(position));
                    } catch (GridOperationOutOfBoundsException e) {
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
        overwriteExceptionThrownCase(new PositionModel(-1, -1), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new PositionModel(-1, 2), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new PositionModel(4, -1), twoByTwoPattern, fiveByThreeEmpty);
        overwriteExceptionThrownCase(new PositionModel(4, 2), twoByTwoPattern, fiveByThreeEmpty);
    }

    private void overwriteExceptionThrownCase(PositionModel cornerPosition, Layout overwriter, Layout overwritten) {
        String failIfNoException = String.format("%s, overlay region out of bounds", FAIL_IF_NO_EXCEPTION);
        String expectedExceptionMessage = String.format(
                GridOperationOutOfBoundsException.MESSAGE_TEMPLATE_AREA,
                cornerPosition.getX(), cornerPosition.getY(),
                cornerPosition.getX() + overwriter.getWidth() - 1, cornerPosition.getY() + overwriter.getHeight() - 1);
        try {
            overwritten.overwrite(cornerPosition, overwriter);
            fail(failIfNoException);
        } catch (GridOperationOutOfBoundsException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test
    public void testIsInBounds() {
        PositionModel start = new PositionModel(0, 0);
        PositionModel end = new PositionModel(
                fiveByThreeEmpty.getWidth() - 1,
                fiveByThreeEmpty.getHeight() - 1);
        PositionModel outsideStart = new PositionModel(-1, -1);
        PositionModel outsideEnd = new PositionModel(fiveByThreeEmpty.getWidth(), fiveByThreeEmpty.getHeight());
        assertTrue(fiveByThreeEmpty.isInBounds(start));
        assertTrue(fiveByThreeEmpty.isInBounds(end));
        assertFalse(fiveByThreeEmpty.isInBounds(outsideStart));
        assertFalse(fiveByThreeEmpty.isInBounds(outsideEnd));
        assertTrue(fiveByThreeEmpty.isInBounds(start, new PositionModel(1, 1)));
        assertFalse(fiveByThreeEmpty.isInBounds(outsideStart, start));
        assertFalse(fiveByThreeEmpty.isInBounds(end, outsideEnd));
    }
}
