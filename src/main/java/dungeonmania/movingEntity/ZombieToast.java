package dungeonmania.movingEntity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.player.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends Entity implements Moving, ZombieType {
    private String id;
    private double health;
    private double damage;
    private Position position;
    private String type = "zombie_toast";
    private MercenaryMovingStrategy currentState = new RandomStrategy();
    private MercenaryMovingStrategy prevState = new RandomStrategy();
    private MoveFactorCounter moveFactorCounter = null;

    public ZombieToast(Position position, String id) {
        this.id = id;
        this.position = position;
    }

    public void setConfig(){
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

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

    // @Override
    // public void attack(Player player) {
    //     while(player.isAlive() || this.isAlive()) {
    //         player.setHealth(player.getHealth() - damage);
    //         this.setHealth(this.getHealth() - player.getAttack());
    //     }
    //     // then remove the player or the enemy depend on who died first.
    // }

    @Override
    public Position getPos() {
        return position;
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
    public double getDamage() {
        return damage;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, position, false);
        return response;
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    @Override
    public List<String> getEntitiesStringByPosition(Position pos) {
        return dungeonInfo.getEntitiesStringByPosition(pos);
    }

    public void setStrategy(MercenaryMovingStrategy strategy) {
        this.currentState = strategy;
    }

    public void revertStrategy() {
        this.currentState = prevState;
    }
    
}
