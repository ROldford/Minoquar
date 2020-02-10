package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class MazeBoardModelTest {
    List<MazeBoardModel> boards;
    MazeBoardModel xs;
    MazeBoardModel sm;
    MazeBoardModel md;
    MazeBoardModel lg;
    MazeBoardModel xl;

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
        List<String> expectedSizes = new ArrayList<>(Arrays.asList(
                MazeSizeModel.NAME_XS,
                MazeSizeModel.NAME_SM,
                MazeSizeModel.NAME_MD,
                MazeSizeModel.NAME_LG,
                MazeSizeModel.NAME_XL));
        iterateSimultaneously(
                expectedSideLengths, boards,
                (Integer sideLength, MazeBoardModel board) -> {
                    assertEquals(sideLength, board.getSideLength());
                });
        iterateSimultaneously(
                expectedSizes, boards,
                (String size, MazeBoardModel board) -> {
                    assertTrue(board.getSizeName().equals(size));
                });
    }

    @Test
    public void testGetSquaresBetweenAllPassages() {
        PositionModel topLeft = new PositionModel(1, 1);
        PositionModel topRight = new PositionModel(1, 5);
        PositionModel bottomLeft = new PositionModel(5, 1);
        List<MazeLayoutModel.MazeSquare> right = boards.get(0).getSquaresBetween(topLeft, topRight);
        List<MazeLayoutModel.MazeSquare> left = boards.get(0).getSquaresBetween(topRight, topLeft);
        List<MazeLayoutModel.MazeSquare> up = boards.get(0).getSquaresBetween(topLeft, bottomLeft);
        List<MazeLayoutModel.MazeSquare> down = boards.get(0).getSquaresBetween(bottomLeft, topLeft);
        List<List<MazeLayoutModel.MazeSquare>> testLists = new ArrayList<>(Arrays.asList(right, left, up, down));
        for (List<MazeLayoutModel.MazeSquare> testList: testLists) {
            assertEquals(3, testList.size());
            for (MazeLayoutModel.MazeSquare square: testList) {
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, square);
            }
        }
    }

    // TODO: Make more tests for all wall sections (in recognizer) and both types (in timing pattern)

    private static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }
}
