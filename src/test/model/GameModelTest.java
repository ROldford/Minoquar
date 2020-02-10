package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    GameModel game;

    @BeforeEach
    public void beforeEach() {
        game = new GameModel(
                new MazeModel("test", MazeSizeModel.MazeSize.EXTRA_SMALL),
                new PositionModel(7, 0));
    }

    @Test
    public void testInit() {
        List<String> display = game.display();
        assertEquals("▓▓▓▓▓▓▓☺", display.get(0).substring(0, 8));
        assertEquals("▓     ▓ ", display.get(1).substring(0, 8));
        assertEquals("▓ ▓▓▓ ▓ ", display.get(2).substring(0, 8));
        assertNotEquals("X", display.get(0).substring(8, 9));
        // TODO: add test for treasure display
        PositionModel alignCorner = MazeSizeModel.getAlignPatternPosition(MazeSizeModel.MazeSize.EXTRA_SMALL);
        assertEquals("▓O  ▓", display.get(alignCorner.getY()+1).substring(
                alignCorner.getX(), alignCorner.getX()+5));
        for (String row : display) {
            System.out.println(row);
        }
    }

    @Test
    public void testMoveHeroValid() {
        // one space passage move
        assertTrue(game.moveHero(new PositionModel(7, 1)));
        // one space tunnel move
        assertTrue(game.moveHero(new PositionModel(5, 1)));
        // many space passage move
        assertTrue(game.moveHero(new PositionModel(1, 1)));
        // one space down to set up for next move
        // not essential to test this, but why not?
        assertTrue(game.moveHero(new PositionModel(1, 2)));
        // many space tunnel move
        assertTrue(game.moveHero(new PositionModel(5, 2)));
    }

    @Test
    public void testMoveHeroInvalid() {
        // move west into wall
        assertFalse(game.moveHero(new PositionModel(6, 0)));
        // TODO: add test for out of bounds move when exceptions are implemented
        // move one down to set up for next move, no test needed since it's done in another test
        game.moveHero(new PositionModel(7, 1));
        // tunnel move west, but over passage
        assertFalse(game.moveHero(new PositionModel(4, 1)));
    }

    @Test
    public void testGetHeroPosition() {
        assertEquals(7, game.getHeroPosition().getX());
        assertEquals(0, game.getHeroPosition().getY());
    }

    @Test
    public void testCheckForWin() {
        assertFalse(game.checkForWin());
        MazeSizeModel.MazeSize size = MazeSizeModel.MazeSize.EXTRA_SMALL;
        GameModel wonGame = new GameModel(
                new MazeModel("already won", size),
                MazeSizeModel.getTreasurePosition(size));
        assertTrue(wonGame.checkForWin());
    }

}