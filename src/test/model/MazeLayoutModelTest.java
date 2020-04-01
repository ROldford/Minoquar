package model;

import exceptions.*;
import grid.Grid;
import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import ui.SquareDisplayData;
import grid.GridArray;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MazeLayoutModelTest {
    private static String FAIL_ON_EXCEPTION = "Exception not expected";
    private static String FAIL_IF_NO_EXCEPTION = "Exception expected";

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
        for ( MazeLayoutModel mazeLayout : layouts ) {
            for (MazeLayoutModel.MazeSquare mazeSquare : mazeLayout ) {
                assertNotEquals(MazeLayoutModel.MazeSquare.EMPTY, mazeSquare);
            }
        }

    }

    @Test
    public void testHasRecognitionPatternsAndMargins() {
        for (MazeLayoutModel layout : layouts) {
            int corner = MazeSizeModel.getSideLength(layout.getSize()) - 1;
            // I'm only testing the corner, one square diagonal inwards, and the margin. THIS IS INTENTIONAL.
            // I'm not going to test every damn square!
            try {
                assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new GridPosition(0, 0)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(1, 1)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(7, 7)));
                assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new GridPosition(corner, 0)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(corner - 1, 1)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(corner - 7, 7)));
                assertEquals(MazeLayoutModel.MazeSquare.WALL, layout.getSquare(new GridPosition(0, corner)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(1, corner - 1)));
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, layout.getSquare(new GridPosition(7, corner - 7)));
            } catch (GridPositionOutOfBoundsException e) {
                fail(String.format("%s, test should be checking squares in bounds",
                        FAIL_ON_EXCEPTION));
            }

        }
    }

    @Test
    public void testHasAlignmentPattern() {
        for (MazeLayoutModel layout : layouts) {
            int alignCorner = MazeSizeModel.getSideLength(layout.getSize()) - 9;
            try {
                assertEquals(
                        MazeLayoutModel.MazeSquare.WALL,
                        layout.getSquare(new GridPosition(alignCorner, alignCorner)));
                assertEquals(
                        MazeLayoutModel.MazeSquare.PASSAGE,
                        layout.getSquare(new GridPosition(alignCorner + 1, alignCorner + 1)));
                assertEquals(
                        MazeLayoutModel.MazeSquare.WALL,
                        layout.getSquare(new GridPosition(alignCorner + 2, alignCorner + 2)));
            } catch (GridPositionOutOfBoundsException e) {
                fail(String.format("%s, test should be checking squares in bounds",
                        FAIL_ON_EXCEPTION));
            }
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
                try {
                    // check horizontal pattern
                    assertEquals(
                            expected,
                            layout.getSquare(new GridPosition(i, timingPosition)));
                    // check vertical pattern
                    assertEquals(
                            expected,
                            layout.getSquare(new GridPosition(timingPosition, i)));
                } catch (GridPositionOutOfBoundsException e) {
                    fail(String.format("%s, test should be checking squares in bounds",
                            FAIL_ON_EXCEPTION));
                }
            }
        }
    }

    @Test
    public void testHasDarkModule() {
        int darkModX = 8;
        for (MazeLayoutModel layout : layouts) {
            try {
                assertEquals(
                        MazeLayoutModel.MazeSquare.WALL,
                        layout.getSquare(new GridPosition(darkModX, MazeSizeModel.getSideLength(layout.getSize()) - 8)));
            } catch (GridPositionOutOfBoundsException e) {
                fail(String.format("%s, test should be checking squares in bounds",
                        FAIL_ON_EXCEPTION));
            }
        }
    }

    @Test
    public void testGetSquare() {
        GridPosition position = new GridPosition(1, 1);
        for (MazeLayoutModel layout : layouts) {
            try {
                assertEquals(
                        MazeLayoutModel.MazeSquare.PASSAGE,
                        layout.getSquare(position));
            } catch (GridPositionOutOfBoundsException e) {
                fail(String.format("%s, test mazes should be large enough for position to be in bounds",
                        FAIL_ON_EXCEPTION));
            }
        }
    }

    @Test
    public void testDisplay() {
        for (MazeLayoutModel layout : layouts) {
            SquareDisplayData wall = new SquareDisplayData(MazeLayoutModel.MazeSquare.WALL);
            SquareDisplayData pass = new SquareDisplayData(MazeLayoutModel.MazeSquare.PASSAGE);
            SquareDisplayData empty = new SquareDisplayData(MazeLayoutModel.MazeSquare.EMPTY);
            Grid<SquareDisplayData> expectedFinder = new GridArray<>(7, 3,
                    new ArrayList<>(Arrays.asList(
                            wall, wall, wall, wall, wall, wall, wall,
                            wall, pass, pass, pass, pass, pass, wall,
                            wall, pass, wall, wall, wall, pass, wall)));
            try {
                Grid<SquareDisplayData> display = layout.display();
                for (int x = 0; x < 7; x++) {
                    for (int y = 0; y < 3; y++) {
                        assertEquals(expectedFinder.get(new GridPosition(x, y)), display.get(new GridPosition(x, y)));
                    }
                }
                assertNotEquals(empty, display.get(new GridPosition(8, 0)));
                assertNotEquals(empty, display.get(new GridPosition(8, 2)));
            } catch (GridPositionOutOfBoundsException e) {
                fail(String.format("%s, test should always be operating in bounds",
                        FAIL_ON_EXCEPTION));
            }
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
        List<String> testData = generateTestData("./data/test/testMazeLayout.txt");
        MazeLayoutModel testMazeLayout = null;
        try {
            testMazeLayout = MazeLayoutModel.createMazeFromMazeContent(
                    MazeSizeModel.MazeSize.EXTRA_SMALL, testData);
        } catch (InvalidMazeSaveDataException e) {
            fail(generateFailMessage(true, "maze data should be correct"));
        }
        assertEquals(25, MazeSizeModel.getSideLength(testMazeLayout.getSize()));
        assertEquals(MazeSizeModel.NAME_XS, MazeSizeModel.getSizeName(testMazeLayout.getSize()));
        List<Character> testDataAsList = new ArrayList<>();
        for ( char c : String.join("", testData).toCharArray() ) {
            testDataAsList.add(c);
        }
        // making sure test data splits into list of 1 char strings
        assertEquals(25 * 25, testDataAsList.size());
        Iterator<MazeLayoutModel.MazeSquare> mazeLayoutIterator = testMazeLayout.iterator();
        Iterator<Character> testDataIterator = testDataAsList.iterator();
        while (testDataIterator.hasNext()) {
            // check that each character in test data matches actual square in maze layout
            MazeLayoutModel.MazeSquare mazeSquare = mazeLayoutIterator.next();
            Character testDataChar = testDataIterator.next();
            if (testDataChar.equals(Reader.SAVE_FILE_WALL)) {
                assertEquals(MazeLayoutModel.MazeSquare.WALL, mazeSquare);
            } else if (testDataChar.equals(Reader.SAVE_FILE_PASSAGE)) {
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, mazeSquare);
            } else {
                fail("There's a saved square that's not a valid character");
            }
        }
//        for (int x = 0; x < 25; x++) {
//            for (int y = 0; y < 25; y++) {
//                char savedSquare = testData.get(y).charAt(x);
//                Layout.MazeSquare testSquare = null;
//                try {
//                    testSquare = testMazeLayout.getSquare(new GridPosition(x, y));
//                } catch (GridPositionOutOfBoundsException e) {
//                    fail(String.format("%s, test should be checking squares in bounds",
//                            FAIL_ON_EXCEPTION));
//                }
//                if (savedSquare == Reader.SAVE_FILE_WALL) {
//                    assertEquals(Layout.MazeSquare.WALL, testSquare);
//                } else if (savedSquare == Reader.SAVE_FILE_PASSAGE) {
//                    assertEquals(Layout.MazeSquare.PASSAGE, testSquare);
//                } else {
//                    fail("There's a saved square that's not a valid character");
//                }
//            }
//        }
    }

    @Test
    public void testInitFromSavedExceptions() {
        // size mismatch
        List<String> testData = generateTestData("./data/test/testMazeLayout.txt");
        try {
            MazeLayoutModel.createMazeFromMazeContent(
                    MazeSizeModel.MazeSize.EXTRA_LARGE, testData);
            fail(generateFailMessage(false, "maze data is wrong size"));
        } catch (InvalidMazeSaveDataException e) {
            assertNotNull(e.getMessage());
        }
        // illegal characters
        testData = generateTestData("./data/test/testMazeLayoutIllegalChars.txt");
        try {
            MazeLayoutModel.createMazeFromMazeContent(
                    MazeSizeModel.MazeSize.EXTRA_SMALL, testData);
            fail(generateFailMessage(false, "maze data has illegal characters"));
        } catch (InvalidMazeSaveDataException e) {
            assertNotNull(e.getMessage());
        }
    }

    private List<String> generateTestData(String pathName) {
        File file = new File(pathName);
        List<String> testData = new ArrayList<>();
        try {
            testData = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            fail("IOException: check that test data file exists with correct name and formatting");
        }
        return testData;
    }

    // TODO: refactor all tests to just use this
    private String generateFailMessage(boolean failOnException, String reason) {
        if (failOnException) {
            return String.format("%s, %s", "Exception not expected", reason);
        } else {
            return String.format("%s, %s", "Exception expected but not received", reason);
        }
    }
}
