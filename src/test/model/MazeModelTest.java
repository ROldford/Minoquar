package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeModelTest {
    @Test
    public void testInit() {
        String testName = "Test Maze";
        MazeSizeModel.MazeSize testSize = MazeSizeModel.MazeSize.MEDIUM;
        MazeSizeModel testSizeModel = new MazeSizeModel(testSize);
        MazeModel maze = new MazeModel(testName, testSize);
        assertTrue(maze.getName().equals(testName));
        assertTrue(maze.getSize().equals(testSizeModel.getMazeSizeName()));
        assertEquals(testSizeModel.getSideLength(), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                PositionModel.createNewInstance(1, 1),
                PositionModel.createNewInstance(1, 2)));
    }

}
