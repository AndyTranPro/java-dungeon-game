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

public class DungeonGeneratorTests {

    @Test
    @DisplayName("dungeon generator unit test")
    public void dungeonGeneratorTest(){
        DungeonGenerator gen = new DungeonGenerator(new Position(0, 0), new Position(10, 10));
        List<EntityResponse> list = gen.generate();
        assertTrue(list.size() != 0);
        gen = new DungeonGenerator(new Position(10, 10), new Position(0, 0));
        list = gen.generate();
        assertTrue(list.size() != 0);
    }

    @Test
    @DisplayName("dungeon generator controller test")
    public void dungeonGeneratorControllerTest(){
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.generateDungeon(0, 0, 10, 10, "simple");
        assertTrue(response.getGoals() == ":exit");
        List<EntityResponse> list = response.getEntities();
        Boolean player_exist = false;
        Boolean exit_exist = false;
        for (EntityResponse res : list) {
            if (res.getType() == "player") {
                assertTrue(res.getPosition().getX() == 0 && res.getPosition().getY() == 0);
                player_exist = true;
            }
            if (res.getType() == "exit") {
                assertTrue(res.getPosition().getX() == 10 && res.getPosition().getY() == 10);
                exit_exist = true;
            }
        }
        assertTrue(player_exist && exit_exist);
    }
}
