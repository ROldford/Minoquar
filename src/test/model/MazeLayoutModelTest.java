package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class MazeLayoutModelTest {
    List<MazeLayoutModel> layouts;

    @BeforeEach
    public void beforeEach() {
        layouts = new ArrayList<>();
        layouts.add(MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_SMALL));
        layouts.add(MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.SMALL));
        layouts.add(MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.MEDIUM));
        layouts.add(MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.LARGE));
        layouts.add(MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_LARGE));
    }

    @Test
    public void testInit() {
        List<Integer> expectedSideLengths = new ArrayList<>(Arrays.asList(
                4*2+17, 4*3+17, 4*4+17, 4*5+17, 4*6+17));
        List<String> expectedSizes = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        iterateSimultaneously(
                expectedSideLengths, layouts,
                (Integer sideLength, MazeLayoutModel layout) -> {
                    assertEquals(sideLength, layout.getSideLength());
                });
        iterateSimultaneously(
                expectedSizes, layouts,
                (String size, MazeLayoutModel layout) -> {
                    assertTrue(layout.getSizeName().equals(size));
                });
    }

    @Test
    public void testHasRecognitionPatternsAndMargins() {
        for (MazeLayoutModel layout : layouts) {
            Integer corner = layout.getSideLength() - 1;
            // I'm only testing the corner, one square diagonal inwards, and the margin. THIS IS INTENTIONAL.
            // I'm not going to test every damn square!
            assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new PositionModel(0, 0)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(1, 1)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(7, 7)));
            assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new PositionModel(corner, 0)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(corner - 1, 1)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(corner - 7, 7)));
            assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new PositionModel(0, corner)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(1, corner - 1)));
            assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new PositionModel(7, corner - 7)));
        }
    }

    @Test
    public void testHasAlignmentPattern() {
        for (MazeLayoutModel layout : layouts) {
            Integer alignCorner = layout.getSideLength() - 9;
            assertEquals(
                    MazeLayoutModel.MazeSquare.WALL,
                    layout.getSquare(new PositionModel(alignCorner, alignCorner)));
            assertEquals(
                    MazeLayoutModel.MazeSquare.PASSAGE,
                    layout.getSquare(new PositionModel(alignCorner + 1, alignCorner + 1)));
            assertEquals(
                    MazeLayoutModel.MazeSquare.WALL,
                    layout.getSquare(new PositionModel(alignCorner + 2, alignCorner + 2)));
        }
    }

    @Test
    public void testHasTimingPatterns() {
        for (MazeLayoutModel layout : layouts) {
            int timingPosition = 6;
            int timingStart = 8;
            int timingEnd = layout.getSideLength() - 9;
            for (int i = timingStart; i <= timingEnd; i++) {
                MazeLayoutModel.MazeSquare expected;
                if (i % 2 == 0) {
                    expected = MazeLayoutModel.MazeSquare.WALL;

                } else {
                    expected = MazeLayoutModel.MazeSquare.PASSAGE;
                }
                // check horizontal pattern
                assertEquals(
                        expected,
                        layout.getSquare(new PositionModel(i, timingPosition)));
                // check vertical pattern
                assertEquals(
                        expected,
                        layout.getSquare(new PositionModel(timingPosition, i)));
            }
        }
    }

    @Test
    public void testHasDarkModule() {
        int darkModX = 8;
        for (MazeLayoutModel layout : layouts) {
            assertEquals(
                    MazeLayoutModel.MazeSquare.WALL,
                    layout.getSquare(new PositionModel(darkModX, layout.getSideLength() - 8)));
        }
    }

    @Test
    public void testGetSquare() {
        PositionModel position = new PositionModel(1, 1);
        for (MazeLayoutModel layout : layouts) {
            assertEquals(
                    MazeLayoutModel.MazeSquare.PASSAGE,
                    layout.getSquare(position));
        }
    }

    @Test
    public void testDisplay() {
        for (MazeLayoutModel layout : layouts) {
            List<String> display = layout.display();
            assertTrue(display.get(0).substring(0, 8).equals("▓▓▓▓▓▓▓ "));
            assertTrue(display.get(1).substring(0, 8).equals("▓     ▓ "));
            assertTrue(display.get(2).substring(0, 8).equals("▓ ▓▓▓ ▓ "));
            assertFalse(display.get(0).substring(8, 9).equals("X"));
            assertFalse(display.get(2).substring(8, 9).equals("X"));
            for (String row : display) {
                System.out.println(row);
            }
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
