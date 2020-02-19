// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package persistence;

import model.MazeListModel;
import model.MazeModel;
import model.MazeSizeModel;
import model.PositionModel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    @Test
    public void testParseMazeListFile1() {
        // xs maze
        try {
            MazeListModel mazes = Reader.readMazeList(new File("./data/test/testMazeList1.txt"));
            testMaze(mazes.readMaze(0), "one", MazeSizeModel.MazeSize.EXTRA_SMALL);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testParseMazeListFile2() {
        // xl maze
        try {
            MazeListModel mazes = Reader.readMazeList(new File("./data/test/testMazeList2.txt"));
            testMaze(mazes.readMaze(0), "two", MazeSizeModel.MazeSize.EXTRA_LARGE);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testParseMazeListFile3() {
        // both mazes from before in same file
        try {
            MazeListModel mazes = Reader.readMazeList(new File("./data/test/testMazeList3.txt"));
            testMaze(mazes.readMaze(0), "one", MazeSizeModel.MazeSize.EXTRA_SMALL);
            testMaze(mazes.readMaze(1), "two", MazeSizeModel.MazeSize.EXTRA_LARGE);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    private void testMaze(MazeModel maze, String expectedName, MazeSizeModel.MazeSize expectedSize) {
        assertEquals(expectedName, maze.getName());
        int oneSideLength = MazeSizeModel.getSideLength(expectedSize);
        assertEquals(MazeSizeModel.getSizeName(expectedSize), maze.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(expectedSize), oneSideLength);
        assertFalse(maze.isMoveValid(new PositionModel(7,0), new PositionModel(0,0)));
        assertTrue(maze.isMoveValid(new PositionModel(1, 1), new PositionModel(5, 1)));
        assertTrue(maze.isMoveValid(new PositionModel(5, 1), new PositionModel(7,1)));
        assertFalse(maze.isMoveValid(new PositionModel(7,6), new PositionModel(11, 6)));
        List<String> oneDisplay = maze.displayMaze();
        assertEquals(oneSideLength, oneDisplay.size());
        for (String row : oneDisplay) {
            assertEquals(oneSideLength, row.length());
        }
    }
}
