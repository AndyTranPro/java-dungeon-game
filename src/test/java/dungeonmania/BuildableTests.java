package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.buildableEntity.Bow;
import dungeonmania.buildableEntity.MidnightArmour;
import dungeonmania.buildableEntity.Sceptre;
import dungeonmania.buildableEntity.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BuildableTests {
    @Test
    @DisplayName("Test Bow Unit")
    public void testBowUnit() {
        Bow bow = new Bow("bow1", 10);

        assertEquals("bow1", bow.getId());
        assertEquals("bow", bow.getType());
        assertEquals(10, bow.getDurability());

        assertEquals("bow1", bow.getItemResponse().getId());
        assertEquals("bow", bow.getItemResponse().getType());

    }

    @Test
    @DisplayName("Test Shield Unit")
    public void testShieldUnit() {
        Shield shield = new Shield("shield1", 10, 10);

        assertEquals("shield1", shield.getId());
        assertEquals("shield", shield.getType());
        assertEquals(10, shield.getDefenseBonus());
        assertEquals(10, shield.getDurability());

        assertEquals("shield1", shield.getItemResponse().getId());
        assertEquals("shield", shield.getItemResponse().getType());
        
    }

    @Test
    @DisplayName("Test Bow Destroyed")
    public void testBowDestroyed() {
        Bow bow = new Bow("bow1", 10);

        bow.setDurability(0);

        assertTrue(bow.isItemDestroyed());

    }

    @Test
    @DisplayName("Test Shield Destroyed")
    public void testShieldDestroyed() {
        Shield shield = new Shield("shield1", 10, 10);

        shield.setDurability(0);

        assertTrue(shield.isItemDestroyed());
        
    }

    @Test
    @DisplayName("Test Bow Use() Method")
    public void testBowUse(){
        Bow bow = new Bow("bow1", 10);

        bow.use();

        assertEquals(9, bow.getDurability());
    }

    @Test
    @DisplayName("Test Shield Use() Method")
    public void testShieldUse(){
        Shield shield = new Shield("shield1", 10, 10);

        shield.use();

        assertEquals(9, shield.getDurability());
    }

    @Test
    @DisplayName("Test invalid craft request")
    public void testInvalidCraft() {
        assertThrows(IllegalArgumentException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("build_bow", "simple");
            controller.tick(dungeonmania.util.Direction.RIGHT);
            controller.tick(dungeonmania.util.Direction.RIGHT);
            controller.tick(dungeonmania.util.Direction.RIGHT);
            controller.tick(dungeonmania.util.Direction.RIGHT);
            controller.build("Random Item Name");
            });
    }

    @Test
    @DisplayName("Test crafting bow without required items")
    public void testInsufficientItemForBow() {
        assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("build_bow", "simple");
            controller.build("bow");
            });
    }

    @Test
    @DisplayName("Test crafting shield without required items")
    public void testInsufficientItemForShield() {
        assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("build_shield", "simple");
            controller.build("shield");
            });
    }

    @Test
    @DisplayName("Test Bow buildable wood and arrow")
    public void testWoodAndArrow() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build_bow", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("bow"));

        DungeonResponse response = controller.build("bow");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bow")
                succeed = true;
        }
        assertTrue(succeed);
    }

    //No map found that can build sheild using treasure
/*     @Test
    @DisplayName("Test buildable treasure")
    public void testTreasure() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build_bow", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        DungeonResponse response = controller.build("Bow");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "Bow")
                succeed = true;
        }
        assertTrue(succeed);
    } */
    
    @Test
    @DisplayName("Test Shield buildable with key")
    public void testUnusedKey() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build_shield", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("shield"));

        DungeonResponse response = controller.build("shield");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "shield")
                succeed = true;
        }
        assertTrue(succeed);
    }

    @Test
    @DisplayName("Trying Crafting Bow twice")
    public void testBowDoubleCraft() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build_bow", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        DungeonResponse response = controller.build("bow");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "bow")
                succeed = true;
        }
        assertTrue(succeed);

        assertThrows(InvalidActionException.class,
            () -> {
                controller.build("bow");
            });

            assertEquals(controller.getDungeonResponseModel().getBuildables().size(), 0);
    }

    @Test
    @DisplayName("Trying Crafting Shield Twice")
    public void testShieldDoubleCraft() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build_shield", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        DungeonResponse response = controller.build("shield");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "shield")
                succeed = true;
        }
        assertTrue(succeed);

        assertThrows(InvalidActionException.class,
            () -> {
                controller.build("shield");
            });

            assertEquals(controller.getDungeonResponseModel().getBuildables().size(), 0);
    }
    
    @Test
    @DisplayName("Test Shield buildable with Sunstone")
    public void testShieldCraftSunStone() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_build_shield_sunstone", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("shield"));

        DungeonResponse response = controller.build("shield");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        boolean stoneRemains = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "shield") {succeed = true;}

            if (item.getType() == "sun_stone") {stoneRemains = true;}
        }

        assertTrue(succeed);
        assertTrue(stoneRemains);
    }

    // sceptre and Midnight unit tests
    @Test
    @DisplayName("Test sceptre Unit")
    public void testsceptreUnit() {
        Sceptre sceptre = new Sceptre("sceptre1", 3);

        assertEquals(3, sceptre.getDuration());
        assertEquals("sceptre", sceptre.getType());

        assertEquals("sceptre1", sceptre.getItemResponse().getId());
        assertEquals("sceptre", sceptre.getItemResponse().getType());
        
    }

    @Test
    @DisplayName("Test Midnight Armour Unit")
    public void testMidnightArmourUnit() {
        MidnightArmour ma = new MidnightArmour("midnight_armour1", 5, 6);

        assertEquals(5, ma.getDefenseBonus());
        assertEquals(6, ma.getAttackBonus());
        assertEquals("midnight_armour", ma.getType());
        assertEquals("midnight_armour1", ma.getId());

        assertEquals("midnight_armour1", ma.getItemResponse().getId());
        assertEquals("midnight_armour", ma.getItemResponse().getType());
        
    }
 
    @Test
    @DisplayName("Test crafting sceptre without required items")
    public void testInsufficientItemForSceptre() {
        assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("d_build_sceptre", "simple");
            controller.build("sceptre");
            });
    }

    @Test
    @DisplayName("Test crafting Midnight Armour without required items")
    public void testInsufficientItemForMidnightArmour() {
        assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("d_build_ma", "simple");
            controller.build("midnight_armour");
            });
    }

    @Test
    @DisplayName("Test crafting Midnight Armour with Zombie on Map")
    public void testCraftMidnightArmourWithZombie() {
        assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("d_build_ma_zombie", "simple");
            controller.tick(dungeonmania.util.Direction.RIGHT);
            controller.tick(dungeonmania.util.Direction.RIGHT);

            controller.build("midnight_armour");
            });

            assertThrows(InvalidActionException.class,
            () -> {
                DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("d_build_ma_zombie", "simple");

            controller.build("midnight_armour");
            });
    }

    @Test
    @DisplayName("Test Midnight Armour buildable")
    public void testMidnightArmourCrafting() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_build_ma", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("midnight_armour"));

        DungeonResponse response = controller.build("midnight_armour");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "midnight_armour")
                succeed = true;
        }
        assertTrue(succeed);
    }

    @Test
    @DisplayName("Test Sceptre buildable with Wood, key and sunstone")
    public void testSceptreCrafting() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_build_sceptre", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("sceptre"));

        DungeonResponse response = controller.build("sceptre");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "sceptre")
                succeed = true;
        }
        assertTrue(succeed);
    }

    @Test
    @DisplayName("Test Sceptre buildable with Arrow, treasure and sunstone")
    public void testSceptreCrafting1() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_build_sceptre1", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("sceptre"));

        DungeonResponse response = controller.build("sceptre");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "sceptre")
                succeed = true;
        }
        assertTrue(succeed);
    }

    @Test
    @DisplayName("Test Sceptre buildable with Wood and sunstone")
    public void testSceptreCrafting2() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_build_sceptre2", "simple");
        controller.tick(dungeonmania.util.Direction.RIGHT);
        controller.tick(dungeonmania.util.Direction.RIGHT);

        assertFalse(controller.getDungeonResponseModel().getBuildables().contains("sceptre"));

        controller.tick(dungeonmania.util.Direction.RIGHT);
        assertTrue(controller.getDungeonResponseModel().getBuildables().contains("sceptre"));

        DungeonResponse response = controller.build("sceptre");
        List<ItemResponse> inventory = response.getInventory();
        boolean succeed = false;
        for (ItemResponse item : inventory) {
            if (item.getType() == "sceptre")
                succeed = true;
        }
        assertTrue(succeed);
    }

    @Test
    @DisplayName("Test Sceptre Mind control works with Mercenary")
    public void testSceptreMindControlMercernary() throws IllegalArgumentException, InvalidActionException{
        // test if mercenary can be bribe and its behaviour after bribed
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_build_sceptre_mercenary", "c_buildableTest_testSceptre");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        //Gather crafting materials
        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        
        res = dmc.tick(Direction.RIGHT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        
        res = dmc.tick(Direction.RIGHT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        //Build Sceptre
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        //Mind control the Mercenary
        String mercenaryId = getEntities(res, "mercenary").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));

        //Move and make sure Mercenary is bribed
        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        assertFalse(getEntities(res, "mercenary").get(0).isInteractable());

        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        assertFalse(getEntities(res, "mercenary").get(0).isInteractable());

        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        assertFalse(getEntities(res, "mercenary").get(0).isInteractable());

        ////Move and make sure Mercenary is no longer bribed
        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
        assertTrue(getEntities(res, "mercenary").get(0).isInteractable());
}

    @Test
    @DisplayName("Test Sceptre Mind control works with Assassin")
    public void testSceptreMindControlAssassin() throws IllegalArgumentException, InvalidActionException{
        // Test if Sceptre Mind Control works and that it lasts for the current time 
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_build_sceptre_assassin", "c_buildableTest_testSceptre");
        Position pos = getEntities(res, "assassin").get(0).getPosition();
        Position expectedPosition = pos.translateBy(Direction.LEFT);

        //Gather crafting materials
        res = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        
        res = dmc.tick(Direction.RIGHT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        
        res = dmc.tick(Direction.RIGHT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        
        //Build Sceptre
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        //Mind Control the Assassin
        String assassinId = getEntities(res, "assassin").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));

        //Move and make sure Assassin is bribed
        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        assertFalse(getEntities(res, "assassin").get(0).isInteractable());

        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        assertFalse(getEntities(res, "assassin").get(0).isInteractable());

        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        assertFalse(getEntities(res, "assassin").get(0).isInteractable());

        ////Move and make sure Assassin is no longer bribed
        res = dmc.tick(Direction.LEFT);
        expectedPosition = expectedPosition.translateBy(Direction.LEFT);
        assertEquals(expectedPosition, getEntities(res, "assassin").get(0).getPosition());
        assertTrue(getEntities(res, "assassin").get(0).isInteractable());


    }
}
