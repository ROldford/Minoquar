package model;

import exceptions.GridOperationOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
        assertEquals(0, mazeList.getSize());
    }

    @Test
    public void testCreateRandomMaze() throws GridOperationOutOfBoundsException {
        String testSizeName = MazeSizeModel.getSizeName(TEST_SIZE);
        mazeList.createRandomMaze(TEST_NAME, TEST_SIZE);
        assertEquals(1, mazeList.getSize());
        MazeModel maze = mazeList.getElementAt(0);
        assertEquals(TEST_NAME, maze.getName());
        assertEquals(testSizeName, maze.getSizeName());
        String test2 = "test 2";
        mazeList.createRandomMaze(0, test2, TEST_SIZE);
        assertEquals(2, mazeList.getSize());
        assertEquals(test2, mazeList.getElementAt(0).getName());
        assertEquals(TEST_NAME, mazeList.getElementAt(1).getName());
    }

    @Test
    public void testDeleteMaze() throws GridOperationOutOfBoundsException {
        populateMazeList();
        mazeList.deleteMaze(1);
        assertEquals(2, mazeList.getSize());
        MazeModel first = mazeList.getElementAt(0);
        MazeModel second = mazeList.getElementAt(1);
        assertEquals("maze 0", first.getName());
        assertEquals("maze 2", second.getName());
    }

    @Test
    public void testUpdateList() throws GridOperationOutOfBoundsException {
        MazeModel maze3 = new MazeModel("maze 3", TEST_SIZE);
        MazeModel maze4 = new MazeModel("maze 4", TEST_SIZE);
        MazeModel maze5 = new MazeModel("maze 5", TEST_SIZE);
        MazeModel maze6 = new MazeModel("maze 6", TEST_SIZE);
        List<MazeModel> newMazeList = new ArrayList<>(Arrays.asList(
                maze3, maze4, maze5, maze6));
        populateMazeList();
        assertEquals(3, mazeList.getSize());
        mazeList.updateMazeList(newMazeList);
        assertEquals(4, mazeList.getSize());
        for (int i = 0; i < newMazeList.size(); i++) {
            assertEquals(newMazeList.get(i).getName(), mazeList.getElementAt(i).getName());
        }
    }

    @Test
    void testContainsSameName() throws GridOperationOutOfBoundsException {
        populateMazeList();
        assertTrue(mazeList.containsSameName("maze 0"));
        assertTrue(mazeList.containsSameName("maze 2"));
        assertFalse(mazeList.containsSameName("maze 9"));
    }

    // MODIFIES: mazeList
    // EFFECTS: adds three mazes to mazeList
    private void populateMazeList() throws GridOperationOutOfBoundsException {
        mazeList.createRandomMaze("maze 0", TEST_SIZE);
        mazeList.createRandomMaze("maze 1", TEST_SIZE);
        mazeList.createRandomMaze("maze 2", TEST_SIZE);
    }
}
