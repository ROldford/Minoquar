package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionModelTest {

    @Test
    public void testInit() {
        PositionModel position = PositionModel.createNewInstance(6, 28);
        assertEquals(6, position.getX());
        assertEquals(28, position.getY());
    }
}
