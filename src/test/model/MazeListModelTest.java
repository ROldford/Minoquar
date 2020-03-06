package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(0, mazeList.getSize());
    }

    @Test
    public void testCreateRandomMaze() {
        String testSizeName = MazeSizeModel.getSizeName(TEST_SIZE);
        mazeList.createRandomMaze(TEST_NAME, TEST_SIZE);
        assertEquals(1, mazeList.getSize());
        MazeModel maze = mazeList.getElementAt(0);
        assertEquals(TEST_NAME, maze.getName());
        assertEquals(testSizeName, maze.getSizeName());
    }

    @Test
    public void testDeleteMaze() {
        mazeList.createRandomMaze("maze 0", TEST_SIZE);
        mazeList.createRandomMaze("maze 1", TEST_SIZE);
        mazeList.createRandomMaze("maze 2", TEST_SIZE);
        mazeList.deleteMaze(1);
        assertEquals(2, mazeList.getSize());
        MazeModel first = mazeList.getElementAt(0);
        MazeModel second = mazeList.getElementAt(1);
        assertEquals("maze 0", first.getName());
        assertEquals("maze 2", second.getName());
    }

}
