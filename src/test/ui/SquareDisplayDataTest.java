package ui;

import model.GameEntity;
import model.MazeLayoutModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareDisplayDataTest {
    SquareDisplayData emptyWall;
    SquareDisplayData emptyPassage;
    SquareDisplayData passageWithEntities;

    @BeforeEach
    void beforeEach() {
        emptyWall = new SquareDisplayData(MazeLayoutModel.MazeSquare.WALL);
        emptyPassage = new SquareDisplayData(MazeLayoutModel.MazeSquare.PASSAGE);
        List<GameEntity.EntityType> entities = new ArrayList<>(Arrays.asList(
                GameEntity.EntityType.HERO, GameEntity.EntityType.TREASURE));
        passageWithEntities = new SquareDisplayData(MazeLayoutModel.MazeSquare.PASSAGE, entities);
    }

    @Test
    void testInit() {
        assertEquals(MazeLayoutModel.MazeSquare.WALL, emptyWall.getSquareStatus());
        assertEquals(0, emptyWall.getEntityTypes().size());
        assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, emptyPassage.getSquareStatus());
        assertEquals(0, emptyPassage.getEntityTypes().size());
        assertEquals(MazeLayoutModel.MazeSquare.PASSAGE, passageWithEntities.getSquareStatus());
        assertEquals(2, passageWithEntities.getEntityTypes().size());
    }

    @Test
    void testAddEntityType() {
        // successful addition
        emptyPassage.addEntityType(GameEntity.EntityType.HERO);
        List<GameEntity.EntityType> entityTypeList = emptyPassage.getEntityTypes();
        assertEquals(1, entityTypeList.size());
        assertTrue(entityTypeList.contains(GameEntity.EntityType.HERO));
        // unsuccessful addition
        passageWithEntities.addEntityType(GameEntity.EntityType.HERO);
        entityTypeList = passageWithEntities.getEntityTypes();
        assertEquals(2, entityTypeList.size());
    }

    @Test
    void testRemoveEntityType() {
        // successful removal
        passageWithEntities.removeEntityType(GameEntity.EntityType.TREASURE);
        List<GameEntity.EntityType> entityTypeList = passageWithEntities.getEntityTypes();
        assertEquals(1, entityTypeList.size());
        assertTrue(entityTypeList.contains(GameEntity.EntityType.HERO));
        assertFalse(entityTypeList.contains(GameEntity.EntityType.TREASURE));
        // unsuccessful removal
        emptyPassage.removeEntityType(GameEntity.EntityType.TREASURE);
        entityTypeList = emptyPassage.getEntityTypes();
        assertEquals(0, entityTypeList.size());
    }

    @Test
    void testEqualsAndHashCode() {
        SquareDisplayData newEmptyWall = new SquareDisplayData(MazeLayoutModel.MazeSquare.WALL);
        List<GameEntity.EntityType> newEntities = new ArrayList<>(Arrays.asList(
                GameEntity.EntityType.HERO, GameEntity.EntityType.TREASURE));
        SquareDisplayData newPassageWithEntities = new SquareDisplayData(MazeLayoutModel.MazeSquare.PASSAGE, newEntities);
        String notDisplayData = "I'm not even the same object type!";
        assertEquals(emptyWall, emptyWall);
        assertEquals(emptyWall.hashCode(), emptyWall.hashCode());
        assertNotEquals(emptyWall, null);
        assertNotEquals(emptyWall, notDisplayData);
        assertEquals(emptyWall, newEmptyWall);
        assertEquals(emptyWall.hashCode(), newEmptyWall.hashCode());
        assertEquals(passageWithEntities, newPassageWithEntities);
        assertEquals(passageWithEntities.hashCode(), newPassageWithEntities.hashCode());
        assertNotEquals(emptyWall, emptyPassage);
        assertNotEquals(emptyWall.hashCode(), emptyPassage.hashCode());
        assertNotEquals(emptyPassage, passageWithEntities);
        assertNotEquals(emptyPassage.hashCode(), passageWithEntities.hashCode());
    }
}