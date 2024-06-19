package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.EntityResolver;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.Spider;
import dungeonmania.player.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BattleTests {
    @Test
    @DisplayName("Win battle")
    public void testWinBattle(){
        // walk into mercenary with config of player health damage at 100 and mercenary health at 1
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_surrounding_Mercenary", "c_battleTest_PlayerWin");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<EntityResponse> entities = response.getEntities();
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
    @Test
    @DisplayName("Lose battle")
    public void testLoseBattle(){
        // walk into mercenary with config of player health at 1 and mercenary damage at 100
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_surrounding_Mercenary", "c_battleTest_Playerdeath");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<EntityResponse> entities = response.getEntities();
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "player");
        }
    }
    @Test
    @DisplayName("Test bow damage in battle")
    public void testBowBattle() throws IllegalArgumentException, InvalidActionException{
        // Player will walk into mercenary and win in one round if the bow multiplies damage by two
        // i.e. player damage at 5 with bow will perfectly kill a mercenary with 2 hp in 1 round
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_bow", "c_battleTest_PerfectWin");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("bow");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        // only one round will be had
        assertTrue(response.getBattles().get(0).getRounds().size() == 1);
        List<EntityResponse> entities = response.getEntities();
        // player lives and mercenary dies
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
    @Test
    @DisplayName("Test sword damage in battle")
    public void testSwordBattle(){
        // Player will walk into mercenary and win in one round if the sword adds base damage of player
        // i.e. player damage at 5 with sword of damage 5 will perfectly kill a mercenary with 2 hp in 1 round
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_sword", "c_battleTest_PerfectWin");
        controller.tick(Direction.RIGHT);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        // only one round will be had
        assertTrue(response.getBattles().get(0).getRounds().size() == 1);
        List<EntityResponse> entities = response.getEntities();
        // player lives and mercenary dies
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
    @Test
    @DisplayName("Test shield defense in battle")
    public void testShieldBattle() throws IllegalArgumentException, InvalidActionException{
        // Player will walk into mercenary and win with 1 hp because the shield will negate its damage
        // i.e. player with any positive damage at 1hp with shield of defense 5 will beat a mercenary with 5 damage of any health
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_shield", "c_battleTest_player_1hp");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("shield");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        // player will never die
        assertTrue(response.getBattles().get(0).getRounds().size() > 1);
        List<EntityResponse> entities = response.getEntities();
        // player lives and mercenary dies
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
    @Test
    @DisplayName("Test invincibility potion in battle")
    public void testInvincibilityBattle() throws IllegalArgumentException, InvalidActionException{  
        // Player and mercenary encased by walls and can only move horizontally
        // player will never beat mercenary in given config unless player is truly invincible
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invincibility", "c_battleTest_permanentPotion");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        String id = response.getInventory().get(0).getId();
        controller.tick(id);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        // player will win in one round
        assertTrue(response.getBattles().get(0).getRounds().size() == 1);
        List<EntityResponse> entities = response.getEntities();
        // player lives and mercenary dies
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
    @Test
    @DisplayName("Test invisibility potion in battle")
    public void testInvisibilityBattle() throws IllegalArgumentException, InvalidActionException{
        // Player and mercenary encased by walls and can only move horizontally
        // player will never get into battle with mercenary even if they move past each other
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invisibility", "c_battleTest_permanentPotion");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        String id = response.getInventory().get(0).getId();
        controller.tick(id);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        // dungeon will have no battles
        assertTrue(response.getBattles().size() == 0);
        List<EntityResponse> entities = response.getEntities();
        // player and mercenary alive
        Boolean player_exists = false;
        Boolean mercenary_exists = false;
        for (EntityResponse e : entities) {
            if (e.getType() == "mercenary")
                mercenary_exists = true;
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
        assertTrue(mercenary_exists);
    }
    @Test
    @DisplayName("Test Midnight Armour in battle")
    public void testMidnightArmourBattle() throws IllegalArgumentException, InvalidActionException{
        // Player will walk into mercenary and win with 1 hp because the midnight armour will negate its damage
        // i.e. player of 1hp with midngiht armour of attack and defense 5 will beat a mercenary with 5 damage of 4 health in 2 rounds
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_MA", "c_battleTest_MA");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("midnight_armour");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        // Battle will last two rounds
        assertTrue(response.getBattles().get(0).getRounds().size() == 2);
        List<EntityResponse> entities = response.getEntities();
        // player lives and mercenary dies
        Boolean player_exists = false;
        for (EntityResponse e : entities) {
            assertTrue(e.getType() != "mercenary");
            if (e.getType() == "player")
                player_exists = true;
        }
        assertTrue(player_exists);
    }
}
