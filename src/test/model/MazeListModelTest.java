package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeListModelTest {
    MazeListModel mazeList;

    @BeforeEach
    public void beforeEach() {
        mazeList = new MazeListModel();
    }

    @Test
    public void testInit() {
        assertEquals(0, mazeList.getMazes().size());
    }

    @Test
    public void testCreateRandomMaze() {
        String testName = "Test Maze";
        MazeSizeModel.MazeSize testSize = MazeSizeModel.MazeSize.MEDIUM;
        String testSizeName = new MazeSizeModel(testSize).getMazeSizeName();
        mazeList.createRandomMaze(testName, testSize);
        assertEquals(1, mazeList.getMazes().size());
        assertTrue(mazeList.readMaze(0).getName().equals(testName));
        assertEquals(testSizeName, mazeList.readMaze(0).getSize());
    }


}
