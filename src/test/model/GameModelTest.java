package model;

import exceptions.IncorrectGridIterationException;
import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import ui.SquareDisplayData;
import grid.GridArray;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    private static final String START_TESTS_FILE = "./data/test/testMazeListMinotaurStart.txt";
    private static final String MOVE_TESTS_FILE = "./data/test/testMazeListMinotaurMove.txt";
    private static final String ALL_WALL_TEST_FILE = "./data/test/testMazeListMinotaurWalledOff.txt";
    private static final String NO_VALID_MOVES_TEST_FILE = "./data/test/testMazeListMinotaurNoValidMoves.txt";

    GameModel game;
    List<GameModel> startTestGames;
    MazeModel emptyMaze;

    @BeforeEach
    public void beforeEach() throws Exception {
        game = new GameModel(
                new MazeModel("test", MazeSizeModel.MazeSize.EXTRA_SMALL));
        try {
            MazeListModel startTestMazeList = new MazeListModel(Reader.readMazeList(new File(START_TESTS_FILE)));
            emptyMaze = startTestMazeList.getElementAt(0);
            startTestGames = new ArrayList<>();
            for (int i = 0; i < startTestMazeList.getSize(); i++) {
                MazeModel startTestMaze = startTestMazeList.getElementAt(i);
                startTestGames.add(new GameModel(startTestMaze));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInit() {
        assertNotNull(game.getHeroPosition());
        assertNotNull(game.getMinotaurPosition());
    }

    @Test
    public void testDisplay() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        SquareDisplayData wall = new SquareDisplayData(Layout.MazeSquare.WALL);
        SquareDisplayData pass = new SquareDisplayData(Layout.MazeSquare.PASSAGE);
        SquareDisplayData empty = new SquareDisplayData(Layout.MazeSquare.EMPTY);
        SquareDisplayData hero = new SquareDisplayData(Layout.MazeSquare.PASSAGE,
                new ArrayList<>(Collections.singletonList(GameEntity.EntityType.HERO)));
        SquareDisplayData exit = new SquareDisplayData(Layout.MazeSquare.PASSAGE,
                new ArrayList<>(Collections.singletonList(GameEntity.EntityType.TREASURE)));
        GridArray<SquareDisplayData> expectedFinder = new GridArray<>(8, 3,
                new ArrayList<>(Arrays.asList(
                        wall, wall, wall, wall, wall, wall, wall, hero,
                        wall, pass, pass, pass, pass, pass, wall, pass,
                        wall, pass, wall, wall, wall, pass, wall, pass)));
        List<SquareDisplayData> expectedAlignment = new ArrayList<>(Arrays.asList(
                wall, exit, pass, pass, wall));
        Grid<SquareDisplayData> display = game.display();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                assertEquals(expectedFinder.get(new GridPosition(x, y)), display.get(new GridPosition(x, y)));
            }
        }
        assertNotEquals(empty, display.get(new GridPosition(8, 0)));
        GridPosition alignCorner = MazeSizeModel.getAlignPatternPosition(MazeSizeModel.MazeSize.EXTRA_SMALL);
        for (int i = 0; i < 4; i++) {
            GridPosition checkPosition = alignCorner.add(new GridPosition(i, 1));
            assertEquals(expectedAlignment.get(i), display.get(checkPosition));
        }
    }

    @Test
    public void testGetHeroPosition() {
        assertEquals(7, game.getHeroPosition().getX());
        assertEquals(0, game.getHeroPosition().getY());
    }

    @Test
    public void testMoveHeroValid() throws GridPositionOutOfBoundsException {
        // one space passage move
        assertTrue(game.moveHero(new GridPosition(7, 1)));
        // one space tunnel move
        assertTrue(game.moveHero(new GridPosition(5, 1)));
        // many space passage move
        assertTrue(game.moveHero(new GridPosition(1, 1)));
        // one space down to set up for next move
        // not essential to test this, but why not?
        assertTrue(game.moveHero(new GridPosition(1, 2)));
        // many space tunnel move
        assertTrue(game.moveHero(new GridPosition(5, 2)));
    }

    @Test
    public void testMoveHeroInvalid() throws GridPositionOutOfBoundsException {
        // move west into wall
        assertFalse(game.moveHero(new GridPosition(6, 0)));
        // TODO: add test for out of bounds move when exceptions are implemented
        // move one down to set up for next move, no test needed since it's done in another test
        game.moveHero(new GridPosition(7, 1));
        // tunnel move west, but over passage
        assertFalse(game.moveHero(new GridPosition(4, 1)));
    }

    @Test
    public void testStartMinotaur() {
        List<GridPosition> expectedMinotaurStartPositions = new ArrayList<>(Arrays.asList(
                new GridPosition(12, 12),
                new GridPosition(12, 11),
                new GridPosition(12, 13),
                new GridPosition(11, 12),
                new GridPosition(13, 12),
                new GridPosition(12, 10),
                new GridPosition(11, 11)
        ));
        Utilities.iterateSimultaneously(
                expectedMinotaurStartPositions, startTestGames,
                (GridPosition expected, GameModel game) -> {
                    GridPosition actual = game.getMinotaurPosition();
                    assertEquals(expected.getX(), actual.getX());
                    assertEquals(expected.getY(), actual.getY());
                });
        MazeModel maze = generateTestData(ALL_WALL_TEST_FILE);
        assertNotNull(maze);
        assertEquals(new GridPosition(-1, -1), maze.getMinotaurStartPosition());
    }

    @Test
    public void testDisplayMinotaur() {
        SquareDisplayData minotaur = new SquareDisplayData(Layout.MazeSquare.PASSAGE,
                new ArrayList<>(Collections.singletonList(GameEntity.EntityType.MINOTAUR)));
        List<GridPosition> expectedMinotaurStartPositions = new ArrayList<>(Arrays.asList(
                new GridPosition(12, 12),
                new GridPosition(12, 11),
                new GridPosition(12, 13),
                new GridPosition(11, 12),
                new GridPosition(13, 12),
                new GridPosition(12, 10),
                new GridPosition(11, 11)));
        Utilities.iterateSimultaneously(
                expectedMinotaurStartPositions, startTestGames,
                (GridPosition expected, GameModel game) -> {
                    Grid<SquareDisplayData> display = null;
                    try {
                        display = game.display();
                        assertEquals(minotaur, display.get(expected));
                    } catch (GridPositionOutOfBoundsException | IncorrectGridIterationException e) {
                        e.printStackTrace();
                        fail("Out of bounds");
                    }

                });
    }

    @Test
    public void testMoveMinotaurOrthogonal() throws GridPositionOutOfBoundsException {
        // orthogonal tests
        // hero is already on minotaur: minotaur doesn't move
        testMoveMinotaurCase(new GridPosition(0, 0), new GridPosition(0, 0));
        // hero above minotaur start, 1 square away: minotaur moves up one square, stops on hero
        testMoveMinotaurCase(new GridPosition(0, -1), new GridPosition(0, -1));
        // hero above minotaur start, 5 squares away: minotaur moves up five squares, stops on hero
        testMoveMinotaurCase(new GridPosition(0, -5), new GridPosition(0, -5));
        // hero right of minotaur start, behind wall: minotaur moves right, stops at wall
        testMoveMinotaurCase(new GridPosition(11, 0), new GridPosition(9, 0));
        // hero left of minotaur start, behind long wall: minotaur moves left through wall, stops on hero
        testMoveMinotaurCase(new GridPosition(-4, 0), new GridPosition(-4, 0));
        // hero below minotaur start, just behind wall: minotaur moves down through wall, stops on hero
        testMoveMinotaurCase(new GridPosition(0, 2), new GridPosition(0, 2));
        // hero below minotaur start, 3 squares past wall: minotaur moves down through wall, stops after wall
        testMoveMinotaurCase(new GridPosition(0, 5), new GridPosition(0, 2));
    }

    @Test
    public void testMoveMinotaurDiagonal() throws GridPositionOutOfBoundsException {
        // diagonal tests
        // hero 1 square up, 3 squares right of minotaur: minotaur moves 1 square up
        testMoveMinotaurCase(new GridPosition(3, -1), new GridPosition(0, -1));
        // hero 3 squares up, 1 square right of minotaur: minotaur moves 1 square right
        testMoveMinotaurCase(new GridPosition(1, -3), new GridPosition(1, 0));
        // hero 1 square up, 1 square right of minotaur: minotaur randomly chooses direction
        testMoveMinotaurDiagonalCase(
                new GridPosition(1, -1),
                new GridPosition(0, -1),
                new GridPosition(1, 0));
        // hero 1 square down, 2 squares left of minotaur: minotaur moves down through wall (2 squares down)
        testMoveMinotaurCase(new GridPosition(-2, 1), new GridPosition(0, 2));
        // hero 2 squares down, 1 square left of minotaur: minotaur moves left through wall (4 squares left)
        testMoveMinotaurCase(new GridPosition(-1, 2), new GridPosition(-4, 0));
        // hero 1 square down, 1 square left of minotaur: minotaur randomly chooses direction
        testMoveMinotaurDiagonalCase(
                new GridPosition(-1, 1),
                new GridPosition(0, 2),
                new GridPosition(-4, 0));
    }

    @Test
    public void testMoveMinotaurInvalid() throws GridPositionOutOfBoundsException {
        MazeModel maze = generateTestData(NO_VALID_MOVES_TEST_FILE);
        assertNotNull(maze);
        assertEquals(new GridPosition(12, 12), maze.getMinotaurStartPosition());
        GameModel testGame = new GameModel(maze, new GridPosition(7, 7));
        assertFalse(testGame.moveMinotaur(0.0));
    }

    private void testMoveMinotaurCase(GridPosition heroStart,
                                      GridPosition expectedDelta) throws GridPositionOutOfBoundsException {
        // moveMinotaur() used, because these moves use the non-random part of the minotaur move rules
        MazeModel maze = generateTestData(MOVE_TESTS_FILE);
        assertNotNull(maze);
        GridPosition minotaurStart = maze.getMinotaurStartPosition();
        GameModel testGame = new GameModel(maze, minotaurStart.add(heroStart));
        assertTrue(testGame.moveMinotaur());
        assertEquals(testGame.getMinotaurPosition(), minotaurStart.add(expectedDelta));
    }

    private void testMoveMinotaurDiagonalCase(GridPosition heroStart,
                                              GridPosition expectedVertical,
                                              GridPosition expectedHorizontal) throws GridPositionOutOfBoundsException {
        // moveMinotaur(double randomNumber) used, because perfectly diagonal moves depend on randomNumber
        MazeModel maze = generateTestData(MOVE_TESTS_FILE);
        assertNotNull(maze);
        GridPosition minotaurStart = maze.getMinotaurStartPosition();
        GameModel testGameHorizontalMove = new GameModel(maze, minotaurStart.add(heroStart));
        testGameHorizontalMove.moveMinotaur(0.4);
        assertEquals(testGameHorizontalMove.getMinotaurPosition(), minotaurStart.add(expectedHorizontal));
        GameModel testGameVerticalMove = new GameModel(maze, minotaurStart.add(heroStart));
        testGameVerticalMove.moveMinotaur(0.9);
        assertEquals(testGameVerticalMove.getMinotaurPosition(), minotaurStart.add(expectedVertical));
    }

    private MazeModel generateTestData(String filename) {
        try {
            MazeListModel mazeList = new MazeListModel(Reader.readMazeList(new File(filename)));
            return mazeList.getElementAt(0);
        } catch (IOException e) {
            fail(String.format("Something is wrong with the test file: %s", filename));
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testCheckForWin() throws Exception {
        assertFalse(game.checkForWin());
        MazeSizeModel.MazeSize size = MazeSizeModel.MazeSize.EXTRA_SMALL;
        GridPosition treasurePosition = MazeSizeModel.getTreasurePosition(size);
        GridPosition belowTreasure = treasurePosition.add(new GridPosition(0, 2));
        GridPosition rightOfTreasure = treasurePosition.add(new GridPosition(2, 0));
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
        GridPosition minotaurPosition = emptyMaze.getMinotaurStartPosition();
        GridPosition belowMinotaur = minotaurPosition.add(new GridPosition(0, 2));
        GridPosition rightOfMinotaur = minotaurPosition.add(new GridPosition(2, 0));
        GameModel lostGame = new GameModel(emptyMaze, minotaurPosition);
        assertTrue(lostGame.checkForLoss());
        GameModel sameX = new GameModel(emptyMaze, belowMinotaur);
        assertFalse(sameX.checkForLoss());
        GameModel sameY = new GameModel(emptyMaze, rightOfMinotaur);
        assertFalse(sameY.checkForLoss());
    }

    @Test
    public void testRegisterOutcome() {
        assertEquals(1, game.registerOutcome(MazeModel.Outcome.WIN));
        assertEquals(2, game.registerOutcome(MazeModel.Outcome.LOSS));
    }
}