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
import java.util.Optional;

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


public class PlayerStateTest {

    @Test
    public void testInvinsiblePotion() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionTest_basicInvincible", "c_movementTest_testMovementDown");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        assertEquals(pos, getEntities(res, "mercenary").get(0).getPosition());


        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        // make sure the player have picked up the potion
        String invinciblePotionId = getInventory(res, "invincibility_potion").get(0).getId();
        //make sure the mercenary working property, so it get closer to the player
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        //consume the potion
        res = dmc.tick(invinciblePotionId);
        assertEquals(pos, getEntities(res, "mercenary").get(0).getPosition());

        // make sure the mercenary run away from player for one round(duration of potion for this config)
        res = dmc.tick(Direction.LEFT);
        Position expectedPosition2 = pos.translateBy(Direction.RIGHT);
        assertEquals(expectedPosition2, getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);
        Position expectedPosition3 = expectedPosition2.translateBy(Direction.RIGHT);
        assertEquals(expectedPosition3, getEntities(res, "mercenary").get(0).getPosition());

        //make sure the mercenary run toward the player after the potion effect is ended
        res = dmc.tick(Direction.LEFT);
        assertEquals(expectedPosition2, getEntities(res, "mercenary").get(0).getPosition());
    }
    @Test
    public void testInvisiblePotion() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionTest_basicInvisible", "c_movementTest_testMovementDown");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        // make sure the player have picked up the potion
        String invisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        //make sure the mercenary working property, so it get closer to the player
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        //consume the potion
        res = dmc.tick(invisibilityPotionId);
        res = dmc.tick(Direction.RIGHT);
        // make sure the mercenary still alive after the potion effect as there will be no battle
        pos = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.tick(invisibilityPotionId));
    }

    @Test
    public void testInvisiblePotionNInvinciblePotion() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionTest_InvincibleNInvisible", "c_potionTest");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        res = dmc.tick(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);

        // make sure the player have picked up the potion
        String invisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        String invincibilityPotionId = getInventory(res, "invincibility_potion").get(0).getId();
        //consume the potion
        res = dmc.tick(invisibilityPotionId);
        res = dmc.tick(invincibilityPotionId);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        String assassinId = getEntities(res, "assassin").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(assassinId));
    }

}
