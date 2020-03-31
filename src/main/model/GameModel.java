package model;

import exceptions.IncorrectGridIterationException;
import exceptions.GridPositionOutOfBoundsException;
import grid.Grid;
import grid.GridPosition;
import ui.SquareDisplayData;
import grid.GridArray;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class GameModel {
    private MazeModel maze;
    private GameEntity hero;
    private GameEntity minotaur;
    private GameEntity treasure;

    // EFFECTS: construct new game with given maze
    public GameModel(MazeModel maze) {
        this.maze = maze;
        // TODO: implement start point choice
        // currently using fixed start
        setupEntities(new GridPosition(7, 0));
    }

    // REQUIRES: hero's start position must be valid for given maze (based on maze size)
    // EFFECTS: construct new game with given maze
    public GameModel(MazeModel maze, GridPosition start) {
        // is public to allow testing
        this.maze = maze;
        setupEntities(start);
    }

    private void setupEntities(GridPosition heroStart) {
        this.hero = new GameEntity(GameEntity.EntityType.HERO, heroStart);
        this.minotaur = new GameEntity(GameEntity.EntityType.MINOTAUR, maze.getMinotaurStartPosition());
        this.treasure = new GameEntity(GameEntity.EntityType.TREASURE, maze.getTreasurePosition());
    }

    // EFFECTS: returns current position of hero in maze
    public GridPosition getHeroPosition() {
        return hero.getPosition();
    }

    // EFFECTS: returns current position of minotaur in maze
    public GridPosition getMinotaurPosition() {
        return minotaur.getPosition();
    }

    // EFFECTS: if move is valid, move hero to end location and return true
    //          return false if move is not valid
    public boolean moveHero(GridPosition end) throws GridPositionOutOfBoundsException {
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
    //              if both directions are equal, decides randomly
    //                  chooses horizontal when randomNumber is < 0.5
    public boolean moveMinotaur() throws GridPositionOutOfBoundsException {
        return moveMinotaur(random());
    }

    // MODIFIES: this
    // EFFECTS: moves minotaur according to minotaur movement rules,
    //          using given number to make diagonal movement decision instead of random
    //              horizontal if <0.5, vertical otherwise
    public boolean moveMinotaur(double randomNumber)
            throws GridPositionOutOfBoundsException { // TODO: needs proper handling
        GridPosition minotaurPosition = minotaur.getPosition();
        GridPosition heroPosition = hero.getPosition();
        GridPosition delta = heroPosition.subtract(minotaurPosition);
        // TODO: replace this with handling of NONE direction
        if (delta.equals(new GridPosition(0, 0))) {
            return true;
        }
        List<MazeModel.Direction> directions = decideDirection(delta, randomNumber);
        for (MazeModel.Direction direction : directions) {
            List<GridPosition> possibleMoves = maze.getValidMoves(minotaurPosition, direction);
            if (possibleMoves.size() > 0) {
                minotaur.setPosition(getBestMinotaurMove(possibleMoves, direction, heroPosition));
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns best minotaur move out of list of possible moves
    //          best = puts minotaur on hero, else puts minotaur orthogonal to hero, else greatest distance
    private GridPosition getBestMinotaurMove(
            List<GridPosition> possibleMoves, MazeModel.Direction direction, GridPosition heroPosition) {
        if (direction == MazeModel.Direction.LEFT || direction == MazeModel.Direction.RIGHT) {
            for (GridPosition possibleMove : possibleMoves) {
                if (possibleMove.getX() == heroPosition.getX()) {
                    return possibleMove;
                }
            }
        } else {
            for (GridPosition possibleMove : possibleMoves) {
                if (possibleMove.getY() == heroPosition.getY()) {
                    return possibleMove;
                }
            }
        }
        return possibleMoves.get(possibleMoves.size() - 1);
    }

    // REQUIRES: at least one of x, y in delta is non-zero
    // TODO: add NONE direction when delta is 0
    // EFFECTS: returns list of correct directions for minotaur to move based on movement rules
    //          if orthogonal, move towards hero; list has only 1 element
    //          if diagonal, choose shorter move between x and y, or random if equal; list has 2 elements
    private List<MazeModel.Direction> decideDirection(GridPosition delta, double random) {
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
    //          list ordered based on minotaur movement rules (shortest orthagonal distance first)
    private List<MazeModel.Direction> decideDirectionDiagonal(GridPosition delta, double random) {
        List<MazeModel.Direction> deltas = new ArrayList<>();
        if (abs(delta.getX()) < abs(delta.getY())) {
            deltas.add(getDirectionFromDelta(new GridPosition(delta.getX(), 0)));
            deltas.add(getDirectionFromDelta(new GridPosition(0, delta.getY())));
        } else if (abs(delta.getY()) < abs(delta.getX())) {
            deltas.add(getDirectionFromDelta(new GridPosition(0, delta.getY())));
            deltas.add(getDirectionFromDelta(new GridPosition(delta.getX(), 0)));
        } else {
            if (random < 0.5) {
                deltas.add(getDirectionFromDelta(new GridPosition(delta.getX(), 0)));
                deltas.add(getDirectionFromDelta(new GridPosition(0, delta.getY())));
            } else {
                deltas.add(getDirectionFromDelta(new GridPosition(0, delta.getY())));
                deltas.add(getDirectionFromDelta(new GridPosition(delta.getX(), 0)));
            }
        }
        return deltas;
    }

    // REQUIRES: only one of x, y in delta is 0
    // EFFECTS: returns Direction corresponding to given delta
    private MazeModel.Direction getDirectionFromDelta(GridPosition delta) {
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
        return (hero.getPosition().equals(treasure.getPosition()));
    }

    // EFFECTS: return true if player has lost the game, false if not
    //          player loses if:
    //              - minotaur moves onto hero's position
    public boolean checkForLoss() {
        return (hero.getPosition().equals(minotaur.getPosition()));
    }

    // EFFECTS: registers outcome of this game to this maze for storage, then returns total plays on this maze
    public int registerOutcome(MazeModel.Outcome outcome) {
        maze.registerOutcome(outcome);
        return maze.getTotalPlays();
    }

    // EFFECTS: return list of SquareDisplayData instances to display the current game state
    public Grid<SquareDisplayData> display() throws GridPositionOutOfBoundsException, IncorrectGridIterationException {
        Grid<SquareDisplayData> display = maze.displayMaze();
        overlayGameElement(treasure, display);
        overlayGameElement(hero, display);
        overlayGameElement(minotaur, display);
        return display;
    }

    // MODIFIES: display
    // EFFECTS: return new list of display data with game element added at given position
    private void overlayGameElement(GameEntity entity, Grid<SquareDisplayData> display) {
        GridPosition position = entity.getPosition();
        SquareDisplayData squareDisplay = display.get(position);
        squareDisplay.addEntityType(entity.getEntityType());
        display.set(position, squareDisplay);
    }
}
