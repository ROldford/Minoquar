package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeSizeModelTest {
    // fields
    private MazeSizeModel xs;
    private MazeSizeModel sm;
    private MazeSizeModel md;
    private MazeSizeModel lg;
    private MazeSizeModel xl;

    // before each
    @BeforeEach
    public void beforeEach() {
        this.xs = new MazeSizeModel(MazeSizeModel.MazeSize.EXTRA_SMALL);
        this.sm = new MazeSizeModel(MazeSizeModel.MazeSize.SMALL);
        this.md = new MazeSizeModel(MazeSizeModel.MazeSize.MEDIUM);
        this.lg = new MazeSizeModel(MazeSizeModel.MazeSize.LARGE);
        this.xl = new MazeSizeModel(MazeSizeModel.MazeSize.EXTRA_LARGE);
    }

    // tests
    @Test
    public void testGetSideLength() {
        assertEquals(4*2+17, xs.getSideLength());
        assertEquals(4*3+17, sm.getSideLength());
        assertEquals(4*4+17, md.getSideLength());
        assertEquals(4*5+17, lg.getSideLength());
        assertEquals(4*6+17, xl.getSideLength());
    }

    @Test
    public void testGetAlignPatternPosition() {
        assertEquals(18, xs.getAlignPatternPosition().getX());
        assertEquals(18, xs.getAlignPatternPosition().getY());
        assertEquals(22, sm.getAlignPatternPosition().getX());
        assertEquals(22, sm.getAlignPatternPosition().getY());
        assertEquals(26, md.getAlignPatternPosition().getX());
        assertEquals(26, md.getAlignPatternPosition().getY());
        assertEquals(30, lg.getAlignPatternPosition().getX());
        assertEquals(30, lg.getAlignPatternPosition().getY());
        assertEquals(34, xl.getAlignPatternPosition().getX());
        assertEquals(34, xl.getAlignPatternPosition().getY());
    }

    @Test
    public void testGetMazeSizeName() {
        assertTrue(xs.getMazeSizeName().equals(MazeSizeModel.NAME_XS));
        assertTrue(sm.getMazeSizeName().equals(MazeSizeModel.NAME_SM));
        assertTrue(md.getMazeSizeName().equals(MazeSizeModel.NAME_MD));
        assertTrue(lg.getMazeSizeName().equals(MazeSizeModel.NAME_LG));
        assertTrue(xl.getMazeSizeName().equals(MazeSizeModel.NAME_XL));
    }
}