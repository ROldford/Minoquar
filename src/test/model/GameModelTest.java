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
    private static final String MOVE_TESTS_FILE = "./data/test/testMazeListMinotaurMove.txt";

    GameModel game;
    List<GameModel> startTestGames;
    MazeModel emptyMaze;

    @BeforeEach
    public void beforeEach() {
        game = new GameModel(
                new MazeModel("test", MazeSizeModel.MazeSize.EXTRA_SMALL),
                new PositionModel(7, 0));
        try {
            MazeListModel startTestMazeList = Reader.readMazeList(new File(START_TESTS_FILE));
            emptyMaze = startTestMazeList.readMaze(0);
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

    @Test
    public void testMoveMinotaurOrthogonal() {
        // orthogonal tests
        // hero is already on minotaur: minotaur doesn't move
        testMoveMinotaurCase(new PositionModel(0, 0), new PositionModel(0, 0));
        // hero above minotaur start, 1 square away: minotaur moves up one square, stops on hero
        testMoveMinotaurCase(new PositionModel(0, -1), new PositionModel(0, -1));
        // hero above minotaur start, 5 squares away: minotaur moves up five squares, stops on hero
        testMoveMinotaurCase(new PositionModel(0, -5), new PositionModel(0, -5));
        // hero right of minotaur start, behind wall: minotaur moves right, stops at wall
        testMoveMinotaurCase(new PositionModel(11, 0), new PositionModel(9, 0));
        // hero left of minotaur start, behind long wall: minotaur moves left through wall, stops on hero
        testMoveMinotaurCase(new PositionModel(-4, 0), new PositionModel(-4, 0));
        // hero below minotaur start, just behind wall: minotaur moves down through wall, stops on hero
        testMoveMinotaurCase(new PositionModel(0, 2), new PositionModel(0, 2));
        // hero below minotaur start, 3 squares past wall: minotaur moves down through wall, stops after wall
        testMoveMinotaurCase(new PositionModel(0, 5), new PositionModel(0, 2));
    }

    @Test
    public void testMoveMinotaurDiagonal() {
        // diagonal tests
        // hero 1 square up, 3 squares right of minotaur: minotaur moves 1 square up
        testMoveMinotaurCase(new PositionModel(3, -1), new PositionModel(0, -1));
        // hero 3 squares up, 1 square right of minotaur: minotaur moves 1 square right
        testMoveMinotaurCase(new PositionModel(1, -3), new PositionModel(1, 0));
        // hero 1 square up, 1 square right of minotaur: minotaur randomly chooses direction
        testMoveMinotaurDiagonalCase(
                new PositionModel(1, -1),
                new PositionModel(0, -1),
                new PositionModel(1, 0));
        // hero 1 square down, 2 squares left of minotaur: minotaur moves down through wall (2 squares down)
        testMoveMinotaurCase(new PositionModel(-2, 1), new PositionModel(0, 2));
        // hero 2 squares down, 1 square left of minotaur: minotaur moves left through wall (4 squares left)
        testMoveMinotaurCase(new PositionModel(-1, 2), new PositionModel(-4, 0));
        // hero 1 square down, 1 square left of minotaur: minotaur randomly chooses direction
        testMoveMinotaurDiagonalCase(
                new PositionModel(-1, 1),
                new PositionModel(0, 2),
                new PositionModel(-4, 0));
    }

    private void testMoveMinotaurCase(PositionModel heroStart,
                                      PositionModel expectedDelta) {
        MazeModel maze = generateMoveTestData();
        assertNotNull(maze);
        PositionModel minotaurStart = maze.getMinotaurStartPosition();
        GameModel testGame = new GameModel(maze, minotaurStart.add(heroStart));
        assertTrue(testGame.moveMinotaur());
        assertTrue(testGame.getMinotaurPosition().equivalent(minotaurStart.add(expectedDelta)));
    }

    private void testMoveMinotaurDiagonalCase(PositionModel heroStart,
                                              PositionModel expectedHorizontal,
                                              PositionModel expectedVertical) {
        MazeModel maze = generateMoveTestData();
        assertNotNull(maze);
        PositionModel minotaurStart = maze.getMinotaurStartPosition();
        GameModel testGame = new GameModel(maze, minotaurStart.add(heroStart));
        testGame.moveMinotaur();
        boolean movedHorizontal = testGame.getMinotaurPosition().equivalent(minotaurStart.add(expectedHorizontal));
        boolean movedVertical = testGame.getMinotaurPosition().equivalent(minotaurStart.add(expectedVertical));
        assertTrue(movedHorizontal || movedVertical);
    }

    private MazeModel generateMoveTestData() {
        try {
            MazeListModel mazeList = Reader.readMazeList(new File(GameModelTest.MOVE_TESTS_FILE));
            return mazeList.readMaze(0);
        } catch (IOException e) {
            fail(String.format("Something is wrong with the test file: %s", GameModelTest.MOVE_TESTS_FILE));
            e.printStackTrace();
        }
        return null;
    }

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

    @Test
    public void testCheckForLoss() {
        assertFalse(game.checkForLoss());
        PositionModel minotaurPosition = emptyMaze.getMinotaurStartPosition();
        PositionModel belowMinotaur = minotaurPosition.add(new PositionModel(0, 2));
        PositionModel rightOfMinotaur = minotaurPosition.add(new PositionModel(2, 0));
        GameModel lostGame = new GameModel(emptyMaze, minotaurPosition);
        assertTrue(lostGame.checkForLoss());
        GameModel sameX = new GameModel(emptyMaze, belowMinotaur);
        assertFalse(sameX.checkForLoss());
        GameModel sameY = new GameModel(emptyMaze, rightOfMinotaur);
        assertFalse(sameY.checkForLoss());
    }
}