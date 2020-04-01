package model;

import exceptions.GridPositionOutOfBoundsException;
import exceptions.InvalidMazeSaveDataException;
import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MazeBoardModelTest {
    List<MazeBoardModel> boards;

    @BeforeEach
    public void beforeEach() {
        boards = new ArrayList<>();
        boards.add(new MazeBoardModel(MazeSizeModel.MazeSize.EXTRA_SMALL));
        boards.add(new MazeBoardModel(MazeSizeModel.MazeSize.SMALL));
        boards.add(new MazeBoardModel(MazeSizeModel.MazeSize.MEDIUM));
        boards.add(new MazeBoardModel(MazeSizeModel.MazeSize.LARGE));
        boards.add(new MazeBoardModel(MazeSizeModel.MazeSize.EXTRA_LARGE));
    }

    @Test
    public void testInit() {
        List<Integer> expectedSideLengths = new ArrayList<>(Arrays.asList(
                4*2+17, 4*3+17, 4*4+17, 4*5+17, 4*6+17));
        List<String> expectedSizeNames = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        Utilities.iterateSimultaneously(
                expectedSideLengths, boards,
                (Integer sideLength, MazeBoardModel board) ->
                        assertEquals(sideLength, MazeSizeModel.getSideLength(board.getSize())));
        Utilities.iterateSimultaneously(
                expectedSizeNames, boards,
                (String sizeName, MazeBoardModel board) ->
                        assertEquals(sizeName, MazeSizeModel.getSizeName(board.getSize())));
    }

    @Test
    public void testInitFromSavedData() {
        List<String> testData = generateTestData("./data/test/testMazeLayout.txt");
        MazeBoardModel testMazeBoard = null;
        try {
            testMazeBoard = new MazeBoardModel(
                    MazeSizeModel.MazeSize.EXTRA_SMALL, testData);
        } catch (InvalidMazeSaveDataException e) {
            fail(generateFailMessage(true, "maze data should be correct"));
        }
        assertEquals(25, MazeSizeModel.getSideLength(testMazeBoard.getSize()));
        assertEquals(MazeSizeModel.NAME_XS, MazeSizeModel.getSizeName(testMazeBoard.getSize()));
//        List<Character> testDataAsList = new ArrayList<>();
//        for ( char c : String.join("", testData).toCharArray() ) {
//            testDataAsList.add(c);
//        }
//        // making sure test data splits into list of 1 char strings
//        assertEquals(25 * 25, testDataAsList.size());
//        Iterator<Layout.MazeSquare> mazeLayoutIterator = testMazeBoard.iterator();
//        Iterator<Character> testDataIterator = testDataAsList.iterator();
//        while (testDataIterator.hasNext()) {
//            // check that each character in test data matches actual square in maze layout
//            Layout.MazeSquare mazeSquare = mazeLayoutIterator.next();
//            Character testDataChar = testDataIterator.next();
//            if (testDataChar.equals(Reader.SAVE_FILE_WALL)) {
//                assertEquals(Layout.MazeSquare.WALL, mazeSquare);
//            } else if (testDataChar.equals(Reader.SAVE_FILE_PASSAGE)) {
//                assertEquals(Layout.MazeSquare.PASSAGE, mazeSquare);
//            } else {
//                fail("There's a saved square that's not a valid character");
//            }
//        }
    }

    @Test
    public void testInitFromSavedThrowException() {
        // size mismatch
        List<String> testData = generateTestData("./data/test/testMazeLayout.txt");
        try {
            new MazeBoardModel(MazeSizeModel.MazeSize.EXTRA_LARGE, testData);
            fail(generateFailMessage(false, "maze data is wrong size"));
        } catch (InvalidMazeSaveDataException e) {
            assertNotNull(e.getMessage());
        }
        // illegal characters
        testData = generateTestData("./data/test/testMazeLayoutIllegalChars.txt");
        try {
            new MazeBoardModel(MazeSizeModel.MazeSize.EXTRA_SMALL, testData);
            fail(generateFailMessage(false, "maze data has illegal characters"));
        } catch (InvalidMazeSaveDataException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSquaresBetweenAllPassages() {
        GridPosition topLeft = new GridPosition(1, 1);
        GridPosition topRight = new GridPosition(5, 1);
        GridPosition bottomLeft = new GridPosition(1, 5);
        List<MazeLayoutModel.MazeSquare> left = boards.get(0).getSquaresBetween(topRight, topLeft);
        List<MazeLayoutModel.MazeSquare> right = boards.get(0).getSquaresBetween(topLeft, topRight);
        List<MazeLayoutModel.MazeSquare> up = boards.get(0).getSquaresBetween(bottomLeft, topLeft);
        List<MazeLayoutModel.MazeSquare> down = boards.get(0).getSquaresBetween(topLeft, bottomLeft);
        List<List<MazeLayoutModel.MazeSquare>> testLists = new ArrayList<>(Arrays.asList(right, left, up, down));
        for (List<MazeLayoutModel.MazeSquare> testList: testLists) {
            assertEquals(3, testList.size());
            for (MazeLayoutModel.MazeSquare square: testList) {
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, square);
            }
        }
    }

    @Test
    public void testGetSquaresBetweenAllWalls() {
        GridPosition topLeft = new GridPosition(0, 0);
        GridPosition topRight = new GridPosition(6, 0);
        GridPosition bottomLeft = new GridPosition(0, 6);
        List<MazeLayoutModel.MazeSquare> left = boards.get(0).getSquaresBetween(topRight, topLeft);
        List<MazeLayoutModel.MazeSquare> right = boards.get(0).getSquaresBetween(topLeft, topRight);
        List<MazeLayoutModel.MazeSquare> up = boards.get(0).getSquaresBetween(bottomLeft, topLeft);
        List<MazeLayoutModel.MazeSquare> down = boards.get(0).getSquaresBetween(topLeft, bottomLeft);
        List<List<MazeLayoutModel.MazeSquare>> testLists = new ArrayList<>(Arrays.asList(right, left, up, down));
        for (List<MazeLayoutModel.MazeSquare> testList: testLists) {
            assertEquals(5, testList.size());
            for (MazeLayoutModel.MazeSquare square: testList) {
                assertEquals(MazeLayoutModel.MazeSquare.WALL, square);
            }
        }
    }

    @Test
    public void testGetSquaresBetweenBothTypes() {
        GridPosition leftSide = new GridPosition(8, 6);
        GridPosition rightSide = new GridPosition(11, 6);
        GridPosition topSide = new GridPosition(6, 8);
        GridPosition bottomSide = new GridPosition(6, 11);
        List<MazeLayoutModel.MazeSquare> right = boards.get(0).getSquaresBetween(leftSide, rightSide);
        List<MazeLayoutModel.MazeSquare> left = boards.get(0).getSquaresBetween(rightSide, leftSide);
        List<MazeLayoutModel.MazeSquare> up = boards.get(0).getSquaresBetween(bottomSide, topSide);
        List<MazeLayoutModel.MazeSquare> down = boards.get(0).getSquaresBetween(topSide, bottomSide);
        List<Layout.MazeSquare> passageFirst = new ArrayList<>(Arrays.asList(
                Layout.MazeSquare.PASSAGE, Layout.MazeSquare.WALL));
        List<Layout.MazeSquare> wallFirst = new ArrayList<>(Arrays.asList(
                Layout.MazeSquare.WALL, Layout.MazeSquare.PASSAGE));
        assertEquals(passageFirst, right);
        assertEquals(wallFirst, left);
        assertEquals(passageFirst, down);
        assertEquals(wallFirst, up);
    }

    @Test
    public void testGetSquaresBetweenExceptions() {
        MazeBoardModel board = boards.get(0);
        // out of bounds
        try {
            board.getSquaresBetween(new GridPosition(-1, 0), new GridPosition(5, 0));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        try {
            board.getSquaresBetween(new GridPosition(0, 20), new GridPosition(0, 25));
        } catch (GridPositionOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
        // not in line
        try {
            board.getSquaresBetween(new GridPosition(0, 0), new GridPosition(4, 4));
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
        // same position
        try {
            board.getSquaresBetween(new GridPosition(10, 15), new GridPosition(10, 15));
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSquaresInDirection() {
        List<MazeSizeModel.MazeSize> sizes = new ArrayList<>(Arrays.asList(
                MazeSizeModel.MazeSize.EXTRA_SMALL,
                MazeSizeModel.MazeSize.SMALL,
                MazeSizeModel.MazeSize.MEDIUM,
                MazeSizeModel.MazeSize.LARGE,
                MazeSizeModel.MazeSize.EXTRA_LARGE
        ));
        Utilities.iterateSimultaneously(
                sizes, boards,
                (MazeSizeModel.MazeSize size, MazeBoardModel board) -> {
                    GridPosition start = new GridPosition(1, 0);
                    int sideLength = MazeSizeModel.getSideLength(size);
                    assertEquals(0, board.getSquaresInDirection(start, MazeModel.Direction.UP).size());
                    assertEquals(1, board.getSquaresInDirection(start, MazeModel.Direction.LEFT).size());
                    assertEquals(
                            sideLength - 1,
                            board.getSquaresInDirection(start, MazeModel.Direction.DOWN).size());
                    assertEquals(
                            sideLength - 2,
                            board.getSquaresInDirection(start, MazeModel.Direction.RIGHT).size());
                });
    }

    private List<String> generateTestData(String pathName) {
        File file = new File(pathName);
        List<String> testData = new ArrayList<>();
        try {
            testData = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            fail("IOException: check that test data file exists with correct name and formatting");
        }
        return testData;
    }

    // TODO: refactor all tests to just use this
    private String generateFailMessage(boolean failOnException, String reason) {
        if (failOnException) {
            return String.format("%s, %s", "Exception not expected", reason);
        } else {
            return String.format("%s, %s", "Exception expected but not received", reason);
        }
    }
}
