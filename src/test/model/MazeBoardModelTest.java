package model;

import exceptions.GridOperationOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MazeBoardModelTest {
    List<MazeBoardModel> boards;

    @BeforeEach
    public void beforeEach() throws GridOperationOutOfBoundsException {
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
    public void testGetSquaresBetweenAllPassages() throws GridOperationOutOfBoundsException {
        PositionModel topLeft = new PositionModel(1, 1);
        PositionModel topRight = new PositionModel(5, 1);
        PositionModel bottomLeft = new PositionModel(1, 5);
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
    public void testGetSquaresBetweenAllWalls() throws GridOperationOutOfBoundsException {
        PositionModel topLeft = new PositionModel(0, 0);
        PositionModel topRight = new PositionModel(6, 0);
        PositionModel bottomLeft = new PositionModel(0, 6);
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
    public void testGetSquaresBetweenBothTypes() throws GridOperationOutOfBoundsException {
        PositionModel leftSide = new PositionModel(8, 6);
        PositionModel rightSide = new PositionModel(11, 6);
        PositionModel topSide = new PositionModel(6, 8);
        PositionModel bottomSide = new PositionModel(6, 11);
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
                    PositionModel start = new PositionModel(1, 0);
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

}
