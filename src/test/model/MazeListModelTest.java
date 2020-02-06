package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MazeListModelTest {
    MazeListModel mazeList;
    static final String TEST_NAME = "Test Maze";
    static final MazeSizeModel.MazeSize TEST_SIZE = MazeSizeModel.MazeSize.MEDIUM;

    @BeforeEach
    public void beforeEach() {
        mazeList = new MazeListModel();
    }

    @Test
    public void testInit() {
        assertEquals(0, mazeList.size());
    }

    @Test
    public void testCreateRandomMaze() {
        String testSizeName = new MazeSizeModel(TEST_SIZE).getMazeSizeName();
        mazeList.createRandomMaze(TEST_NAME, TEST_SIZE);
        assertEquals(1, mazeList.size());
        MazeModel maze = mazeList.readMaze(0);
        assertTrue(maze.getName().equals(TEST_NAME));
        assertEquals(testSizeName, maze.getSize());
    }

}
