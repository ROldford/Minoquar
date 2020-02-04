package model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class MazeLayoutModelTest {
    List<MazeLayoutModel> layouts;
    MazeLayoutModel xs;
    MazeLayoutModel sm;
    MazeLayoutModel md;
    MazeLayoutModel lg;
    MazeLayoutModel xl;

    @BeforeEach
    public void beforeEach() {
        layouts = new ArrayList<>();
        xs = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_SMALL);
        sm = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.SMALL);
        md = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.MEDIUM);
        lg = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.LARGE);
        xl = MazeLayoutModel.createRandomMaze(MazeSizeModel.MazeSize.EXTRA_LARGE);
        layouts.add(xs);
        layouts.add(sm);
        layouts.add(md);
        layouts.add(lg);
        layouts.add(xl);
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
                expectedSideLengths, layouts,
                (Integer sideLength, MazeLayoutModel layout) -> {
                    assertEquals(sideLength, layout.getMazeSideLength());
                });
        iterateSimultaneously(
                expectedSizes, layouts,
                (String size, MazeLayoutModel layout) -> {
                    assertTrue(layout.getMazeSize().equals(size));
                });
    }

    @Test
    public void testGetSquare() {
        PositionModel position = PositionModel.createNewInstance(1, 1);
        for (MazeLayoutModel layout : layouts) {
            assertEquals(
                    MazeLayoutModel.MazeSquare.PASSAGE,
                    layout.getSquare(position));
        }
    }

    @Test
    public void testGetSquaresBetween() {
        PositionModel topLeft = PositionModel.createNewInstance(1, 1);
        PositionModel topRight = PositionModel.createNewInstance(1, 5);
        PositionModel bottomLeft = PositionModel.createNewInstance(5, 1);
        List<MazeLayoutModel.MazeSquare> right = layouts.get(0).getSquaresBetween(topLeft, topRight);
        List<MazeLayoutModel.MazeSquare> left = layouts.get(0).getSquaresBetween(topRight, topLeft);
        List<MazeLayoutModel.MazeSquare> up = layouts.get(0).getSquaresBetween(topLeft, bottomLeft);
        List<MazeLayoutModel.MazeSquare> down = layouts.get(0).getSquaresBetween(bottomLeft, topLeft);
        List<List<MazeLayoutModel.MazeSquare>> testLists = new ArrayList<>(Arrays.asList(right, left, up, down));
        for (List<MazeLayoutModel.MazeSquare> testList: testLists) {
            assertEquals(3, testList.size());
            for (MazeLayoutModel.MazeSquare square: testList) {
                assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, square);
            }
        }
    }

    private static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }
}
