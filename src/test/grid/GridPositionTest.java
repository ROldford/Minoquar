package grid;

import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GridPositionTest {
    private GridPosition position;
    private int posX;
    private int posY;

    @BeforeEach
    public void beforeEach() {
        this.posX = 6;
        this.posY = 28;
        this.position = new GridPosition(posX, posY);
    }

    @Test
    public void testInit() {
        assertEquals(posX, position.getX());
        assertEquals(posY, position.getY());
    }

    @Test
    public void testAdd() {
        List<GridPosition> deltas = new ArrayList<>(Arrays.asList(
                new GridPosition(0, 0),
                new GridPosition(0, 7),
                new GridPosition(0, -7),
                new GridPosition(7, 0),
                new GridPosition(-7, 0),
                new GridPosition(14, 5)
        ));
        List<GridPosition> expectedPositions = new ArrayList<>(Arrays.asList(
                new GridPosition(posX, posY),
                new GridPosition(posX, posY + 7),
                new GridPosition(posX, posY + -7),
                new GridPosition(posX + 7, posY),
                new GridPosition(posX + -7, posY),
                new GridPosition(posX + 14, posY + 5)
        ));
        Utilities.iterateSimultaneously(
                expectedPositions, deltas,
                (GridPosition expected, GridPosition delta) ->
                        assertEquals(expected, position.add(delta)));
    }

    @Test
    void testSubtract() {
        List<GridPosition> others = new ArrayList<>(Arrays.asList(
                new GridPosition(0, 0),
                new GridPosition(0, 7),
                new GridPosition(0, -7),
                new GridPosition(7, 0),
                new GridPosition(-7, 0),
                new GridPosition(14, 5)
        ));
        List<GridPosition> expectedPositions = new ArrayList<>(Arrays.asList(
                new GridPosition(posX, posY),
                new GridPosition(posX, posY - 7),
                new GridPosition(posX, posY - -7),
                new GridPosition(posX - 7, posY),
                new GridPosition(posX - -7, posY),
                new GridPosition(posX - 14, posY - 5)
        ));
        Utilities.iterateSimultaneously(
                expectedPositions, others,
                (GridPosition expected, GridPosition other) -> assertEquals(
                        expected, position.subtract(other)));
    }

    @Test
    void testEqualsHashCode() {
        assertNotEquals(position, null);
        assertNotEquals(position, "not same type");
        assertEquals(position, position);
        assertEquals(position.hashCode(), position.hashCode());
        GridPosition other = new GridPosition(posX, posY);
        assertEquals(position, other);
        assertEquals(position.hashCode(), other.hashCode());
    }

}
