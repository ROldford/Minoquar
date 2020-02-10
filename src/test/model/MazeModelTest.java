package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MazeModelTest {
    static final String TEST_NAME = "Test Maze";
    static final MazeSizeModel.MazeSize TEST_SIZE = MazeSizeModel.MazeSize.MEDIUM;
    MazeModel maze;

    @BeforeEach
    public void beforeEach() {
        maze = new MazeModel(TEST_NAME, TEST_SIZE);
    }

    @Test
    public void testInit() {
        assertEquals(TEST_NAME, maze.getName());
        assertEquals(MazeSizeModel.getMazeSizeName(TEST_SIZE), maze.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(TEST_SIZE), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                new PositionModel(1, 1),
                new PositionModel(1, 2)));
    }
}
