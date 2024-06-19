package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.collectableEntity.Key;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.Arrow;
import dungeonmania.inventoryItem.Bomb;
import dungeonmania.inventoryItem.Sword;
import dungeonmania.inventoryItem.Treasure;
import dungeonmania.inventoryItem.Wood;
import dungeonmania.inventoryItem.Potion.InvincibilityPotion;
import dungeonmania.inventoryItem.Potion.InvisibilityPotion;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

public class ItemTests {
    @Test
    @DisplayName("Bomb Unit test")
    public void testBomb(){
        Bomb b = new Bomb("1", 1);
        assertTrue(b.getId() == "1");
        assertTrue(b.getItemResponse().getId() == "1" && b.getItemResponse().getType() == "bomb");
        assertTrue(b.getType() == "bomb");
    }

    @Test
    @DisplayName("Treasure Unit test")
    public void testTreasure(){
        Treasure t = new Treasure("1");
        assertTrue(t.getId() == "1");
        assertTrue(t.getItemResponse().getId() == "1" && t.getItemResponse().getType() == "treasure");
    }

    @Test
    @DisplayName("Wood Unit test")
    public void testWood(){
        Wood w = new Wood("1");
        assertTrue(w.getId() == "1");
        assertTrue(w.getItemResponse().getId() == "1" && w.getItemResponse().getType() == "wood");
    }

    @Test
    @DisplayName("Arrow Unit test")
    public void testArrow(){
        Arrow a = new Arrow("1");
        assertTrue(a.getId() == "1");
        assertTrue(a.getItemResponse().getId() == "1" && a.getItemResponse().getType() == "arrow");
        assertTrue(a.getType() == "arrow");
    }

    @Test
    @DisplayName("Sword Unit test")
    public void testSword(){
        Sword s = new Sword("1", 1, 2);
        assertTrue(s.getId() == "1");
        assertTrue(s.getItemResponse().getId() == "1" && s.getItemResponse().getType() == "sword");
        assertTrue(s.getType() == "sword");
        assertTrue(s.getAttackBonus() == 1);
        assertTrue(s.getDurability() == 2);
        s.use();
        assertTrue(s.getDurability() == 1);
    }

    @Test
    @DisplayName("invis potion Unit test")
    public void testInvisibiltyPotion(){
        InvisibilityPotion i = new InvisibilityPotion(1, "1");
        assertTrue(i.getId() == "1");
        assertTrue(i.getItemResponse().getId() == "1" && i.getItemResponse().getType() == "invisibility_potion");
        assertTrue(i.getDuration() == 2);
        assertTrue(i.getType() == "invisibility_potion");
    }

    @Test
    @DisplayName("invincibility potion Unit test")
    public void testInvincibility(){
        InvincibilityPotion i = new InvincibilityPotion(1, "1");
        assertTrue(i.getId() == "1");
        assertTrue(i.getItemResponse().getId() == "1" && i.getItemResponse().getType() == "invincibility_potion");
        assertTrue(i.getDuration() == 2);
        assertTrue(i.getType() == "invincibility_potion");
    }

    @Test
    @DisplayName("Test using invisibility potion")
    public void testConsumeInvisibilityPotion() throws IllegalArgumentException, InvalidActionException {
        // pick up invincibility potion
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "simple");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "invincibility_potion")
                id = item.getId();
        }
        // use invincibility potion
        response = controller.tick(id);
        inventory = response.getInventory();
        assertTrue(inventory.size() == 0);
    }

    @Test
    @DisplayName("Test using invincibility potion")
    public void testConsumeInvincibilityPotion() throws IllegalArgumentException, InvalidActionException {
        // pick up and use invincibility potion before invisibility potion
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "simple");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "invincibility_potion")
                id = item.getId();
        }
        controller.tick(id);
        // pick up and use invincibility potion
        response = controller.tick(Direction.RIGHT);
        inventory = response.getInventory();
        for (ItemResponse item : inventory) {
            if (item.getType() == "invisibility_potion")
                id = item.getId();
        }
        response = controller.tick(id);
        inventory = response.getInventory();
        assertTrue(inventory.size() == 0);
    }

    @Test
    @DisplayName("Using Bomb")
    public void testPlacingBomb() throws IllegalArgumentException, InvalidActionException {
        // pick up the bomb
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombs", "simple");
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        // placing the bomb
        response = controller.tick(id);
        inventory = response.getInventory();
        assertTrue(inventory.size() == 0);
    }

    @Test
    @DisplayName("Placing a bomb on an already active switch below it")
    public void testPlacingBombOnActiveSwitchBelow() throws IllegalArgumentException, InvalidActionException {
        // push the boulder to activate switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombs", "simple");
        controller.tick(Direction.RIGHT);
        // pick up the bomb and place it at 4,3 which is below an active switch
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        // placing the bomb
        response = controller.tick(id);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 4,3
        // Everything in 1 radius should be gone except the player
        // 1 radius means should be a 3 x 3 square with player being in the center
        int y_coord = 2;
        int i = 0;
        int j = 0;
        while (i < 3) {
            int x_coord = 3;
            while (j < 3) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }

    @Test
    @DisplayName("Placing a bomb on an already active switch left of it")
    public void testPlacingBombOnActiveSwitchleft() throws IllegalArgumentException, InvalidActionException {
        // push the boulder to activate switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_bombs", "simple");
        controller.tick(Direction.RIGHT);
        // pick up the bomb and place it at 3,2 which is left of an active switch
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        // placing the bomb
        response = controller.tick(id);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 3,2
        // Everything in 1 radius should be gone except the player
        // 1 radius means should be a 3 x 3 square with player being in the center
        int y_coord = 1;
        int i = 0;
        int j = 0;
        while (i < 3) {
            int x_coord = 2;
            while (j < 3) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }

    @Test
    @DisplayName("Placing a bomb on an already active switch above it")
    public void testPlacingBombOnActiveSwitchAbove() throws IllegalArgumentException, InvalidActionException {
        // push the boulder to activate switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_bombs", "simple");
        controller.tick(Direction.RIGHT);
        // pick up the bomb and place it at 4,1 which is above an active switch
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.RIGHT);
        // placing the bomb
        response = controller.tick(id);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 4,1
        // Everything in 1 radius should be gone except the player
        // 1 radius means should be a 3 x 3 square with player being in the center
        int y_coord = 0;
        int i = 0;
        int j = 0;
        while (i < 3) {
            int x_coord = 3;
            while (j < 3) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }

    @Test
    @DisplayName("Placing a bomb on an already active switch right of it")
    public void testPlacingBombOnActiveSwitchRight() throws IllegalArgumentException, InvalidActionException {
        // push the boulder to activate switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_bombs", "simple");
        controller.tick(Direction.RIGHT);
        // pick up the bomb and place it at 5,2 which is right of an active switch
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.UP);
        // placing the bomb
        response = controller.tick(id);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 5,2
        // Everything in 1 radius should be gone except the player
        // 1 radius means should be a 3 x 3 square with player being in the center
        int y_coord = 1;
        int i = 0;
        int j = 0;
        while (i < 3) {
            int x_coord = 4;
            while (j < 3) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }
    @Test
    @DisplayName("Placing a bomb on an inactive switch")
    public void testPlacingBombThenActivatingSwitch() throws IllegalArgumentException, InvalidActionException {
        // pick up bomb and place at 4,3 adjacent to unactive switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombs", "simple");
        controller.tick(Direction.DOWN);
        controller.tick(Direction.RIGHT);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        // placing the bomb
        controller.tick(id);
        // activate switch
        controller.tick(Direction.LEFT);
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        response = controller.tick(Direction.RIGHT);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 4,3
        // Everything in 1 radius should be gone except the player
        // so it should be a 3 x 3 square
        int y_coord = 2;
        int i = 0;
        int j = 0;
        while (i < 3) {
            int x_coord = 3;
            while (j < 3) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }

    @Test
    @DisplayName("Testing larger bomb radius")
    public void testBombRadius2() throws IllegalArgumentException, InvalidActionException {
        // push the boulder to activate switch
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombs", "bomb_radius_2");
        controller.tick(Direction.RIGHT);
        // pick up the bomb and place it at 4,3 which is adjacent to active switch
        controller.tick(Direction.DOWN);
        DungeonResponse response = controller.tick(Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        String id = null;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bomb")
                id = item.getId();
        }
        // placing the bomb
        response = controller.tick(id);
        List<EntityResponse> entities = response.getEntities();
        List<Position> bombRadius = new ArrayList<>();
        // bomb placed at 4,3
        // Everything in 2 radius should be gone except the player
        // 1 radius means should be a 5 x 5 square with player being in the center
        int y_coord = 2;
        int i = 0;
        int j = 0;
        while (i < 5) {
            int x_coord = 3;
            while (j < 5) {
                bombRadius.add(new Position(x_coord, y_coord));
                x_coord ++;
                j ++;
            }
            i ++;
            y_coord ++;
        }
        for (EntityResponse e : entities) {
            if (e.getType() != "player")
                assertFalse(bombRadius.contains(e.getPosition()));
        }
    }
    //Need tests for the effectiveness of potion
}
