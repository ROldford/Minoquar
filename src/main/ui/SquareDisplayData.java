package ui;

import model.GameEntity;
import model.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// used to pass data about display of given maze square to UI objects
public class SquareDisplayData {
    private Layout.MazeSquare squareStatus;
    private List<GameEntity.EntityType> entityTypes;

    // EFFECTS: creates new display data object carrying square status and types of any entities present there
    public SquareDisplayData(Layout.MazeSquare mazeSquare, List<GameEntity.EntityType> entityTypes) {
        this.squareStatus = mazeSquare;
        this.entityTypes = entityTypes;
    }

    // EFFECTS: creates new display data object carrying square status with no entities
    public SquareDisplayData(Layout.MazeSquare mazeSquare) {
        this.squareStatus = mazeSquare;
        this.entityTypes = new ArrayList<>();
    }

    // EFFECT: returns status of this square
    public Layout.MazeSquare getSquareStatus() {
        return squareStatus;
    }

    // EFFECT: returns list of types of entities at this square
    public List<GameEntity.EntityType> getEntityTypes() {
        return entityTypes;
    }

    // EFFECT: adds new entity type to list if not already present
    public void addEntityType(GameEntity.EntityType entityType) {
        if (!entityTypes.contains(entityType)) {
            entityTypes.add(entityType);
        }
    }

    // EFFECT: removes entity type from list if present
    public void removeEntityType(GameEntity.EntityType entityType) {
        entityTypes.remove(entityType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SquareDisplayData that = (SquareDisplayData) o;
        return squareStatus == that.squareStatus
                && Objects.equals(entityTypes, that.entityTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(squareStatus, entityTypes);
    }
}
