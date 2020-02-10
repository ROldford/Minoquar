package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class MazeSizeModelTest {
    private List<MazeSizeModel.MazeSize> sizes;

    // before each
    @BeforeEach
    public void beforeEach() {
        this.sizes = new ArrayList<>();
        sizes.add(MazeSizeModel.MazeSize.EXTRA_SMALL);
        sizes.add(MazeSizeModel.MazeSize.SMALL);
        sizes.add(MazeSizeModel.MazeSize.MEDIUM);
        sizes.add(MazeSizeModel.MazeSize.LARGE);
        sizes.add(MazeSizeModel.MazeSize.EXTRA_LARGE);
    }

    @Test
    public void testGetMazeSizeName() {
        List<String> expectedSizeNames = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        iterateSimultaneously(
                expectedSizeNames, sizes,
                (String expected, MazeSizeModel.MazeSize size) -> assertEquals(
                        expected, MazeSizeModel.getMazeSizeName(size)));
    }

    @Test
    public void testGetSideLength() {
        List<Integer> expectedSideLengths = new ArrayList<>(Arrays.asList(
                4*2+17, 4*3+17, 4*4+17, 4*5+17, 4*6+17));
        iterateSimultaneously(
                expectedSideLengths, sizes,
                (Integer sideLength, MazeSizeModel.MazeSize size) -> assertEquals(
                        sideLength, MazeSizeModel.getSideLength(size)));
    }

    @Test
    public void testGetFinderPatternPositions() {
        List<Integer> expectedNonZeroPositions = new ArrayList<>(Arrays.asList(
                4*2+10, 4*3+10, 4*4+10, 4*5+10, 4*6+10));
        iterateSimultaneously(
                expectedNonZeroPositions, sizes,
                (Integer expected, MazeSizeModel.MazeSize size) -> {
                    List<PositionModel> positions = MazeSizeModel.getFinderPatternPositions(size);
                    assertEquals(0, positions.get(0).getX());
                    assertEquals(0, positions.get(0).getY());
                    assertEquals(expected, positions.get(1).getX());
                    assertEquals(0, positions.get(1).getY());
                    assertEquals(0, positions.get(2).getX());
                    assertEquals(expected, positions.get(2).getY());
                });
    }

    @Test
    public void testGetFinderMarginPositions() {
        List<List<PositionModel>> expectedAllSizes = new ArrayList<>();
        for (int i = 2; i <= 6; i++) {
            List<PositionModel> expectedThisSize = new ArrayList<>();
            expectedThisSize.add(new PositionModel(7, 0));
            expectedThisSize.add(new PositionModel(0, 7));
            expectedThisSize.add(new PositionModel(4 * i + 9, 0));
            expectedThisSize.add(new PositionModel(4 * i + 10, 7));
            expectedThisSize.add(new PositionModel(7, 4 * i + 9));
            expectedThisSize.add(new PositionModel(0, 4 * i + 9));
            expectedAllSizes.add(expectedThisSize);
        }
        iterateSimultaneously(
                sizes, expectedAllSizes,
                (MazeSizeModel.MazeSize size, List<PositionModel> expectedPositions) -> {
                    List<PositionModel> actualPositions = MazeSizeModel.getFinderMarginPositions(size);
                    iterateSimultaneously(
                            expectedPositions, actualPositions,
                            (PositionModel expected, PositionModel actual) -> {
                                assertEquals(expected.getX(), actual.getX());
                                assertEquals(expected.getY(), actual.getY());
                            });
                });
    }

    @Test
    public void testGetAlignPatternPosition() {
        List<Integer> expectedPositions = new ArrayList<>(Arrays.asList(16, 20, 24, 28, 32));
        iterateSimultaneously(
                expectedPositions, sizes,
                (Integer expected, MazeSizeModel.MazeSize size) -> {
                    assertEquals(expected, MazeSizeModel.getAlignPatternPosition(size).getX());
                    assertEquals(expected, MazeSizeModel.getAlignPatternPosition(size).getY());
                });
    }

    @Test
    public void testGetTimingPatternPositions() {
        List<PositionModel> timingPositions = MazeSizeModel.getTimingPatternPositions();
        assertEquals(2, timingPositions.size());
        assertEquals(6, timingPositions.get(0).getX());
        assertEquals(8, timingPositions.get(0).getY());
        assertEquals(8, timingPositions.get(1).getX());
        assertEquals(6, timingPositions.get(1).getY());
    }

    @Test
    public void testGetTimingPatternLength() {
        List<Integer> expectedValues = new ArrayList<>(Arrays.asList(
                4*2+1, 4*3+1, 4*4+1, 4*5+1, 4*6+1
        ));
        iterateSimultaneously(
                expectedValues, sizes,
                (Integer expected, MazeSizeModel.MazeSize size) -> assertEquals(
                        expected, MazeSizeModel.getTimingPatternLength(size)));
    }

    @Test
    public void testGetDarkModulePosition() {
        List<PositionModel> expectedPositions = new ArrayList<>(Arrays.asList(
                new PositionModel(8, 4*2+9),
                new PositionModel(8, 4*3+9),
                new PositionModel(8, 4*4+9),
                new PositionModel(8, 4*5+9),
                new PositionModel(8, 4*6+9)
        ));
        iterateSimultaneously(
                expectedPositions, sizes,
                (PositionModel expected, MazeSizeModel.MazeSize size) -> {
                    PositionModel actual = MazeSizeModel.getDarkModulePosition(size);
                    assertEquals(expected.getX(), actual.getX());
                    assertEquals(expected.getY(), actual.getY());
                });
    }

    @Test
    public void testGetTreasurePosition() {
        List<Integer> expectedPositions = new ArrayList<>(Arrays.asList(17, 21, 25, 29, 33));
        iterateSimultaneously(
                expectedPositions, sizes,
                (Integer expected, MazeSizeModel.MazeSize size) -> {
                    assertEquals(expected, MazeSizeModel.getTreasurePosition(size).getX());
                    assertEquals(expected, MazeSizeModel.getTreasurePosition(size).getY());
                });
    }

    // TODO: extract to a utils class
    // TODO: document where I found this on StackOverflow
    private static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }
}