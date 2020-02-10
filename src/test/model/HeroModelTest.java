package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HeroModelTest {
    HeroModel hero;

    @BeforeEach
    public void beforeEach() {
        hero = new HeroModel(new PositionModel(3, 5));
    }

    @Test
    public void testInit() {
        PositionModel heroPosition = hero.getPosition();
        assertEquals(3, heroPosition.getX());
        assertEquals(5, heroPosition.getY());
    }

    @Test
    public void testSetPosition() {
        hero.setPosition(new PositionModel(2, 1));
        assertEquals(2, hero.getPosition().getX());
        assertEquals(1, hero.getPosition().getY());
    }
}
