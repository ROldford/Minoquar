package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Utilities.iterateSimultaneously(
                expectedPositions, deltas,
                (PositionModel expected, PositionModel delta) ->
                        assertEquals(expected, position.add(delta)));
    }

    @Test
    void testSubtract() {
        List<PositionModel> others = new ArrayList<>(Arrays.asList(
                new PositionModel(0, 0),
                new PositionModel(0, 7),
                new PositionModel(0, -7),
                new PositionModel(7, 0),
                new PositionModel(-7, 0),
                new PositionModel(14, 5)
        ));
        List<PositionModel> expectedPositions = new ArrayList<>(Arrays.asList(
                new PositionModel(posX, posY),
                new PositionModel(posX, posY - 7),
                new PositionModel(posX, posY - -7),
                new PositionModel(posX - 7, posY),
                new PositionModel(posX - -7, posY),
                new PositionModel(posX - 14, posY - 5)
        ));
        Utilities.iterateSimultaneously(
                expectedPositions, others,
                (PositionModel expected, PositionModel other) -> assertEquals(
                        expected, position.subtract(other)));
    }

    @Test
    void testEqualsHashCode() {
        assertNotEquals(position, null);
        assertNotEquals(position, "not same type");
        assertEquals(position, position);
        assertEquals(position.hashCode(), position.hashCode());
        PositionModel other = new PositionModel(posX, posY);
        assertEquals(position, other);
        assertEquals(position.hashCode(), other.hashCode());
    }

}
