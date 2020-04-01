// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridPosition;
import model.MazeListModel;
import model.MazeModel;
import model.MazeSizeModel;
import org.junit.jupiter.api.Test;
import ui.SquareDisplayData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    @Test
    public void testParseMazeListFile1() {
        // xs maze
        try {
            MazeListModel mazes = new MazeListModel(
                    Reader.readMazeList(new File("./data/test/testMazeListXS.txt")));
            testMaze(mazes.getElementAt(0),
                    "one",
                    MazeSizeModel.MazeSize.EXTRA_SMALL,
                    0,
                    0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testParseMazeListFile2() {
        // xl maze
        try {
            MazeListModel mazes = new MazeListModel(
                    Reader.readMazeList(new File("./data/test/testMazeListXL.txt")));
            testMaze(mazes.getElementAt(0),
                    "two",
                    MazeSizeModel.MazeSize.EXTRA_LARGE,
                    5,
                    5);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testParseMazeListFile3() {
        // both mazes from before in same file
        try {
            MazeListModel mazes = new MazeListModel(
                    Reader.readMazeList(new File("./data/test/testMazeListMultipleLists.txt")));
            testMaze(mazes.getElementAt(0),
                    "one",
                    MazeSizeModel.MazeSize.EXTRA_SMALL,
                    0,
                    0);
            testMaze(mazes.getElementAt(1),
                    "two",
                    MazeSizeModel.MazeSize.EXTRA_LARGE,
                    5,
                    5);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testParseNonMazeListFile() {
        try {
            MazeListModel mazes = new MazeListModel(
                    Reader.readMazeList(new File("./data/test/testMazeListBadFile.txt")));
            assertEquals(0, mazes.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testParseLongHistory() {
        try {
            MazeListModel mazes = new MazeListModel(
                    Reader.readMazeList(new File("./data/test/testMazeListLongGameHistory.txt")));
            testMaze(mazes.getElementAt(0),
                    "long history",
                    MazeSizeModel.MazeSize.EXTRA_SMALL,
                    55,
                    55);
        } catch (IOException | GridPositionOutOfBoundsException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testParseCorruptedSaves() {
        String corruptedSavesFilePath = "./data/test/testMazeListCorruptedSaves.txt";
        String testTrashFilePath = "./data/test/corruptedSaveTrash.txt";
        assertTrue(clearTestTrash(testTrashFilePath));
        try {
            MazeListModel mazes = new MazeListModel(Reader.readMazeList(new File(corruptedSavesFilePath), testTrashFilePath));
            // test file has 8 mazes, 7 are corrupted, 1 is valid
            assertEquals(1, mazes.getSize());
            assertEquals("goodmaze", mazes.getElementAt(0).getName());
            // TODO: test that trash file has the other 7 mazes (by checking that all names are there)
            List<String> badMazeNames = new ArrayList<>(Arrays.asList(
                    "badsizecode", "nogamecount", "negativegamecount", "toomanyhistorylines",
                    "toofewhistorylines", "toosmall", "toobig"));
            assertTrue(allBadMazesTrashed(badMazeNames, testTrashFilePath));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private boolean clearTestTrash(String trashFilePath) {
        // check if file exists
        // if exists, attempt delete
        //      if delete fails, return false
        // attempt create, return true if success, false if not
        try {
            File trashFile = new File(trashFilePath);
            if (trashFile.exists()) {
                if (!trashFile.delete()) {
                    return false;
                }
            }
            return trashFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean allBadMazesTrashed(List<String> badMazeNames, String trashFilePath) throws IOException {
        Map<String, Boolean> badMazeCounts = new HashMap<>();
        // map will only have bad maze names
        for (String badMaze : badMazeNames) {
            badMazeCounts.put(badMaze, false);
        }
        List<String> trashContent = Files.readAllLines(new File(trashFilePath).toPath());
        // replace ensures that only the given maze names count, all else skipped
        for (String trashLine : trashContent) {
            badMazeCounts.replace(trashLine, true);
        }
        // return true if all bad maze names found
        for (Boolean value : badMazeCounts.values()) {
            if (!value) {
                return false;
            }
        }
        return true;
    }

    private void testMaze(MazeModel maze,
                          String expectedName,
                          MazeSizeModel.MazeSize expectedSize,
                          int expectedWins,
                          int expectedLosses) throws GridPositionOutOfBoundsException {
        // name check
        assertEquals(expectedName, maze.getName());
        // size check
        int oneSideLength = MazeSizeModel.getSideLength(expectedSize);
        assertEquals(MazeSizeModel.getSizeName(expectedSize), maze.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(expectedSize), oneSideLength);
        // play history check
        assertEquals(expectedWins, maze.getWins());
        assertEquals(expectedLosses, maze.getLosses());
        assertEquals(expectedWins + expectedLosses, maze.getTotalPlays());
        // valid maze check
        assertFalse(maze.isMoveValid(new GridPosition(7,0), new GridPosition(0,0)));
        assertTrue(maze.isMoveValid(new GridPosition(1, 1), new GridPosition(5, 1)));
        assertTrue(maze.isMoveValid(new GridPosition(5, 1), new GridPosition(7,1)));
        assertFalse(maze.isMoveValid(new GridPosition(7,6), new GridPosition(11, 6)));
        // valid display data check
        Grid<SquareDisplayData> oneDisplay = maze.displayMaze();
        assertEquals(oneSideLength, oneDisplay.getWidth());
        assertEquals(oneSideLength, oneDisplay.getHeight());
    }
}
