package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeModelTest {
    @Test
    public void testInit() {
        String testName = "Test Maze";
        MazeModel maze = new MazeModel(testName, MazeSizeModel.MazeSize.MEDIUM);
        assertTrue(maze.getName().equals(testName));
        assertTrue(maze.isMoveValid(
                PositionModel.createNewInstance(1, 1),
                PositionModel.createNewInstance(1, 2)));
    }

}
