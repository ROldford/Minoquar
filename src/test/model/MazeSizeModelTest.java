package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MazeSizeModelTest {
    private List<MazeSizeModel.MazeSize> sizes;
    private List<String> sizeCodes;

    // before each
    @BeforeEach
    public void beforeEach() {
        this.sizes = new ArrayList<>();
        sizes.add(MazeSizeModel.MazeSize.EXTRA_SMALL);
        sizes.add(MazeSizeModel.MazeSize.SMALL);
        sizes.add(MazeSizeModel.MazeSize.MEDIUM);
        sizes.add(MazeSizeModel.MazeSize.LARGE);
        sizes.add(MazeSizeModel.MazeSize.EXTRA_LARGE);
        sizeCodes = new ArrayList<>(Arrays.asList(
                "xs", "sm", "md", "lg", "xl", "no"
        ));
    }

    @Test
    public void testGetMazeSizeName() {
        List<String> expectedSizeNames = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        Utilities.iterateSimultaneously(
                expectedSizeNames, sizes,
                (String expected, MazeSizeModel.MazeSize size) -> assertEquals(
                        expected, MazeSizeModel.getMazeSizeName(size)));
    }

    @Test
    public void testGetSideLength() {
        List<Integer> expectedSideLengths = new ArrayList<>(Arrays.asList(
                4*2+17, 4*3+17, 4*4+17, 4*5+17, 4*6+17));
        Utilities.iterateSimultaneously(
                expectedSideLengths, sizes,
                (Integer sideLength, MazeSizeModel.MazeSize size) -> assertEquals(
                        sideLength, MazeSizeModel.getSideLength(size)));
    }

    @Test
    public void testGetSizeCode() {
        Utilities.iterateSimultaneously(
                sizeCodes, sizes,
                (String sizeCode, MazeSizeModel.MazeSize size) -> assertEquals(
                        sizeCode, MazeSizeModel.getSizeCode(size)));
    }

    @Test
    public void testGetSizeForSizeCode() {
        List<MazeSizeModel.MazeSize> expectedSizes = new ArrayList<>(Arrays.asList(
                MazeSizeModel.MazeSize.EXTRA_SMALL,
                MazeSizeModel.MazeSize.SMALL,
                MazeSizeModel.MazeSize.MEDIUM,
                MazeSizeModel.MazeSize.LARGE,
                MazeSizeModel.MazeSize.EXTRA_LARGE,
                null
        ));
        Utilities.iterateSimultaneously(
                expectedSizes, sizeCodes,
                (MazeSizeModel.MazeSize expectedSize, String sizeCode) -> assertEquals(
                        expectedSize, MazeSizeModel.getSizeForSizeCode(sizeCode)));
    }

    @Test
    public void testGetFinderPatternPositions() {
        List<Integer> expectedNonZeroPositions = new ArrayList<>(Arrays.asList(
                4*2+10, 4*3+10, 4*4+10, 4*5+10, 4*6+10));
        Utilities.iterateSimultaneously(
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
        Utilities.iterateSimultaneously(
                sizes, expectedAllSizes,
                (MazeSizeModel.MazeSize size, List<PositionModel> expectedPositions) -> {
                    List<PositionModel> actualPositions = MazeSizeModel.getFinderMarginPositions(size);
                    Utilities.iterateSimultaneously(
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
        Utilities.iterateSimultaneously(
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
        Utilities.iterateSimultaneously(
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
        Utilities.iterateSimultaneously(
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
        Utilities.iterateSimultaneously(
                expectedPositions, sizes,
                (Integer expected, MazeSizeModel.MazeSize size) -> {
                    assertEquals(expected, MazeSizeModel.getTreasurePosition(size).getX());
                    assertEquals(expected, MazeSizeModel.getTreasurePosition(size).getY());
                });
    }
}