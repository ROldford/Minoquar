package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeModelTest {
    @Test
    public void testInit() {
        String testName = "Test Maze";
        MazeSizeModel.MazeSize testSize = MazeSizeModel.MazeSize.MEDIUM;
        MazeModel maze = new MazeModel(testName, testSize);
        assertTrue(maze.getName().equals(testName));
        assertTrue(maze.getSizeName().equals(MazeSizeModel.getMazeSizeName(testSize)));
        assertEquals(MazeSizeModel.getSideLength(testSize), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                new PositionModel(1, 1),
                new PositionModel(1, 2)));
    }

}
