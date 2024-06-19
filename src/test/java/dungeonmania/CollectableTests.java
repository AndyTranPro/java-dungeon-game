package dungeonmania;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;

public class CollectableTests {

    @Test
    @DisplayName("Test collecting item")
    public void testCollectingItem(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "simple");
        DungeonResponse response = controller.tick(dungeonmania.util.Direction.RIGHT);
        List<ItemResponse> inventory = response.getInventory();
        assertTrue(inventory.size() != 0);
    }

}
