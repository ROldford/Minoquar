package model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class GameModel {
    public static final char TREASURE_CHAR = "O".charAt(0);

    private MazeModel maze;
    // TODO: Should mazeDisplay be static or final to make it set-once read-only?
    private List<String> mazeDisplay;
    private HeroModel hero;
    private MinotaurModel minotaur;
    private PositionModel treasure;

    // REQUIRES: hero's start position must be valid for given maze (based on maze size)
    // EFFECTS: construct new game with given maze and hero start position
    public GameModel(MazeModel maze, PositionModel start) {
        this.maze = maze;
        this.mazeDisplay = maze.displayMaze();
        this.hero = new HeroModel(start);
        this.minotaur = new MinotaurModel(maze.getMinotaurStartPosition());
//        this.minotaur = new MinotaurModel(new PositionModel(10, 10));
        this.treasure = maze.getTreasurePosition();
    }

    // EFFECTS: returns current position of hero in maze
    public PositionModel getHeroPosition() {
        return hero.getPosition();
    }

    // EFFECTS: returns current position of minotaur in maze
    public PositionModel getMinotaurPosition() {
        return minotaur.getPosition();
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

    // MODIFIES: this
    // EFFECTS: moves minotaur according to minotaur movement rules
    //          can tunnel through many WALL squares
    //          must follow all other movement rules (ie. passage moves stops at wall, etc.)
    //          will move orthogonally towards hero
    //          if diagonal to hero, will move whichever direction has the smallest delta to hero
    //              will end move so it is orthogonal to hero if possible
    //              if both directions are equal, chooses based on given random number (horizontal if <0.5)
    public boolean moveMinotaur(double random) {
        PositionModel minotaurPosition = minotaur.getPosition();
        PositionModel heroPosition = hero.getPosition();
        PositionModel delta = heroPosition.subtract(minotaurPosition);
        if (delta.equals(new PositionModel(0, 0))) {
            return true;
        }
        List<MazeModel.Direction> directions = decideDirection(delta, random);
        for (MazeModel.Direction direction : directions) {
            List<PositionModel> possibleMoves = maze.getValidMoves(minotaurPosition, direction);
            if (possibleMoves.size() > 0) {
                minotaur.setPosition(getBestMinotaurMove(possibleMoves, direction, heroPosition));
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns best minotaur move out of list of possible moves
    //          best = puts minotaur on hero, else puts minotaur orthogonal to hero, else greatest distance
    private PositionModel getBestMinotaurMove(
            List<PositionModel> possibleMoves, MazeModel.Direction direction, PositionModel heroPosition) {
        if (direction == MazeModel.Direction.LEFT || direction == MazeModel.Direction.RIGHT) {
            for (PositionModel possibleMove : possibleMoves) {
                if (possibleMove.getX() == heroPosition.getX()) {
                    return possibleMove;
                }
            }
        } else {
            for (PositionModel possibleMove : possibleMoves) {
                if (possibleMove.getY() == heroPosition.getY()) {
                    return possibleMove;
                }
            }
        }
        return possibleMoves.get(possibleMoves.size() - 1);
    }

    // REQUIRES: at least one of x, y in delta is non-zero
    // EFFECTS: returns list of correct directions for minotaur to move based on movement rules
    //          if orthogonal, move towards hero; list has only 1 element
    //          if diagonal, choose shorter move between x and y, or random if equal; list has 2 elements
    private List<MazeModel.Direction> decideDirection(PositionModel delta, double random) {
        List<MazeModel.Direction> directions;
        if (delta.getX() != 0 && delta.getY() != 0) {
            directions = decideDirectionDiagonal(delta, random);
        } else {
            directions = new ArrayList<>();
            directions.add(getDirectionFromDelta(delta));
        }
        return directions;
    }

    // REQUIRES: both x and y in delta are non-zero
    // EFFECTS: returns list of correct directions for minotaur when diagonal to hero
    private List<MazeModel.Direction> decideDirectionDiagonal(PositionModel delta, double random) {
        List<MazeModel.Direction> deltas = new ArrayList<>();
        if (abs(delta.getX()) < abs(delta.getY())) {
            deltas.add(getDirectionFromDelta(new PositionModel(delta.getX(), 0)));
            deltas.add(getDirectionFromDelta(new PositionModel(0, delta.getY())));
        } else if (abs(delta.getY()) < abs(delta.getX())) {
            deltas.add(getDirectionFromDelta(new PositionModel(0, delta.getY())));
            deltas.add(getDirectionFromDelta(new PositionModel(delta.getX(), 0)));
        } else {
            if (random < 0.5) {
                deltas.add(getDirectionFromDelta(new PositionModel(delta.getX(), 0)));
                deltas.add(getDirectionFromDelta(new PositionModel(0, delta.getY())));
            } else {
                deltas.add(getDirectionFromDelta(new PositionModel(0, delta.getY())));
                deltas.add(getDirectionFromDelta(new PositionModel(delta.getX(), 0)));
            }
        }
        return deltas;
    }

    // REQUIRES: only one of x, y in delta is 0
    // EFFECTS: returns Direction corresponding to given delta
    private MazeModel.Direction getDirectionFromDelta(PositionModel delta) {
        if (delta.getX() != 0) {
            if (delta.getX() > 0) {
                return MazeModel.Direction.RIGHT;
            } else {
                return MazeModel.Direction.LEFT;
            }
        } else {
            if (delta.getY() > 0) {
                return MazeModel.Direction.DOWN;
            } else {
                return MazeModel.Direction.UP;
            }
        }
    }

    // EFFECTS: returns true if player has won the game, false if not
    //          player wins if:
    //              - hero has captured the treasure
    public boolean checkForWin() {
        return (hero.getPosition().equals(treasure));
    }

    // EFFECTS: return true if player has lost the game, false if not
    //          player loses if:
    //              - minotaur moves onto hero's position
    public boolean checkForLoss() {
        return (hero.getPosition().equals(minotaur.getPosition()));
    }

    // EFFECTS: return list of strings to display the current game state
    public List<String> display() {
        List<String> display = new ArrayList<>(mazeDisplay);
        display = overlayGameElement(TREASURE_CHAR, treasure, display);
        display = overlayGameElement(HeroModel.HERO_CHAR, hero.getPosition(), display);
        display = overlayGameElement(MinotaurModel.MINO_CHAR, minotaur.getPosition(), display);
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

    // EFFECTS: makes new string where character at index is replaced with new character
    // Based on code at https://www.baeldung.com/java-replace-character-at-index
    private String replaceCharAtIndex(String str, int index, char newChar) {
        StringBuilder newString = new StringBuilder(str);
        newString.setCharAt(index, newChar);
        return newString.toString();
    }
}
