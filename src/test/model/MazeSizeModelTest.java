package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeSizeModelTest {
    // fields
    private MazeSizeModel mazeSizeModel;

    // before each
    @BeforeEach
    public void beforeEach() {
        this.mazeSizeModel = new MazeSizeModel();
    }

    // tests
    @Test
    public void testGetSideLength() {
        assertEquals(
                4*2+17,
                mazeSizeModel.getSideLength(MazeSizeModel.MazeSize.EXTRA_SMALL));
        assertEquals(
                4*3+17,
                mazeSizeModel.getSideLength(MazeSizeModel.MazeSize.SMALL));
        assertEquals(
                4*4+17,
                mazeSizeModel.getSideLength(MazeSizeModel.MazeSize.MEDIUM));
        assertEquals(
                4*5+17,
                mazeSizeModel.getSideLength(MazeSizeModel.MazeSize.LARGE));
        assertEquals(
                4*6+17,
                mazeSizeModel.getSideLength(MazeSizeModel.MazeSize.EXTRA_LARGE));
    }

    @Test
    public void testGetAlignPatternPosition() {
        PositionModel xs = mazeSizeModel.getAlignPatternPosition(
                MazeSizeModel.MazeSize.EXTRA_SMALL);
        PositionModel sm = mazeSizeModel.getAlignPatternPosition(
                MazeSizeModel.MazeSize.SMALL);
        PositionModel md = mazeSizeModel.getAlignPatternPosition(
                MazeSizeModel.MazeSize.MEDIUM);
        PositionModel lg = mazeSizeModel.getAlignPatternPosition(
                MazeSizeModel.MazeSize.LARGE);
        PositionModel xl = mazeSizeModel.getAlignPatternPosition(
                MazeSizeModel.MazeSize.EXTRA_LARGE);
        assertEquals(18, xs.getX());
        assertEquals(18, xs.getY());
        assertEquals(22, sm.getX());
        assertEquals(22, sm.getY());
        assertEquals(26, md.getX());
        assertEquals(26, md.getY());
        assertEquals(30, lg.getX());
        assertEquals(30, lg.getY());
        assertEquals(34, xl.getX());
        assertEquals(34, xl.getY());
    }
}