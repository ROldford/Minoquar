package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    private static final String START_TESTS_FILE = "./data/test/testMazeListMinotaurStart.txt";

    GameModel game;
    List<GameModel> startTestGames;

    @BeforeEach
    public void beforeEach() {
        game = new GameModel(
                new MazeModel("test", MazeSizeModel.MazeSize.EXTRA_SMALL),
                new PositionModel(7, 0));
        try {
            MazeListModel startTestMazeList = Reader.readMazeList(new File(START_TESTS_FILE));
            startTestGames = new ArrayList<>();
            for (int i = 0; i < startTestMazeList.size(); i++) {
                MazeModel startTestMaze = startTestMazeList.readMaze(i);
                startTestGames.add(new GameModel(
                        startTestMaze,
                        new PositionModel(7, 0)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInit() {
        List<String> display = game.display();
        assertEquals("▓▓▓▓▓▓▓☺", display.get(0).substring(0, 8));
        assertEquals("▓     ▓ ", display.get(1).substring(0, 8));
        assertEquals("▓ ▓▓▓ ▓ ", display.get(2).substring(0, 8));
        assertNotEquals("X", display.get(0).substring(8, 9));
        PositionModel alignCorner = MazeSizeModel.getAlignPatternPosition(MazeSizeModel.MazeSize.EXTRA_SMALL);
        assertEquals("▓O  ▓", display.get(alignCorner.getY()+1).substring(
                alignCorner.getX(), alignCorner.getX()+5));
//        for (String row : display) {
//            System.out.println(row);
//        }
    }

    @Test
    public void testGetHeroPosition() {
        assertEquals(7, game.getHeroPosition().getX());
        assertEquals(0, game.getHeroPosition().getY());
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
    public void testStartMinotaur() {
        List<PositionModel> expectedMinotaurStartPositions = new ArrayList<>(Arrays.asList(
                new PositionModel(12, 12),
                new PositionModel(12, 11),
                new PositionModel(12, 13),
                new PositionModel(11, 12),
                new PositionModel(13, 12),
                new PositionModel(12, 10),
                new PositionModel(11, 11)
        ));
        Utilities.iterateSimultaneously(
                expectedMinotaurStartPositions, startTestGames,
                (PositionModel expected, GameModel game) -> {
                    PositionModel actual = game.getMinotaurPosition();
                    assertEquals(expected.getX(), actual.getX());
                    assertEquals(expected.getY(), actual.getY());
                });
    }

    @Test
    public void displayMinotaur() {
        List<PositionModel> expectedMinotaurStartPositions = new ArrayList<>(Arrays.asList(
                new PositionModel(12, 12),
                new PositionModel(12, 11),
                new PositionModel(12, 13),
                new PositionModel(11, 12),
                new PositionModel(13, 12),
                new PositionModel(12, 10),
                new PositionModel(11, 11)));
        Utilities.iterateSimultaneously(
                expectedMinotaurStartPositions, startTestGames,
                (PositionModel expected, GameModel game) -> {
                    List<String> display = game.display();
                    assertEquals(MinotaurModel.MINO_CHAR, display.get(expected.getY()).charAt(expected.getX()));
                });
    }

//    @Test
//    public void testMoveMinotaur() {
//
//    }

    @Test
    public void testCheckForWin() {
        assertFalse(game.checkForWin());
        MazeSizeModel.MazeSize size = MazeSizeModel.MazeSize.EXTRA_SMALL;
        PositionModel treasurePosition = MazeSizeModel.getTreasurePosition(size);
        PositionModel belowTreasure = treasurePosition.add(new PositionModel(0, 2));
        PositionModel rightOfTreasure = treasurePosition.add(new PositionModel(2, 0));
        GameModel wonGame = new GameModel(new MazeModel("already won", size), treasurePosition);
        assertTrue(wonGame.checkForWin());
        GameModel sameX = new GameModel(new MazeModel("same x", size), belowTreasure);
        assertFalse(sameX.checkForWin());
        GameModel sameY = new GameModel(new MazeModel("same y", size), rightOfTreasure);
        assertFalse(sameY.checkForWin());
    }

}