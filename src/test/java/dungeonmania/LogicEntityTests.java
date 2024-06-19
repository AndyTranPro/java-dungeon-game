package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.countEntityOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;

public class LogicEntityTests {
    @Test
    @DisplayName("Test the logic behavior of light bulb")
    public void testLogicLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();

        //test and logic
        DungeonResponse DungonRes = dmc.newGame("lightbulbAND", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));

        DungonRes = dmc.newGame("lightbulbANDtwo", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        DungonRes = dmc.newGame("lightbulbANDthree", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));

        //test or logic
        DungonRes = dmc.newGame("lightbulbOR", "c_movementTest_testMovementDown");
        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));

        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        //test xor logic
        DungonRes = dmc.newGame("lightbulbXOR", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));

        DungonRes = dmc.newGame("lightbulbXORtwo", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        //test co_and logic
        DungonRes = dmc.newGame("lightbulbCOAND", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));

        DungonRes = dmc.newGame("lightbulbCOANDtwo", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));

        DungonRes = dmc.newGame("lightbulbCOANDthree", "c_movementTest_testMovementDown");

        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));        
    } 

    @Test
    @DisplayName("Test the logic behavior of logic bomb")
    public void testLogicBomb() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();

        //test and logic
        DungeonResponse DungonRes = dmc.newGame("logicBombAND", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.LEFT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);
        dmc.tick(getInventory(DungonRes, "bomb").get(0).getId());

        assertEquals(1, countEntityOfType(DungonRes, "switch"));

        dmc = new DungeonManiaController();


        DungonRes = dmc.newGame("logicBombANDtwo", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.LEFT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);
        dmc.tick(getInventory(DungonRes, "bomb").get(0).getId());

        assertEquals(0, countEntityOfType(DungonRes, "bomb"));

        dmc = new DungeonManiaController();
        DungonRes = dmc.newGame("logicBombANDthree", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.LEFT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);
        DungonRes = dmc.tick(getInventory(DungonRes, "bomb").get(0).getId());

        assertEquals(1, countEntityOfType(DungonRes, "bomb"));
    }

    @Test
    @DisplayName("Test the logic behavior of logic switch door")
    public void testLogicSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        //test and logic
        DungeonResponse DungonRes = dmc.newGame("logicSwitchDoorAND", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(new Position(3, -1), getPlayer(DungonRes).get().getPosition());

        dmc = new DungeonManiaController();
        //test and logic
        DungonRes = dmc.newGame("logicSwitchDoorANDtwo", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.DOWN);

        assertEquals(new Position(3, 0), getPlayer(DungonRes).get().getPosition());
        dmc.tick(Direction.DOWN);
        DungonRes = dmc.tick(Direction.LEFT);
        DungonRes = dmc.tick(Direction.LEFT);
        DungonRes = dmc.tick(Direction.UP);
        DungonRes = dmc.tick(Direction.RIGHT);

        dmc = new DungeonManiaController();
        //test and logic
        DungonRes = dmc.newGame("logicSwitchDoorANDtwo", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(1, 0), getPlayer(DungonRes).get().getPosition());
    }

    @Test
    @DisplayName("Test the logic behavior of logic switch")
    public void testLogicSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        //test and logic
        DungeonResponse DungonRes = dmc.newGame("logicSwitch", "c_movementTest_testMovementDown");
        DungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_off"));
        DungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(DungonRes, "light_bulb_on"));
    }
}
