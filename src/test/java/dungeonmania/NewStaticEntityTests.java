package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.countEntityOfType;

public class NewStaticEntityTests {
    @Test
    @DisplayName("Test light bulb can be turned on by switch and turn off if switch turned off")
    public void testLightBulbBasicturnOn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("testLightBulb", "c_movementTest_testMovementDown");

        // move player right
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));
    }

    @Test
    @DisplayName("Test light bulb can be turned on by wire and turn off if wire turned off")
    public void testLightBulbBasicturnOnWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("testLightBulb2", "c_movementTest_testMovementDown");

        // move player right
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));
    }

    @Test
    @DisplayName("Test switch door can be turned on by switch and turn off if switch turned off")
    public void testswitchDoorBasicturnOnSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("testSwitchDoor", "c_movementTest_testMovementDown");

        // move player right
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "switch_door"));

        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, countEntityOfType(DungonRes, "switch_door"));
    }

    @Test
    @DisplayName("Test switch door can be turned on by wire and turn off if wire turned off")
    public void testswitchDoorBasicturnOnWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("testSwitchDoor2", "c_movementTest_testMovementDown");

        // move player right
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "switch_door"));

        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, countEntityOfType(DungonRes, "switch_door"));
    }
        
}
