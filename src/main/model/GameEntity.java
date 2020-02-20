package model;

public class GameEntity {
    protected PositionModel position;

    public GameEntity(PositionModel start) {
        this.position = start;
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
}
