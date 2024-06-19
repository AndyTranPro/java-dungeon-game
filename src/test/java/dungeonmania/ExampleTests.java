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


public class ExampleTests {
    @Test
    @DisplayName("Test the player can move down")
    public void testMovementDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        // assert after movement
        assertEquals(expectedPlayer, actualPlayer);
    }
    
    @Test
    @DisplayName("Test player can use a key to open and walk through a door")
    public void useKeyWalkThroughOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_useKeyWalkThroughOpenDoor", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getInventory(res, "key").size());

        // walk through door and check key is gone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "key").size());
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());
    }
    
    @Test
    @DisplayName("Test basic movement of spiders")
    public void basicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    public void testMercenaryBasicBattle(){
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionTest_basicInvincible", "c_movementTest_testMovementDown");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        // make sure the player have picked up the potion
        String invinciblePotionId = getInventory(res, "invincibility_potion").get(0).getId();
        //make sure the mercenary working property, so it get closer to the player
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        // make sure the mercenary is dead when player and mercenary meet
        res = dmc.tick(Direction.RIGHT);
        Position expectedPosition2 = pos.translateBy(Direction.RIGHT);
        assertEquals(true, getEntities(res, "mercenary").isEmpty());
    }

    @Test
    public void testMercenaryBribedDontBattleNDontOverlap() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribe", "c_movementTest_testMovementDown");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        String mercenaryId = getEntities(res, "mercenary").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));
        res = dmc.tick(Direction.RIGHT);
        Position expectedPosition2 = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition2, getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }
    }
        
    @Test
    @DisplayName("Test surrounding entities are removed when placing a bomb next to an active switch with config file bomb radius set to 2")
    public void placeBombRadius2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }
    
    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void andAll() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");

        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":exit"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(res));
    }

    private static DungeonResponse genericMercenarySequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicMercenary", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    @Test
    @DisplayName("Test basic battle calculations - mercenary - player loses")
    public void testHealthBelowZeroMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryPlayerDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, false, "c_battleTests_basicMercenaryPlayerDies");
    }


    @Test
    @DisplayName("Test basic battle calculations - mercenary - player wins")
    public void testRoundCalculationsMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }


    @Test
    @DisplayName("Test zomie Spawner geatures")
    public void testZombieSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        //no wall
        DungeonResponse res = controller.newGame("zombies", "bomb_radius_2");

        res = controller.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "zombie_toast").size());

        controller = new DungeonManiaController();
        res = controller.newGame("zombies", "c_battleTests_basicMercenaryMercenaryDies");

        res = controller.tick(Direction.DOWN);
        assertEquals(0, getEntities(res, "zombie_toast").size());
        //up wall
        controller = new DungeonManiaController();
        res = controller.newGame("zombie1", "bomb_radius_2");

        res = controller.tick(Direction.DOWN);
        assertEquals(1, getEntities(res, "zombie_toast").size());

        //down wall
        controller = new DungeonManiaController();
        res = controller.newGame("zombie2", "bomb_radius_2");

        res = controller.tick(Direction.DOWN);
        assertEquals(1, getEntities(res, "zombie_toast").size());

        //left wall
        controller = new DungeonManiaController();
        res = controller.newGame("zombie3", "bomb_radius_2");

        res = controller.tick(Direction.DOWN);
        assertEquals(1, getEntities(res, "zombie_toast").size());

        //all walls
        controller = new DungeonManiaController();
        res = controller.newGame("zombie4", "bomb_radius_2");

        res = controller.tick(Direction.DOWN);
        assertEquals(0, getEntities(res, "zombie_toast").size());

        controller = new DungeonManiaController();
        res = controller.newGame("zombie4", "spawn");

        res = controller.tick(Direction.DOWN);
        assertEquals(0, getEntities(res, "zombie_toast").size());
    }

    @Test
    @DisplayName("test portal features")
    public void testPortalFeature(){
        DungeonManiaController controller = new DungeonManiaController();
        
        DungeonResponse res = controller.newGame("portals", "bomb_radius_2"); 
        EntityResponse player = getPlayer(res).get();
        Position p = player.getPosition();
        res = controller.tick(Direction.RIGHT);
        assertNotEquals(p.translateBy(Direction.RIGHT), getPlayer(res).get().getPosition());

        //check up wall
        res = controller.newGame("portal1", "bomb_radius_2"); 
        p = getPlayer(res).get().getPosition();
        res = controller.tick(Direction.RIGHT);
        assertNotEquals(p.translateBy(Direction.RIGHT), getPlayer(res).get().getPosition()); 

        //check down wall
        res = controller.newGame("portal2", "bomb_radius_2"); 
        p = getPlayer(res).get().getPosition();
        res = controller.tick(Direction.RIGHT);
        assertNotEquals(p.translateBy(Direction.RIGHT), getPlayer(res).get().getPosition());

        //check left wall
        res = controller.newGame("portal3", "bomb_radius_2"); 
        p = getPlayer(res).get().getPosition();
        res = controller.tick(Direction.RIGHT);
        assertNotEquals(p.translateBy(Direction.RIGHT), getPlayer(res).get().getPosition());

        //check all walls
        //check down wall
        res = controller.newGame("portal4", "bomb_radius_2"); 
        p = getPlayer(res).get().getPosition();
        res = controller.tick(Direction.RIGHT);
        assertEquals(p, getPlayer(res).get().getPosition());
    }

    @Test
    public void testInteractError() {
        // test error for interact when given wrong input
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribe", "c_bribe_notEnoughAmount");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);
        // assert InvalidActionException for interact merenary as range is not enough
        String mercenaryId = getEntities(res, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercenaryId));

        assertThrows(IllegalArgumentException.class, () -> dmc.interact("null"));
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("1"));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        // assert InvalidActionException for interact merenary as money not enough
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercenaryId));
        res = dmc.tick(Direction.LEFT);
        Position expectedPosition2 = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition2, getEntities(res, "mercenary").get(0).getPosition());
    }


    @Test
    public void destoryZombieToastSpawner() {
        // test destroy zombie toast spawner with error handling
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_testZombieToastSpawner", "bomb_radius_2");
        String zombieToastSpawnerId = getEntities(res, "zombie_toast_spawner").get(0).getId();
        dmc.getSkin();
        dmc.getLocalisation();
        assertThrows(InvalidActionException.class, () -> dmc.interact(zombieToastSpawnerId));
        res = dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.interact(zombieToastSpawnerId));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.interact(zombieToastSpawnerId));
        dmc.dungeons();
        dmc.configs();
    }
}
