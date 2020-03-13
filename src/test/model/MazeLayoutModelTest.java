package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import ui.SquareDisplayData;
import utils.GridArray;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> expectedSizeNames = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        Utilities.iterateSimultaneously(
                expectedSideLengths, layouts,
                (Integer sideLength, MazeLayoutModel layout) ->
                        assertEquals(sideLength, MazeSizeModel.getSideLength(layout.getSize())));
        Utilities.iterateSimultaneously(
                expectedSizeNames, layouts,
                (String sizeName, MazeLayoutModel layout) ->
                        assertEquals(sizeName, MazeSizeModel.getSizeName(layout.getSize())));
    }

    @Test
    public void testHasRecognitionPatternsAndMargins() {
        for (MazeLayoutModel layout : layouts) {
            int corner = MazeSizeModel.getSideLength(layout.getSize()) - 1;
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
            int alignCorner = MazeSizeModel.getSideLength(layout.getSize()) - 9;
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
            int timingEnd = MazeSizeModel.getSideLength(layout.getSize()) - 9;
            for (int i = timingStart; i <= timingEnd; i++) {
                MazeLayoutModel.MazeSquare expected;
                if (Utilities.isEven(i)) {
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
                    layout.getSquare(new PositionModel(darkModX, MazeSizeModel.getSideLength(layout.getSize()) - 8)));
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
            SquareDisplayData wall = new SquareDisplayData(Layout.MazeSquare.WALL);
            SquareDisplayData pass = new SquareDisplayData(Layout.MazeSquare.PASSAGE);
            SquareDisplayData empty = new SquareDisplayData(Layout.MazeSquare.EMPTY);
            GridArray<SquareDisplayData> expectedFinder = new GridArray<>(7, 3,
                    new ArrayList<>(Arrays.asList(
                            wall, wall, wall, wall, wall, wall, wall,
                            wall, pass, pass, pass, pass, pass, wall,
                            wall, pass, wall, wall, wall, pass, wall)));
            GridArray<SquareDisplayData> display = layout.display();
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 3; y++) {
                    assertEquals(expectedFinder.get(x, y), display.get(x, y));
                }
            }
            assertNotEquals(empty, display.get(8, 0));
            assertNotEquals(empty, display.get(8, 2));
//            assertEquals("▓▓▓▓▓▓▓ ", display.get(0).substring(0, 8));
//            assertEquals("▓     ▓ ", display.get(1).substring(0, 8));
//            assertEquals("▓ ▓▓▓ ▓ ", display.get(2).substring(0, 8));
//            assertNotEquals("X", display.get(0).substring(8, 9));
//            assertNotEquals("X", display.get(2).substring(8, 9));
//            for (String row : display) {
//                System.out.println(row);
//            }
        }
    }

    @Test
    public void testGetTreasurePosition() {
        List<Integer> expectedPositions = new ArrayList<>(Arrays.asList(17, 21, 25, 29, 33));
        Utilities.iterateSimultaneously(
                expectedPositions, layouts,
                (Integer expected, MazeLayoutModel layout) -> {
                    assertEquals(expected, layout.getTreasurePosition().getX());
                    assertEquals(expected, layout.getTreasurePosition().getY());
                });
    }

    @Test
    public void testGetMazeData() {
        List<Integer> expectedSideLengths = new ArrayList<>(Arrays.asList(
                4*2+17, 4*3+17, 4*4+17, 4*5+17, 4*6+17));
        Utilities.iterateSimultaneously(
                expectedSideLengths, layouts,
                (Integer expected, MazeLayoutModel layout) -> {
                    List<String> saveData = layout.getSaveData();
                    assertEquals(expected, saveData.size());
                    for (String row : saveData) {
                        assertEquals(expected, row.length());
                    }
                });
    }

    @Test
    public void testInitFromSavedData() {
        List<String> testData = generateTestData();
        MazeLayoutModel testMazeLayout = MazeLayoutModel.createMazeFromMazeContent(
                MazeSizeModel.MazeSize.EXTRA_SMALL, testData);
        assertEquals(25, MazeSizeModel.getSideLength(testMazeLayout.getSize()));
        assertEquals(MazeSizeModel.NAME_XS, MazeSizeModel.getSizeName(testMazeLayout.getSize()));
        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 25; y++) {
                char savedSquare = testData.get(y).charAt(x);
                Layout.MazeSquare testSquare = testMazeLayout.getSquare(new PositionModel(x, y));
                if (savedSquare == Reader.SAVE_FILE_WALL) {
                    assertEquals(Layout.MazeSquare.WALL, testSquare);
                } else if (savedSquare == Reader.SAVE_FILE_PASSAGE) {
                    assertEquals(Layout.MazeSquare.PASSAGE, testSquare);
                } else {
                    fail("There's a saved square that's not a valid character");
                }
            }
        }
    }

    private List<String> generateTestData() {
        File file = new File("./data/test/testMazeLayout.txt");
        List<String> testData = new ArrayList<>();
        try {
            testData = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            fail("IOException: check that test data file exists with correct name and formatting");
        }
        return testData;
    }

}
