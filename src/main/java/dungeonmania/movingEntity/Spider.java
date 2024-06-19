package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.player.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends Entity implements Moving, Serializable {
    private static double damage;
    private static int SpawnRate;
    private static int timeToSpawn;
    private static double classHealth;

    private String id;
    private Position position;
    private Position spawnPosition;
    private double health;
    private SpiderMovingState currentState = new CircleDirection(this); 
    private String type = "spider";
    private MoveFactorCounter moveFactorCounter = null;

    
    public Spider(Position position, String id) {
        this.position = position;
        this.spawnPosition = position;
        this.id = id;
        this.health = classHealth;
    }
    
    public void setConfig(){
    }

    public void move() {
        // create a moveFactorCounter if it is null, so if the moving entity first enter a new position
        // if it is any position with a movement factor block(swamptile) in it, it will count the movement factor block
        if (moveFactorCounter == null) {
            moveFactorCounter = new MoveFactorCounter(this, getPos());
        }
        // if the counter is 0, then move the zombie.
        if (moveFactorCounter.movementFactorCounter()) {
            currentState.move();
            moveFactorCounter = null;
        }
    }

    @Override
    public Position getPos() {
        return position;
    }
    
    public void setCurrentState(SpiderMovingState state) {
        currentState = state;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }

    public SpiderMovingState getCurrentState() {
        return currentState;
    }
    
    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, position, false);
        return response;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public List<String> getEntitiesStringByPosition(Position pos) {
        return dungeonInfo.getEntitiesStringByPosition(pos);
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    public static void setDamage(int damage) {
        Spider.damage = damage;
    }

    public static void setSpawnRate(int spawnRate) {
        SpawnRate = spawnRate;
    }

    public static void setClassHealth(double classHealth) {
        Spider.classHealth = classHealth;
    }
   
    public static void setTimeToSpawn(int timeToSpawn) {
        Spider.timeToSpawn = timeToSpawn;
    }

    /**
     * 
     * @return true if the spider is spawn
     */
    public static boolean spawn() {
        if (SpawnRate == 0) {
            return false;
        }
        timeToSpawn = timeToSpawn - 1;
        if (timeToSpawn <= 0) {
            timeToSpawn = SpawnRate;
            return true;
        }

        return false;
    }

    /**
     * @return the position of the spider spawned
     */
    public static Position generateSpawnPos(Position p) {
        Random r = new Random();
        int x = r.ints(-10, 10).findFirst().getAsInt();
        int y = r.ints(-10, 10).findFirst().getAsInt();
        return p.translateBy(x, y);
    }
}
    
