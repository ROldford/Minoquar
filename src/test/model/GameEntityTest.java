package model;

import grid.GridPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameEntityTest {
    GameEntity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new GameEntity(GameEntity.EntityType.HERO, new GridPosition(3, 5));
    }

    @Test
    public void testInit() {
        GridPosition entityPosition = entity.getPosition();
        assertEquals(3, entityPosition.getX());
        assertEquals(5, entityPosition.getY());
        assertEquals(GameEntity.EntityType.HERO, entity.getEntityType());
    }

    @Test
    public void testSetPosition() {
        entity.setPosition(new GridPosition(2, 1));
        assertEquals(2, entity.getPosition().getX());
        assertEquals(1, entity.getPosition().getY());
    }


}
