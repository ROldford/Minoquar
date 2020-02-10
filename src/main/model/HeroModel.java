package model;

// represents the current position and state of Hero in a game
public class HeroModel {
    private PositionModel position;

    // REQUIREMENTS: start position must be on PASSAGE square
    // EFFECTS: constructs a new hero at given start position
    public HeroModel(PositionModel start) {
        this.position = start;
    }

    // EFFECTS: returns current position of hero
    public PositionModel getPosition() {
        return position;
    }

    // MODIFIES: this
    // EFFECTS: sets position of hero
    public void setPosition(PositionModel position) {
        this.position = position;
    }
}
