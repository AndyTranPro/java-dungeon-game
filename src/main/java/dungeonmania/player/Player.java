package dungeonmania.player;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import dungeonmania.Battle;
import dungeonmania.Entity;
import dungeonmania.collectableEntity.CollectableEntity;
import dungeonmania.collectableEntity.Key;
import dungeonmania.movingEntity.Moving;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.inventoryItem.Potion.Potion;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntities.staticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Serializable{
    private String id;
    private String type = "player";
    private int enemiesMet = 0; 

    protected double attack;
    protected Position position;
    protected double health;

    private PlayerState playerState = new NormalState(this);
    private Queue<Potion> potionUsedqueue = new LinkedList<Potion>();

    private Position playerLastPosition;

    /**
     * Creates a Player Object at a sepcificied location with default health and attack values.
     *  
     * @param x     - Horizontal Position
     * @param y     - Vertical Position
     * 
     */
    public Player(Position position, String id){
        this.position = position;
        this.id = id;
        playerLastPosition = position;

        return;
    }

    public void setConfig(){
        this.health = dungeonInfo.getSpecificConfig("player_health");
        this.attack = dungeonInfo.getSpecificConfig("player_attack");
    }

    /**
     * Gets the current Attack value of the player.
     * 
     * @return Attack - Integer
     */
    public double getAttack(){
        return playerState.getAttack(); 
    }

    public void setAttack (double attack) {
        this.attack = attack;
    }

    /**
     * Gets the Health value of the player.
     * 
     * @return Health - Integer
     */
    public double getHealth(){ 
        return playerState.getHealth();
    }
    
    /**
     * Sets the Health to a specified value.
     * 
     * @param health
     */
    public void setHealth(double health){
        this.health = health;

        return;
    }
    
    /**
     * Reduces the Health by a specified value.
     * 
     * @param health
     */
    public void reduceHealth(double health){
        this.health -= health;

        return;
    }

    /**
     * Gets the current position of the player.
     * 
     * @return Position - Position
     */
    public Position getPos(){
        return playerState.getPosition();
    }

    /**
     * Sets the Position to a specified value.
     * 
     * @param x - Horizontal Position 
     * @param y - Vertical Position 
     */
    public void setPos(int x, int y){
        position = new Position(x, y);
         
        return;
    }

    /**
     * Sets the Position to a specified value.
     * 
     * @param position - Position (x and y)  
     */
    public void setPos(Position position){
        this.position = position;

        return;
    }
    
    /**
     * Moves the Player in a specified direction.
     * 
     * @param direction
     */
    public void move(Direction direction){
        //check the static entities before move into the cell
        Position p = null;
        Position checkPosition = position.translateBy(direction);
        List<Entity> checkEntity = dungeonInfo.getEntitiesByPosition(checkPosition);
        BattleResponse response = null;
        playerLastPosition = position;
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                p = se.playerMoveIn(this.position, direction);
            }

            if (this.position.equals(p)){
                break;
            }
        }
        if (p == null){
            this.position = checkPosition;
        } else {
            this.position = p;
        }

        //After move to the cell, collect all collectables
        checkEntity = dungeonInfo.getEntitiesByPosition(this.position);
        for(Entity e : checkEntity){
            if (e instanceof CollectableEntity) {
                if (e instanceof Key){
                    Key k = (Key) e;
                    k.pickup();
                } else {
                    CollectableEntity ce = (CollectableEntity) e;
                    ce.pickup();
                }

            }
        }
        //After move to the cell, check for any moving entities and if there is, start battle
        checkEntity = dungeonInfo.getEntitiesByPosition(this.position);
        for(Entity e : checkEntity){
            if (e instanceof Moving) {
                Moving movingEntity = (Moving) e;
                Battle battle = new Battle(this, movingEntity, dungeonInfo);
                response = battle.start();
                if (response != null)
                    dungeonInfo.addBattleResponse(response);
                break;
            }
        }

        
    }

    
    public int getEnemiesMet() {
        return enemiesMet;
    }

    

    public void setEnemiesMet(int enemiesMet) {
        this.enemiesMet = enemiesMet;
    }

    /**
     * Checks if the player is currently alive or dead.
     * 
     * @return 
     *      <code>true</code> if health is 
     *      greater than 0;
     *      <code>false</code> otherwise.
     */
    // public Boolean isAlive() {
    //     if (health > 0) { return true; }
    //     else { return false; }
    // }

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

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }
    
    public void addPotion(Potion potion){
        potionUsedqueue.add(potion);
    }
    public Potion pullPotion(){
        return potionUsedqueue.poll();
    }
    public void tickPlayerState(){
        playerState.tickPotionTime();
    }
    public Boolean isInvincible() {
        if (playerState.getStateName() == "Invincible")
            return true;
        return false;
    }

    public Position getPlayerLastPosition() {
        return playerLastPosition;
    }
}
