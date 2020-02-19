package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MazeModelTest {
    static final String TEST_NAME = "testmaze";
    static final MazeSizeModel.MazeSize TEST_SIZE = MazeSizeModel.MazeSize.MEDIUM;
    MazeModel maze;

    @BeforeEach
    public void beforeEach() {
        maze = new MazeModel(TEST_NAME, TEST_SIZE);
    }

    @Test
    public void testInit() {
        assertEquals(TEST_NAME, maze.getName());
        assertEquals(MazeSizeModel.getSizeName(TEST_SIZE), maze.getSizeName());
        assertEquals(MazeSizeModel.getSideLength(TEST_SIZE), maze.getSideLength());
        assertTrue(maze.isMoveValid(
                new PositionModel(1, 1),
                new PositionModel(1, 2)));
    }

    @Test
    public void testGetSaveData() {
        List<String> saveData = maze.getSaveData();
        assertEquals(TEST_NAME, saveData.get(0));
        assertEquals(MazeSizeModel.getSizeCode(TEST_SIZE), saveData.get(1));
        int sideLength = MazeSizeModel.getSideLength(TEST_SIZE);
        assertEquals( sideLength + 2, saveData.size());
        for (int i = 2; i < saveData.size(); i++) {
            String row = saveData.get(i);
            assertEquals(sideLength, row.length());
            for (int j = 0; j < row.length(); j++) {
                char ch = row.charAt(j);
                assertTrue(ch == "X".charAt(0) || ch == ".".charAt(0));
            }
        }
    }
}
