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
        assertTrue(display.get(0).substring(0, 8).equals("▓▓▓▓▓▓▓☺"));
        assertTrue(display.get(1).substring(0, 8).equals("▓     ▓ "));
        assertTrue(display.get(2).substring(0, 8).equals("▓ ▓▓▓ ▓ "));
        assertFalse(display.get(0).substring(8, 9).equals("X"));
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

}