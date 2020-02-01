package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeLayoutModelTest {
    @Test
    public void testInit() {
        MazeLayoutModel xs = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_SMALL);
        MazeLayoutModel sm = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.SMALL);
        MazeLayoutModel md = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.MEDIUM);
        MazeLayoutModel lg = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.LARGE);
        MazeLayoutModel xl = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_LARGE);
        assertEquals(4*2+17, xs.getMazeSideLength());
        assertEquals(4*3+17, sm.getMazeSideLength());
        assertEquals(4*4+17, md.getMazeSideLength());
        assertEquals(4*5+17, lg.getMazeSideLength());
        assertEquals(4*6+17, xl.getMazeSideLength());
        assertTrue(xs.getMazeSize().equals(MazeSizeModel.NAME_XS));
        assertTrue(sm.getMazeSize().equals(MazeSizeModel.NAME_SM));
        assertTrue(md.getMazeSize().equals(MazeSizeModel.NAME_MD));
        assertTrue(lg.getMazeSize().equals(MazeSizeModel.NAME_LG));
        assertTrue(xl.getMazeSize().equals(MazeSizeModel.NAME_XL));
    }
}
