package model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class LayoutTest {
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
        this.singleSquare = new Layout(1, 1,
                new ArrayList<>(Arrays.asList(w)));
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
        this.fiveByThreeEmpty = new Layout(5, 3);
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
        iterateSimultaneously(
                widths, layouts,
                (Integer width, Layout layout) -> {
                    assertEquals(width, layout.getWidth());
                });
        iterateSimultaneously(
                heights, layouts,
                (Integer height, Layout layout) -> {
                    assertEquals(height, layout.getHeight());
                });
    }

    @Test
    public void testGetSquare() {
        assertEquals(w, singleSquare.getSquare(new PositionModel(0, 0)));
        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
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
    }

    @Test
    public void testOverwritePattern() {
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
        fiveByThreeEmpty.overwrite(startPosition, twoByTwoPattern);
        iterateSimultaneously(
                patternList, patternPositions,
                (Layout.MazeSquare patternSquare, PositionModel position) -> {
                    assertEquals(patternSquare, fiveByThreeEmpty.getSquare(position));
                });
    }

    @Test
    public void testDisplay() {
        List<String> alignDisplay = alignmentPattern.display();
        assertTrue(alignDisplay.get(0).substring(0,5).equals("▓▓▓▓▓"));
        assertTrue(alignDisplay.get(1).substring(0,5).equals("▓   ▓"));
        assertTrue(alignDisplay.get(2).substring(0,5).equals("▓ ▓ ▓"));
        List<String> emptyDisplay = fiveByThreeEmpty.display();
        for (String row : emptyDisplay) {
            assertTrue(row.equals("XXXXX"));
        }
    }

    private static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }

}
