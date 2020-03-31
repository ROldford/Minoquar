package model;

import grid.GridPosition;

public class GameEntity {

    public enum EntityType {
        HERO,
        MINOTAUR,
        TREASURE
    }

    private GridPosition position;
    private EntityType entityType;

    public GameEntity(EntityType entityType, GridPosition start) {
        this.position = start;
        this.entityType = entityType;
    }

    // EFFECTS: returns current position of entity
    public GridPosition getPosition() {
        return position;
    }

    // MODIFIES: this
    // EFFECTS: sets position of entity
    public void setPosition(GridPosition position) {
        this.position = position;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
