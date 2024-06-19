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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.io.File;
import java.util.Objects;

public class PresistenceTests {
    @Test
    @DisplayName("check if everything remains in the correct position and the loaded game is able to continue to play")
    public void testCheckIfPositionCorrect(){
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        
        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse initPlayer = getPlayer(actualDungonRes).get();

        //get player position
        Position player = initPlayer.getPosition();
        dmc.saveGame("temp");


        //change to another dungeon controller
        DungeonManiaController dmcNew = new DungeonManiaController();
        dmcNew.loadGame("temp");
        DungeonResponse initDungonResNew = dmc.getDungeonResponseModel();
        EntityResponse initPlayerNew = getPlayer(initDungonResNew).get();
        Position playerNew = initPlayerNew.getPosition();

        assertEquals(player, playerNew);

        assertDoesNotThrow(() -> dmcNew.tick(Direction.DOWN));


        //test exception
        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("non-existGame"));

        // //delete test files
        // File directory = new File("src/main/resources/savedGames");
        // for (File file: Objects.requireNonNull(directory.listFiles())) {
        //     file.delete();
        // }
    }

}
