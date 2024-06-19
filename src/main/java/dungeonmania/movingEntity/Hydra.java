package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Hydra extends Entity implements Moving, ZombieType, Serializable {
    private String id;
    private Position position;
    private double health;
    private double damage;
    private MercenaryMovingStrategy currentState = new RandomStrategy();
    private MercenaryMovingStrategy prevState = new RandomStrategy();

    private String type = "hydra";
    private boolean bribed = false;
    private double hydra_health_increase_rate;
    private int hydra_health_increase_amount;
    private MoveFactorCounter moveFactorCounter = null;

    public Hydra(Position position, String id) {
        this.id = id;
        this.position = position;
    }

    public boolean chanceTrue(){
        // if the random number is greater than 0 after subtract the rate(between 0 to 1) times by 100, then return true.
        Random rand = new Random();
        if (rand.nextInt(100) - hydra_health_increase_rate*100 > 0) {
            return true;
        }
        return false;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        if (health < this.health) {
            int rate = (int)(hydra_health_increase_rate * 100);
            Random rand = new Random();
            if (rand.nextInt(100) < rate) {
                this.health += hydra_health_increase_amount;
                return;
            }
        }
        this.health = health;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public void move() {
        // create a moveFactorCounter if it is null, so if the moving entity first enter a new position
        // if it is any position with a movement factor block(swamptile) in it, it will count the movement factor block
        if (moveFactorCounter == null) {
            moveFactorCounter = new MoveFactorCounter(this, getPos());
        }
        // if the counter is 0, then move the zombie.
        if (moveFactorCounter.movementFactorCounter()) {
            currentState.move(this);
            moveFactorCounter = null;
        }
    }

    @Override
    public List<String> getEntitiesStringByPosition(Position pos) {
        return dungeonInfo.getEntitiesStringByPosition(pos);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Position getPos() {
        return position;
    }

    public void setPos(Position pos){
        this.position = pos;
    }
    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, position, false);
        return response;
    }

    @Override
    public void setConfig() {
        this.health = dungeonInfo.getSpecificConfig("hydra_health");
        this.damage = dungeonInfo.getSpecificConfig("hydra_attack");
        this.hydra_health_increase_rate = dungeonInfo.getSpecificConfigDouble("hydra_health_increase_rate");
        this.hydra_health_increase_amount = dungeonInfo.getSpecificConfig("hydra_health_increase_amount");
    }

    public int gethydra_health_increase_amount(){
        return hydra_health_increase_amount;
    }

    public void revertStrategy() {
        this.currentState = prevState;
    }

    @Override
    public void setStrategy(MercenaryMovingStrategy strategy) {
        this.currentState = strategy;   
    }
    
}
