package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionModelTest {
    private PositionModel position;

    @Test
    public void testInit() {
        this.position = new PositionModel(6, 28);
        assertEquals(6, position.getX());
        assertEquals(28, position.getY());
    }
}
