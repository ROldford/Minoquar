package model;

// represents the current position and state of Hero in a game
public class HeroModel extends GameEntity {
    public static final char HERO_CHAR = "☺".charAt(0);

    // REQUIREMENTS: start position must be on PASSAGE square
    // EFFECTS: constructs a new hero at given start position
    public HeroModel(PositionModel start) {
        super(start);
    }

}
