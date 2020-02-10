package model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    public static final char TREASURE_CHAR = "O".charAt(0);

    private MazeModel maze;
    // TODO: Should mazeDisplay be static or final to make it set-once read-only?
    private List<String> mazeDisplay;
    private HeroModel hero;
    private PositionModel treasure;

    // REQUIRES: hero's start position must be valid for given maze (based on maze size)
    // EFFECTS: construct new game with given maze and hero start position
    public GameModel(MazeModel maze, PositionModel start) {
        this.maze = maze;
        this.mazeDisplay = maze.displayMaze();
        this.hero = new HeroModel(start);
        this.treasure = maze.getTreasurePosition();
    }

    // EFFECTS: return list of strings to display the current game state
    public List<String> display() {
        List<String> display = new ArrayList<>(mazeDisplay);
        display = overlayGameElement(TREASURE_CHAR, treasure, display);
        display = overlayGameElement(HeroModel.HERO_CHAR, hero.getPosition(), display);
        return display;
    }

    // EFFECTS: return new display list of strings with game element character overlaid at given position
    private List<String> overlayGameElement(char ch, PositionModel position, List<String> display) {
        List<String> overlaid = new ArrayList<>(display);
        String row = overlaid.get(position.getY());
        row = replaceCharAtIndex(row, position.getX(), ch);
        overlaid.set(position.getY(), row);
        return overlaid;
    }

    // EFFECTS: if move is valid, move hero to end location and return true
    //          return false if move is not valid
    public boolean moveHero(PositionModel end) {
        if (maze.isMoveValid(hero.getPosition(), end)) {
            hero.setPosition(end);
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: returns true if hero has captured the treasure, false if not
    public boolean checkForWin() {
        return (hero.getPosition().getX() == treasure.getX() && hero.getPosition().getY() == treasure.getY());
    }

    // EFFECTS: makes new string where character at index is replaced with new character
    // Based on code at https://www.baeldung.com/java-replace-character-at-index
    private String replaceCharAtIndex(String str, int index, char newChar) {
        StringBuilder newString = new StringBuilder(str);
        newString.setCharAt(index, newChar);
        return newString.toString();
    }

    // EFFECTS: returns current position of hero in maze
    public PositionModel getHeroPosition() {
        return hero.getPosition();
    }
}
