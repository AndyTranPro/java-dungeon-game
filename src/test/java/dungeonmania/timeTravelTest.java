package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class timeTravelTest {
    @Test
    @DisplayName("Test exceptions of time travel rewind") 
    void testTTexceptions() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravel", "c_movementTest_testMovementDown");

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.rewind(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.rewind(5);
        });
    }

    @Test
    @DisplayName("Test back 1 tick of time_turner ") 
    void testTToneticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravel", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.rewind(1);
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(1, 0), pos);
    }

    @Test
    @DisplayName("Test back 5 ticks of time_turner ") 
    void testTTfiveticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravel", "c_movementTest_testMovementDown");
        for (int i = 0; i < 5; i++) {
            DungonRes = dmc.tick(Direction.RIGHT);
        }
        DungonRes = dmc.rewind(5);
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(1, 0), pos);
    }

    @Test
    @DisplayName("Test back 30 ticks when go into a time travel portal") 
    void testTTportal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravelPortal", "c_movementTest_testMovementDown");
        for (int i = 0; i < 35; i++) {
            DungonRes = dmc.tick(Direction.RIGHT);
        }
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(6, 0), pos);
    }

    @Test
    @DisplayName("Test back to original tick when go into a time travel portal and ticks less than 30") 
    void testTTportallessthanThrity() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravelPortaltwo", "c_movementTest_testMovementDown");
        for (int i = 0; i < 25; i++) {
            DungonRes = dmc.tick(Direction.RIGHT);
        }
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(1, 0), pos);
    }

    @Test
    @DisplayName("Test an older play will be presented in the dungeon after time travel") 
    void testTTolderplayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravel", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.rewind(1);
        Position playerPos = getPlayer(DungonRes).get().getPosition();
        assertEquals(new Position(1, 0), playerPos);

        Position pos = getEntities(DungonRes, "older_player").get(0).getPosition();
        assertEquals(new Position(0, 0), pos);
    }

    @Test
    @DisplayName("Test an older play will be presented in the dungeon after time travel and it would continue its behaviour") 
    void testOldplayerContinueMoving() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravelPortaltwo", "c_movementTest_testMovementDown");
        for (int i = 0; i < 25; i++) {
            DungonRes = dmc.tick(Direction.RIGHT);
        }
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(1, 0), pos);

        Position oldPlayerPos = getEntities(DungonRes, "older_player").get(0).getPosition();
        assertEquals(new Position(0, 0), oldPlayerPos);

        Position playerPos = getPlayer(DungonRes).get().getPosition();
        assertEquals(new Position(24, 0), playerPos);

        DungonRes = dmc.tick(Direction.DOWN);
        playerPos = getPlayer(DungonRes).get().getPosition();
        assertEquals(new Position(24, 1), playerPos);

        pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(2, 0), pos);

        oldPlayerPos = getEntities(DungonRes, "older_player").get(0).getPosition();
        assertEquals(new Position(1, 0), oldPlayerPos);
    }

    @Test
    @DisplayName("Test an older play will be presented in the dungeon after time travel and it would continue its behaviour") 
    void testOldplayerContinueMoving2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("timeTravelPortaltwo", "c_movementTest_testMovementDown");
        for (int i = 0; i < 25; i++) {
            DungonRes = dmc.tick(Direction.RIGHT);
        }
        Position pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(1, 0), pos);

        Position oldPlayerPos = getEntities(DungonRes, "older_player").get(0).getPosition();
        assertEquals(new Position(0, 0), oldPlayerPos);

        Position playerPos = getPlayer(DungonRes).get().getPosition();
        assertEquals(new Position(24, 0), playerPos);

        DungonRes = dmc.tick(Direction.DOWN);
        playerPos = getPlayer(DungonRes).get().getPosition();
        assertEquals(new Position(24, 1), playerPos);

        pos = getEntities(DungonRes, "boulder").get(0).getPosition();
        assertEquals(new Position(2, 0), pos);

        oldPlayerPos = getEntities(DungonRes, "older_player").get(0).getPosition();
        assertEquals(new Position(1, 0), oldPlayerPos);
    }
}
