package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class PositionModelTest {
    private PositionModel position;
    private int posX;
    private int posY;

    @BeforeEach
    public void beforeEach() {
        this.posX = 6;
        this.posY = 28;
        this.position = new PositionModel(posX, posY);
    }

    @Test
    public void testInit() {
        assertEquals(posX, position.getX());
        assertEquals(posY, position.getY());
    }

    @Test
    public void testAdd() {
        List<PositionModel> deltas = new ArrayList<>(Arrays.asList(
                new PositionModel(0, 0),
                new PositionModel(0, 7),
                new PositionModel(0, -7),
                new PositionModel(7, 0),
                new PositionModel(-7, 0),
                new PositionModel(14, 5)
        ));
        List<PositionModel> expectedPositions = new ArrayList<>(Arrays.asList(
                new PositionModel(posX, posY),
                new PositionModel(posX, posY + 7),
                new PositionModel(posX, posY + -7),
                new PositionModel(posX + 7, posY),
                new PositionModel(posX + -7, posY),
                new PositionModel(posX + 14, posY + 5)
        ));
        iterateSimultaneously(
                expectedPositions, deltas,
                (PositionModel expected, PositionModel delta) -> assertTrue(
                        samePosition(expected, position.add(delta))));
    }

    private boolean samePosition(PositionModel a, PositionModel b) {
        return (a.getX() == b.getX() && a.getY() == b.getY());
    }

    private static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }
}
