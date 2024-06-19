// package dungeonmania;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import dungeonmania.player.Player;
// import dungeonmania.util.Direction;
// import dungeonmania.util.Position;

// public class PlayerTests {
//     static final Position defaultPosition = new Position(0, 0); 

//     @Test
//     @DisplayName("Test whether the player spawns at correct position")
//     public void PlayerTestSpawnLocation() {
//         Player player = new Player(defaultPosition, "1");
//         Position position = player.getPos();

//         assertEquals(position, new Position(0, 0));
//     }


//     @Test
//     @DisplayName("Tests if player's health reduces by the correct amount")
//     public void PlayerTestReduceHealth() {
//         double reduce = 5;
//         Player player = new Player(defaultPosition, "1");

//         player.reduceHealth(reduce);
//         double health = player.getHealth();

//         assertEquals(health, 10 - reduce);
//     }

//     @Test
//     @DisplayName("Tests if player's position is set to correct value")
//     public void PlayerTestSetPosition() {
//         Position newPosition = new Position(1, 1);
//         Player player = new Player(defaultPosition, "1");

//         player.setPos(newPosition);
//         Position position = player.getPos();

//         assertEquals(position, newPosition);

//         player.setPos(10,10);
//         position = player.getPos();

//         assertEquals(position, new Position(10, 10));

//     }

//     @Test
//     @DisplayName("Tests if player moves up")
//     public void PlayerTestMoveUp() {
//         Player player = new Player(defaultPosition, "1");

//         player.move(Direction.UP);
//         Position position = player.getPos();

//         assertEquals(position, new Position(0, -1));

//         player.move(Direction.UP);
//         position = player.getPos();

//         assertEquals(position, new Position(0, -2));

//     }

//     @Test
//     @DisplayName("Tests if player moves left")
//     public void PlayerTestMoveLeft() {
//         Player player = new Player(defaultPosition, "1");

//         player.move(Direction.LEFT);
//         Position position = player.getPos();

//         assertEquals(position, new Position(-1, 0));

//         player.move(Direction.LEFT);
//         position = player.getPos();

//         assertEquals(position, new Position(-2, 0));

//     }


//     @Test
//     @DisplayName("Tests if player moves right")
//     public void PlayerTestMoveRight() {
//         Player player = new Player(defaultPosition, "1");

//         player.move(Direction.RIGHT);
//         Position position = player.getPos();

//         assertEquals(position, new Position(1, 0));

//         player.move(Direction.RIGHT);
//         position = player.getPos();

//         assertEquals(position, new Position(2, 0));

//     }

//     @Test
//     @DisplayName("Tests if player moves down")
//     public void PlayerTestMoveDown() {
//         Player player = new Player(defaultPosition, "1");

//         player.move(Direction.DOWN);
//         Position position = player.getPos();

//         assertEquals(position, new Position(0, 1));

//         player.move(Direction.DOWN);
//         position = player.getPos();

//         assertEquals(position, new Position(0, 2));

//     }

//     @Test
//     @DisplayName("Tests if player moves correctly")
//     public void PlayerTestMove() {
//         Player player = new Player(defaultPosition, "1");

//         player.move(Direction.DOWN);
//         player.move(Direction.DOWN);
//         player.move(Direction.LEFT);
//         player.move(Direction.LEFT);
//         player.move(Direction.LEFT);
//         player.move(Direction.RIGHT);
//         player.move(Direction.LEFT);
//         player.move(Direction.DOWN);
//         player.move(Direction.UP);
//         player.move(Direction.UP);
//         player.move(Direction.DOWN);
//         Position position = player.getPos();

//         assertEquals(position, new Position(-3, 2));

//     }

//     @Test
//     @DisplayName("Tests if player identifies correctly it is alive")
//     public void PlayerTestAlive(){
//         Player player = new Player(defaultPosition, "1");

//         assertTrue(player.isAlive());

//         player.setHealth(0.1);

//         assertTrue(player.isAlive());

//     }

//     @Test
//     @DisplayName("Tests if player correctly identifies correctly it is alive")
//     public void PlayerTestDead(){
//         Player player = new Player(defaultPosition, "1");

//         assertTrue(player.isAlive());

//         player.setHealth(0);
        
//         assertFalse(player.isAlive());

//     }
// }
