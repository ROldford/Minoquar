package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MazeModelTest {
    static final String TEST_NAME = "Test Maze";
    static final MazeSizeModel.MazeSize TEST_SIZE = MazeSizeModel.MazeSize.MEDIUM;;
    MazeModel maze;

    @BeforeEach
    public void beforeEach() {
        maze = new MazeModel(TEST_NAME, TEST_SIZE);
    }

    @Test
    public void testInit() {
        assertTrue(maze.getName().equals(TEST_NAME));
        assertTrue(maze.getSizeName().equals(MazeSizeModel.getMazeSizeName(TEST_SIZE)));
        assertEquals(MazeSizeModel.getSideLength(TEST_SIZE), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                new PositionModel(1, 1),
                new PositionModel(1, 2)));
    }
}
