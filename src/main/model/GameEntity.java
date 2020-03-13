package model;

public class GameEntity {

    public enum EntityType {
        HERO,
        MINOTAUR,
        TREASURE
    }

    private PositionModel position;
    private EntityType entityType;

    public GameEntity(EntityType entityType, PositionModel start) {
        this.position = start;
        this.entityType = entityType;
    }

    // EFFECTS: returns current position of entity
    public PositionModel getPosition() {
        return position;
    }

    // MODIFIES: this
    // EFFECTS: sets position of entity
    public void setPosition(PositionModel position) {
        this.position = position;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
