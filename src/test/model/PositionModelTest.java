package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionModelTest {
    private PositionModel position;
    private int posX;
    private int posY;

    @BeforeEach
    public void beforeEach() {
        this.posX = 6;
        this.posY = 28;
        this.position = PositionModel.createNewInstance(posX, posY);
    }

    @Test
    public void testInit() {
        assertEquals(posX, position.getX());
        assertEquals(posY, position.getY());
    }

    @Test
    public void testToString() {
        String expected = String.format("(%d, %d)", posX, posY);
        assertTrue(position.toString().equals(expected));
    }
}
