package model;

import exceptions.GridPositionOutOfBoundsException;
import exceptions.InvalidMazeSaveDataException;
import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MazeModelTest {
    static final String TEST_NAME = "testmaze";
    static final MazeSizeModel.MazeSize TEST_SIZE = MazeSizeModel.MazeSize.MEDIUM;
    MazeModel maze;

    @BeforeEach
    public void beforeEach() {
        maze = new MazeModel(TEST_NAME, TEST_SIZE);
    }

    @Test
    public void testInit() {
        assertEquals(TEST_NAME, maze.getName());
        assertEquals(MazeSizeModel.getSizeName(TEST_SIZE), maze.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(TEST_SIZE), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                new GridPosition(1, 1),
                new GridPosition(1, 2)));
        assertEquals(0, maze.getWins());
        assertEquals(0, maze.getLosses());
    }

    @Test
    public void testInitFromSaveData() {
        String testName = "fromsave";
        MazeSizeModel.MazeSize testSize = MazeSizeModel.MazeSize.EXTRA_SMALL;
        List<String> savedLayout = generateTestData("./data/test/testMazeLayout.txt");
        List<String> savedOutcomeHistory = new ArrayList<>(Collections.singletonList(
                "WLWLWWWLWWWL"));
        MazeModel mazeModel = null;
        try {
            mazeModel = new MazeModel(testName, testSize, savedOutcomeHistory, savedLayout);
        } catch (InvalidMazeSaveDataException e) {
            fail(generateFailMessage(true, "save data should be valid"));
        }
        assertEquals(testName, mazeModel.getName());
        assertEquals(MazeSizeModel.getSizeName(testSize), mazeModel.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(testSize), mazeModel.getSideLength());
        assertTrue(mazeModel.isMoveValid(
                new GridPosition(1, 1),
                new GridPosition(1, 2)));
        assertEquals(8, mazeModel.getWins());
        assertEquals(4, mazeModel.getLosses());
    }

    @Test
    public void testInitFromSaveDataExceptions() {
        MazeSizeModel.MazeSize validSize = MazeSizeModel.MazeSize.EXTRA_SMALL;
        List<String> validHistoryData = new ArrayList<>(Collections.singletonList(
                "WLWLWWWLWWWL"));
        List<String> validMazeData = generateTestData("./data/test/testMazeLayout.txt");
        // size mismatch
        MazeSizeModel.MazeSize invalidSize = MazeSizeModel.MazeSize.EXTRA_LARGE;
        initFromSaveExceptionsCase(
                invalidSize, validHistoryData, validMazeData,
                "maze data is wrong size");
        // invalid chars in maze save data
        List<String> invalidMazeData = generateTestData("./data/test/testMazeLayoutIllegalChars.txt");
        initFromSaveExceptionsCase(
                validSize, validHistoryData, invalidMazeData,
                "maze data has invalid characters");
        // invalid chars in outcome history save data
        List<String> invalidHistoryData = new ArrayList<>(Collections.singletonList(
                "WLWLWXWLWWWL"));
        initFromSaveExceptionsCase(
                validSize, invalidHistoryData, validMazeData,
                "outcome history data has invalid characters");
    }

    private void initFromSaveExceptionsCase(
            MazeSizeModel.MazeSize size,
            List<String> historyData,
            List<String> mazeData,
            String reason) {
        String validName = "exceptions";
        try {
            new MazeModel(validName, size, historyData, mazeData);
            fail(generateFailMessage(false, reason));
        } catch (InvalidMazeSaveDataException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSaveData() {
        addGameHistory();
        List<String> saveData = maze.getSaveData();
        // part 1: name and size (1 line each)
        assertEquals(TEST_NAME, saveData.get(0));
        assertEquals(MazeSizeModel.getSizeCode(TEST_SIZE), saveData.get(1));
        int sideLength = MazeSizeModel.getSideLength(TEST_SIZE);
        // part 2: game history
        // 1 line for number of games played
        // 1 line per hundred games played, most recent game first
        // example has 104 games, so total of 3 lines
        assertEquals("104", saveData.get(2));
        String historyLineTwo = "WWLW";
        String historyLineOne = String.join("", Collections.nCopies(25, historyLineTwo));
        assertEquals(historyLineOne, saveData.get(3));
        assertEquals(historyLineTwo, saveData.get(4));
        // part 3: maze
        // 1 line per maze row
        for (int i = 5; i < saveData.size(); i++) {
            String row = saveData.get(i);
            assertEquals(sideLength, row.length());
            for (int j = 0; j < row.length(); j++) {
                char ch = row.charAt(j);
                // TODO: change to use constants (might be in persistence somewhere?)
                assertTrue(ch == "X".charAt(0) || ch == ".".charAt(0));
            }
        }
    }

    @Test
    public void testIsPositionValid() {
        assertTrue(maze.isPositionValid(new GridPosition(1, 1)));
        assertFalse(maze.isPositionValid(new GridPosition(0, 0)));
    }

    @Test
    void testIsMoveValid() {
        try {
            // corridor moves, all 4 directions: valid
            assertTrue(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(1, 5)));
            assertTrue(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(5, 1)));
            assertTrue(maze.isMoveValid(new GridPosition(5, 5), new GridPosition(1, 5)));
            assertTrue(maze.isMoveValid(new GridPosition(5, 5), new GridPosition(5, 1)));
            // tunnel moves, all 4 directions: valid
            assertTrue(maze.isMoveValid(new GridPosition(5, 5), new GridPosition(5, 7)));
            assertTrue(maze.isMoveValid(new GridPosition(5, 5), new GridPosition(7, 5)));
            assertTrue(maze.isMoveValid(new GridPosition(5, 7), new GridPosition(5, 5)));
            assertTrue(maze.isMoveValid(new GridPosition(7, 5), new GridPosition(5, 5)));
            // invalid moves (same position, diagonal, ending on wall, tunnel and corridor in same move)
            assertFalse(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(1, 1)));
            assertFalse(maze.isMoveValid(new GridPosition(1, 5), new GridPosition(5, 1)));
            assertFalse(maze.isMoveValid(new GridPosition(5, 1), new GridPosition(1, 5)));
            assertFalse(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(1, 0)));
            assertFalse(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(7, 1)));
        } catch (GridPositionOutOfBoundsException e) {
            fail("Exception not expected, all positions are in bounds");
        }
    }

    @Test
    public void testIsMoveValidExceptions() {
        try {
            maze.isMoveValid(new GridPosition(-1, 1), new GridPosition(4, 1));
            fail(generateFailMessage(false, "start position is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        try {
            maze.isMoveValid(new GridPosition(1, 30), new GridPosition(1, 33));
            fail(generateFailMessage(false, "end position is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetValidMoves() throws GridPositionOutOfBoundsException {
        try {
            // corridors of top-left finder pattern: 4-size list
            assertEquals(4, maze.getValidMoves(new GridPosition(1,1), MazeModel.Direction.DOWN).size());
            assertEquals(4, maze.getValidMoves(new GridPosition(1,1), MazeModel.Direction.RIGHT).size());
            assertEquals(4, maze.getValidMoves(new GridPosition(5,5), MazeModel.Direction.UP).size());
            assertEquals(4, maze.getValidMoves(new GridPosition(5,5), MazeModel.Direction.LEFT).size());
            // over wall, all directions: 1-size list
            assertEquals(1, maze.getValidMoves(new GridPosition(5,5), MazeModel.Direction.RIGHT).size());
            assertEquals(1, maze.getValidMoves(new GridPosition(5,5), MazeModel.Direction.DOWN).size());
            assertEquals(1, maze.getValidMoves(new GridPosition(7,5), MazeModel.Direction.LEFT).size());
            assertEquals(1, maze.getValidMoves(new GridPosition(5,7), MazeModel.Direction.UP).size());
            // out of bounds (left and up): 0-size list
            assertEquals(0, maze.getValidMoves(new GridPosition(1,1), MazeModel.Direction.UP).size());
            assertEquals(0, maze.getValidMoves(new GridPosition(1,1), MazeModel.Direction.LEFT).size());
        } catch (GridPositionOutOfBoundsException e) {
            fail("Exception not expected, all positions are in bounds");
        }
    }

    @Test
    public void testGetValidMovesExceptions() {
        try {
            maze.getValidMoves(new GridPosition(-1, -1), MazeModel.Direction.DOWN);
            fail(generateFailMessage(false, "start position is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        try {
            maze.getValidMoves(new GridPosition(33, 33), MazeModel.Direction.DOWN);
            fail(generateFailMessage(false, "end position is out of bounds"));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testToString() {
        addGameHistory();
        String sizeName = MazeSizeModel.getSizeName(TEST_SIZE);
        int sideLength = MazeSizeModel.getSideLength(TEST_SIZE);
        int plays = maze.getTotalPlays();
        int wins = maze.getWins();
        int losses = maze.getLosses();
        assertEquals(String.format("%s - %s (%dx%d) - Games played: %d, Wins: %d, Losses: %d",
                TEST_NAME, sizeName, sideLength, sideLength, plays, wins, losses), maze.toString());
    }

    @Test
    void testGameOutcomes() {
        addGameHistory();
        assertEquals(78, maze.getWins());
        assertEquals(26, maze.getLosses());
        assertEquals(104, maze.getTotalPlays());
    }

    // EFFECTS: add games to this maze's history
    // 104 games total, 78 wins, 26 losses, biggest win streak = 3, last win streak = 2
    private void addGameHistory() {
        for (int i = 0; i < 26; i++) {
            maze.registerOutcome(MazeModel.Outcome.WIN);
            maze.registerOutcome(MazeModel.Outcome.LOSS);
            maze.registerOutcome(MazeModel.Outcome.WIN);
            maze.registerOutcome(MazeModel.Outcome.WIN);
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
    // TODO: if tests can extend, put in superclass and make protected
    private String generateFailMessage(boolean failOnException, String reason) {
        if (failOnException) {
            return String.format("%s, %s", "Exception not expected", reason);
        } else {
            return String.format("%s, %s", "Exception expected but not received", reason);
        }
    }
}
