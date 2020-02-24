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

    @Test
    public void testGetValidMoves() {
        // corridors of top-left finder pattern: 4-size list
        assertEquals(4, maze.getValidMoves(new PositionModel(1,1), MazeModel.Direction.DOWN).size());
        assertEquals(4, maze.getValidMoves(new PositionModel(1,1), MazeModel.Direction.RIGHT).size());
        assertEquals(4, maze.getValidMoves(new PositionModel(5,5), MazeModel.Direction.UP).size());
        assertEquals(4, maze.getValidMoves(new PositionModel(5,5), MazeModel.Direction.LEFT).size());
        // over wall, all directions: 1-size list
        assertEquals(1, maze.getValidMoves(new PositionModel(5,5), MazeModel.Direction.RIGHT).size());
        assertEquals(1, maze.getValidMoves(new PositionModel(5,5), MazeModel.Direction.DOWN).size());
        assertEquals(1, maze.getValidMoves(new PositionModel(7,5), MazeModel.Direction.LEFT).size());
        assertEquals(1, maze.getValidMoves(new PositionModel(5,7), MazeModel.Direction.UP).size());
        // out of bounds (left and up): 0-size list
        assertEquals(0, maze.getValidMoves(new PositionModel(1,1), MazeModel.Direction.UP).size());
        assertEquals(0, maze.getValidMoves(new PositionModel(1,1), MazeModel.Direction.LEFT).size());
    }
}
