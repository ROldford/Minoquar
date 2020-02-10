package model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private MazeModel maze;
    // TODO: Should this be static or final to make it set-once read-only?
    private List<String> mazeDisplay;
    private HeroModel hero;

    // REQUIRES: hero's start position must be valid for given maze (based on maze size)
    // EFFECTS: construct new game with given maze and hero start position
    public GameModel(MazeModel maze, PositionModel start) {
        this.maze = maze;
        this.mazeDisplay = maze.displayMaze();
        this.hero = new HeroModel(start);
    }

    // EFFECTS: return list of strings to display the current game state
    public List<String> display() {
        List<String> display = new ArrayList<>();
        // TODO: Extract this into new helper method
        for (int i = 0; i < mazeDisplay.size(); i++) {
            if (hero.getPosition().getY() == i) {
                String newRow = replaceCharAtIndex(
                        mazeDisplay.get(i),
                        hero.getPosition().getX(),
                        hero.HERO_CHAR);
                display.add(newRow);
            } else {
                display.add(mazeDisplay.get(i));
            }
        }
        return display; //stub
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
