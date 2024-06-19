package dungeonmania;

import java.io.Serializable;
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import dungeonmania.buildableEntity.Bow;
import dungeonmania.buildableEntity.Buildable;
import dungeonmania.buildableEntity.Shield;
import dungeonmania.collectableEntity.CollectableEntity;
import dungeonmania.collectableEntity.Key;
import dungeonmania.goal.Goal;
import dungeonmania.goal.basicGoal.bouldersGoal;
import dungeonmania.goal.basicGoal.enemiesGoal;
import dungeonmania.goal.basicGoal.exitGoal;
import dungeonmania.goal.basicGoal.treasureGoal;
import dungeonmania.goal.complexGoal.andGoal;
import dungeonmania.goal.complexGoal.orGoal;
import dungeonmania.inventoryItem.Bomb;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.inventoryItem.Sword;
import dungeonmania.inventoryItem.Potion.InvincibilityPotion;
import dungeonmania.inventoryItem.Potion.InvisibilityPotion;
import dungeonmania.movingEntity.Assassin;
import dungeonmania.movingEntity.Hydra;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.MercenaryType;
import dungeonmania.movingEntity.Moving;
import dungeonmania.movingEntity.Spider;
import dungeonmania.movingEntity.ZombieToast;
import dungeonmania.movingEntity.ZombieType;
import dungeonmania.player.OlderPlayer;
import dungeonmania.player.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntities.Boulder;
import dungeonmania.staticEntities.Door;
import dungeonmania.staticEntities.Exit;
import dungeonmania.staticEntities.FloorSwitch;
import dungeonmania.staticEntities.LightBulb;
import dungeonmania.staticEntities.LogicFloorSwitch;
import dungeonmania.staticEntities.LogicLightBulb;
import dungeonmania.staticEntities.LogicSwitchDoor;
import dungeonmania.staticEntities.PlacedBomb;
import dungeonmania.staticEntities.Portal;
import dungeonmania.staticEntities.SwampTile;
import dungeonmania.staticEntities.SwitchDoor;
import dungeonmania.staticEntities.TimeTravellingPortal;
import dungeonmania.staticEntities.Wall;
import dungeonmania.staticEntities.Wire;
import dungeonmania.staticEntities.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonInfo implements Serializable, Cloneable{
    private HashMap<String, Entity> entityMap = new HashMap<>(); // the entity map
    private HashMap<String, Integer> configMap = new HashMap<>(); // the config file map
    private List<InvItem> itemList = new ArrayList<>(); // the item list
    private Goal dungeonGoal = null;
    private List<BattleResponse> battleList = new ArrayList<>(); // battle response list
    private List<Tick> tickList = new ArrayList<>(); // tickable entities list
    private int entityCounter = 0;
    private DungeonInfoHistory dungeonInfoHistory = new DungeonInfoHistory();

    private AtomicInteger timeTravelCounter = new AtomicInteger(0);
    private AtomicInteger timeTravelledTick = new AtomicInteger(0);

    //store all entities into map
    public void storeEntitiesInMap(JSONArray arr){
        for (int i = 0; i < arr.length(); i++){
            JSONObject json = (JSONObject) arr.get(i);
            //get an id for that entity
            this.entityCounter = this.entityCounter + 1;
            String id = Integer.toString(this.entityCounter);
            entityMap.put(id, this.createEntity(json, id, this));
        }
    }

    //helper methods
    public int getTimeTravelCounter() {
        return timeTravelCounter.get();
    }

    public void setTimeTravelCounter(AtomicInteger timeTravelCounter) {
        this.timeTravelCounter = timeTravelCounter;
    }

    public int getTimeTravelledTick() {
        return timeTravelledTick.get();
    }

    public void setTimeTravelledTick(AtomicInteger timeTravelledTick) {
        this.timeTravelledTick = timeTravelledTick;
    }

    public int getDungeonInfoHistorySize(){
        return this.dungeonInfoHistory.getDungeonInfoSize();
    }

    //Store the dungeon info history
    public void storeDungeonInfo() throws CloneNotSupportedException{
        //clone the dungeonInfo and everything in it and add it to dungeonInfoHistory
        DungeonInfo dungeonInfoClone = (DungeonInfo) this.clone();
        // copy everything in the entityMap and put it in the dungeonInfoHistory
        HashMap<String, Entity> entityMapClone = new HashMap<>();
        for (String key : this.entityMap.keySet()){
            entityMapClone.put(key, this.entityMap.get(key).clone());
        }
        dungeonInfoClone.setEntityMap(entityMapClone);
        dungeonInfoClone.setTimeTravelCounter(timeTravelCounter);
        dungeonInfoClone.setTimeTravelledTick(timeTravelledTick);
        // store the dungeonInfo into the historyArray for time travel
        dungeonInfoHistory.addDungeonInfo(dungeonInfoClone);
    }

    public void removeOlderPlayerIfExist(){
        for (Entity e : entityMap.values()){
            if (e instanceof OlderPlayer){
                entityMap.remove(e.getId());
                return;
            }
        }
    }

    // get the dungeon from history based on the tickbefore,then overlap the current dungeon with the dungeon from history
    public void rewind(int tickBefore) {
        if (timeTravelCounter.get() == 0) {
            timeTravelCounter.set(tickBefore + 1);
            timeTravelledTick.set(tickBefore + 1);
        }
        if (timeTravelCounter.get() > 0) {
            timeTravelCounter.getAndDecrement();
        }
        int index = dungeonInfoHistory.getDungeonInfoSize() - tickBefore - 1;
        if (index < 0) {
            index = 0;
            timeTravelledTick.set(dungeonInfoHistory.getDungeonInfoSize() - 1);
            timeTravelCounter.set(dungeonInfoHistory.getDungeonInfoSize() - 1);
        }

        // get the old dungeonInfo from the historyArray
        DungeonInfo oldDungeonInfo = dungeonInfoHistory.getDungeonInfo(index);
        // get the player in the current dungeonInfo
        Player player = this.getPlayer();
        // get the player in the last dungeonInfo if there is no olderplayer in the current dungeonInfo, add a new one
        Player playerLast = oldDungeonInfo.getPlayer();

        if (oldDungeonInfo.getOlderPlayer() == null) {
            String id = "older_player";
            OlderPlayer olderPlayer = new OlderPlayer(id, playerLast);
            oldDungeonInfo.getEntityMap().put(olderPlayer.getId(), olderPlayer);
            //replace the player in the old dungeon with the player in current dungeon
            oldDungeonInfo.replacePlayer(player);
        } else {
            // get the older player in the last dungeonInfo
            OlderPlayer olderPlayer = this.getOlderPlayer();
            // set the older player to the player in the current dungeonInfo
            olderPlayer.setPos(playerLast.getPos());
            oldDungeonInfo.replacePlayer(player);
        }
        // set the dungeonInfo to the current dungeonInfo
        this.entityMap = oldDungeonInfo.entityMap;
        this.configMap = oldDungeonInfo.configMap;
        this.itemList = oldDungeonInfo.itemList;
        this.dungeonGoal = oldDungeonInfo.dungeonGoal;
        this.battleList = oldDungeonInfo.battleList;
        this.tickList = oldDungeonInfo.tickList;
        this.entityCounter = oldDungeonInfo.entityCounter;
        this.dungeonInfoHistory = oldDungeonInfo.dungeonInfoHistory;
        this.timeTravelCounter = oldDungeonInfo.timeTravelCounter;
        this.timeTravelledTick = oldDungeonInfo.timeTravelledTick;
    }

    //create an entity class in terms of the type.
    public Entity createEntity(JSONObject json, String id, DungeonInfo info){
        Entity newEntity;
        int x = (int) json.get("x");
        int y = (int) json.get("y");
        //check the type of the entity
        switch((String) json.get("type")){
            case "player":
               newEntity = new Player(new Position(x, y), id);
               newEntity.setDungeonInfo(info);
               newEntity.setConfig();
               break;
               
            case "spider":
               newEntity = new Spider(new Position(x, y), id);
               newEntity.setDungeonInfo(info);
               newEntity.setConfig();
               break;

            case "mercenary":
                newEntity = new Mercenary(new Position(x, y), id);
                newEntity.setDungeonInfo(info);
                newEntity.setConfig();
                break;

            case "assassin":
                newEntity = new Assassin(new Position(x, y), id);
                newEntity.setDungeonInfo(info);
                newEntity.setConfig();
                break;
            
            case "hydra":
                newEntity = new Hydra(new Position(x,y), id);
                newEntity.setDungeonInfo(info);
                newEntity.setConfig();
                break;

            case "boulder":
                newEntity = new Boulder(new Position(x, y), id);
                break;

            case "door":
                newEntity = new Door(new Position(x, y), (int) json.get("key"), id);
                break;
             
            case "exit":
                newEntity = new Exit(new Position(x, y), id);
                break;
            
            case "switch":
                try{
                    newEntity = new LogicFloorSwitch(new Position(x, y), id, (String) json.get("logic"));
                    this.addTick((Tick) newEntity);
                } catch(JSONException e) {
                    newEntity = new FloorSwitch(new Position(x, y), id);
                } 
                break;

            case "portal":
                newEntity = new Portal(new Position(x, y), (String) json.get("colour"), id);
                break;

            case "wall":
                newEntity = new Wall(new Position(x, y), id);
                break;
            
            case "zombie_toast_spawner":
                newEntity = new ZombieToastSpawner(new Position(x, y), id);
                newEntity.setDungeonInfo(info);
                newEntity.setConfig();
                break;

            case "zombie_toast":
                newEntity = new ZombieToast(new Position(x, y), id);
                newEntity.setDungeonInfo(info);
                newEntity.setConfig();
                break;
            
            case "key":
                newEntity = new Key(id, (String) json.get("type"), new Position(x, y), (int) json.get("key"));
                break;

            case "swamp_tile":
                newEntity = new SwampTile(new Position(x, y), id, (int) json.get("movement_factor"));
                break;
            
            case "switch_door":
                try{
                    newEntity = new LogicSwitchDoor(new Position(x, y), (int) json.get("key"), id, (String) json.get("logic"));
                } catch(JSONException e){
                    newEntity = new SwitchDoor(new Position(x, y), (int) json.get("key"), id);
                }         
                this.addTick((Tick) newEntity);
                break;
            
            case "light_bulb_off":
                try{
                    newEntity = new LogicLightBulb(new Position(x, y), id, (String) json.get("logic"));
                } catch(JSONException e) {
                    newEntity = new LightBulb(new Position(x, y), id);
                }           
                this.addTick((Tick) newEntity);
                break;
            
            case "wire":
                newEntity = new Wire(new Position(x, y), id);
                break;
            
            case "bomb":
                try{
                    newEntity = new CollectableEntity(id, (String) json.get("type"), new Position(x, y), (String) json.get("logic"));
                } catch(JSONException e) {
                    newEntity = new CollectableEntity(id, (String) json.get("type"), new Position(x, y));
                }
                break;
            
            case "time_travelling_portal":
                newEntity = new TimeTravellingPortal(new Position(x, y), id);
                break;
            
            // if default, it will be collectableEntities
            default:
                newEntity = new CollectableEntity(id, (String) json.get("type"), new Position(x, y));
        }
        newEntity.setDungeonInfo(info);
        return newEntity;
    }

    // get a list of entityResponse for controller to use
    public List<EntityResponse> getListEntityResponse() {
        List<EntityResponse> list = new ArrayList<>();
        for (Entity e: entityMap.values()) {
            list.add(e.getEntityResponse());
        }

        return list;
    } 

    // set config
    public void setConfigs(JSONObject config){    

        for (String keyString : config.keySet()){
            int configValue;
            if (keyString == "assassin_bribe_fail_rate" || keyString == "hydra_health_increase_rate") {
                double double_configValue = config.getDouble(keyString) * 100;
                configValue = (int) double_configValue;
            }
            else
                configValue = config.getInt(keyString);
            
            configMap.put(keyString, configValue);
        }
        //m3 config Values for backwards compatibility
        if (configMap.get("mind_control_duration") == null)
            configMap.put("mind_control_duration", 3);
        if (configMap.get("midnight_armour_attack") == null)
            configMap.put("midnight_armour_attack", 2);
        if (configMap.get("midnight_armour_defence") == null)
            configMap.put("midnight_armour_defence", 2);
        if (configMap.get("hydra_attack") == null) 
            configMap.put("hydra_attack", 10);
        if (configMap.get("hydra_health") == null)
            configMap.put("hydra_health", 10);
        if (configMap.get("hydra_health_increase_amount") == null)
            configMap.put("hydra_health_increase_amount", 1);
        if (configMap.get("hydra_health_increase_rate") == null)
            configMap.put("hydra_health_increase_rate", 50);
        if (configMap.get("hydra_spawn_rate") == null)
            configMap.put("hydra_spawn_rate", 0);
        if (configMap.get("assassin_attack") == null)
            configMap.put("assassin_attack", 10);
        if (configMap.get("assassin_bribe_amount") == null)
            configMap.put("assassin_bribe_amount", 1);
        if (configMap.get("assassin_bribe_fail_rate") == null)
            configMap.put("assassin_bribe_fail_rate", 30);
        if (configMap.get("assassin_health") == null)
            configMap.put("assassin_health", 10);
        if (configMap.get("assassin_recon_radius") == null)
            configMap.put("assassin_recon_radius", 5);
    }

    public int getSpecificConfig(String name){
        return configMap.get(name);
    }

    public double getSpecificConfigDouble(String name){
        return configMap.get(name);
    }

    public List<String> getEntitiesStringByPosition(Position pos) {
        List<String> entities = new ArrayList<String>();
        for (Entity entity : entityMap.values()) {
            if (entity.getPos().equals(pos)) {
                entities.add(entity.getType());
            }
        }
        return entities;
    }

    public List<Entity> getEntitiesByPosition(Position pos) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : entityMap.values()) {
            if (entity.getPos().equals(pos)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    //trigger player move
    public void movePLayer(Direction d){
        //find player
        Player p = this.getPlayer();
        p.move(d);
    }
    
    //find the player
    public Player getPlayer(){
        for (Entity e : entityMap.values()){
            if (e.getType().equals("player")){
                Player p = (Player) e;
                return p;
            }
        }
        return null;
    }

    public OlderPlayer getOlderPlayer(){
        for (Entity e : entityMap.values()){
            if (e.getType().equals("older_player")){
                OlderPlayer p = (OlderPlayer) e;
                return p;
            }
        }
        return null;
    }

    public void replacePlayer(Player newPlayer){
        for (Entity e : entityMap.values()){
            if (e.getType().equals("player")){
                entityMap.remove(e.getId());
                entityMap.put(newPlayer.getId(), newPlayer);
                return;
            }
        }
    }

    public HashMap<String, Entity> getEntityMap() {
        return entityMap;
    }

    public HashMap<String, Integer> getConfigMap() {
        return configMap;
    }

    public List<InvItem> getItemList() {
        return itemList;
    }

    // get a list of itemResponse for controller to use
    public List<ItemResponse> getListItemResponse() {
        List<ItemResponse> list = new ArrayList<>();
        for (InvItem i: itemList) {
            list.add(i.getItemResponse());
        }

        return list;
    }

    public void moveAllMovingEntity(){
        for (Moving m : getAllMovingEntity()){
            m.move();
            for(Entity e : getEntitiesByPosition(m.getPos())){
                if (e instanceof Player) {
                    Player player = (Player) e;
                    Battle battle = new Battle(player, m, this);
                    BattleResponse response = battle.start();
                    if (response != null)
                        addBattleResponse(response);
                    break;
                }
            }
        }
    }

    public List<Moving> getAllMovingEntity(){
        List<Moving> list = new ArrayList<>();
        for (Entity e : entityMap.values()){
            if (e instanceof Moving){
                Moving m = (Moving) e;
                list.add(m);
            }
        }
        return list;
    }
    public List<MercenaryType> getAllMencenaryType(){
        List<MercenaryType> list = new ArrayList<>();
        for (Entity e : entityMap.values()){
            if (e instanceof MercenaryType){
                list.add((MercenaryType) e);
            }
        }
        return list;
    }
    public List<ZombieToast> getAllZombie(){
        List<ZombieToast> list = new ArrayList<>();
        for (Entity e : entityMap.values()){
            if (e instanceof ZombieToast){
                list.add((ZombieToast) e);
            }
        }
        return list;
    }
    public List<ZombieType> getAllZombieType(){
        List<ZombieType> list = new ArrayList<>();
        for (Entity e : entityMap.values()){
            if (e instanceof ZombieType){
                list.add((ZombieType) e);
            }
        }
        return list;
    }

    public void initSpawnConfig(){
        //in this stage, only spider needs to be init
        Spider.setClassHealth(getSpecificConfig("spider_health"));
        Spider.setDamage(getSpecificConfig("spider_attack"));
        Spider.setSpawnRate(getSpecificConfig("spider_spawn_rate"));
        Spider.setTimeToSpawn(getSpecificConfig("spider_spawn_rate"));
    }

    public boolean isItemInList(String id){
        List<ItemResponse> list = getListItemResponse();
        for (ItemResponse i : list){
            if(i.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public InvItem getItemById(String id) {
        for (InvItem i : itemList){
            if(i.getId().equals(id)){
                return i;
            }
        }
        return null;
    }

    public boolean isItemAllowed(String id, List<String> allowedList){
        List<ItemResponse> list = getListItemResponse();
        for (ItemResponse i : list){
            if(i.getId().equals(id) && allowedList.contains(i.getType())){
                return true;
            }
        }
        return false;
    }

    /*
     * Returns the list of item id's by a specified item type
     */
    public List<String> getInvItemIdsListByType(String type){
        List<ItemResponse> list = getListItemResponse();
        return list.stream().filter(obj -> obj.getType().equals(type)).map(ItemResponse::getId).collect(Collectors.toList());
    }
    
    /*
     * Remove a specified item from inventory by it's id
     */
    public void removeInvItemById(String id){
        for (InvItem i: itemList) {
            i.getItemResponse().getId().equals(id);
            itemList.remove(i);
            break;
        }

    }

    /*
     * Returns number of inventory items of a specific type
     */
    public int getNumInvItemType(String type){
        return getInvItemIdsListByType(type).size();
    }

    /*
     * Adds a InvItem to the Item List
     */
    public void addInvItem(InvItem item){
        itemList.add(item);
    }

    public void Spawn(){
        // spawn zombie
        List<ZombieToastSpawner> zl = new ArrayList<>();
        for (Entity e : entityMap.values()){
            if (e.getType().equals("zombie_toast_spawner")) {
                ZombieToastSpawner z = (ZombieToastSpawner) e;
                zl.add(z);
            }
        }
        for (ZombieToastSpawner z : zl){
            z.spawn();
        }

        //spawn spider
        if (Spider.spawn() == true){
            spiderSpawn();
        }
    }

    public void zombieSpawn(Position p){
        this.entityCounter = this.entityCounter + 1;
        String id = Integer.toString(this.entityCounter);
        ZombieToast z = new ZombieToast(p, id);
        z.setHealth(configMap.get("zombie_health"));
        z.setDamage(configMap.get("zombie_attack"));
        z.setDungeonInfo(this);
        entityMap.put(id, z);
    }

    public void spiderSpawn() {
        this.entityCounter = this.entityCounter + 1;
        String id = Integer.toString(this.entityCounter);
        Position p = Spider.generateSpawnPos(getPlayer().getPos());
        Spider s = new Spider(p, id);
        s.setDungeonInfo(this);
        entityMap.put(id, s);
    }

    public void storeGoals(JSONObject jsonGoals) {
        this.dungeonGoal = getGoalsFromJson(jsonGoals);
    }
    
    //recursive method to initalize the goal
    public Goal getGoalsFromJson(JSONObject jsonGoals) {
        //read the json
        String superGoal = jsonGoals.getString("goal");
        //base case
        if (superGoal != "AND" && superGoal != "OR") {
            switch (superGoal){
                case "exit":
                    return new exitGoal(this);
                case "enemies":
                    return new enemiesGoal(this, getSpecificConfig("enemy_goal"));
                case "boulders":
                    return new bouldersGoal(this);
                case "treasure":
                    return new treasureGoal(this, getSpecificConfig("treasure_goal"));
            }
        //get the subgoal
        JSONArray subGoals = jsonGoals.getJSONArray("subgoals");
        //case and
        if (superGoal.equals("AND")) {
            return new andGoal(getGoalsFromJson((JSONObject) subGoals.get(0)), getGoalsFromJson((JSONObject) subGoals.get(1)));
        } else {
            return new orGoal(getGoalsFromJson((JSONObject) subGoals.get(0)), getGoalsFromJson((JSONObject) subGoals.get(1)));
        }
        
        }
        //we will never get to this.
        return null;
    }

    public String getGoalString() {
        return dungeonGoal.evalGoal();
    }
    public void addBattleResponse(BattleResponse response) {
        battleList.add(response);
    }

    public List<BattleResponse> getBattleResponses() {
        return battleList;
    }

    public void addTick(Tick tickableEntity) {
        tickList.add(tickableEntity);
    }

    public List<Tick> getTickList() {
        return tickList;
    }

    public void runTicks() {
        for (Tick tickableEntity : tickList)
            tickableEntity.tick();
    }

    public void setDungeonGoal(Goal dungeonGoal) {
        this.dungeonGoal = dungeonGoal;
    }

    public List<Entity> getSurroundingEntities(Position pos) {
        List<Entity> l = new ArrayList<>();

        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(pos.translateBy(Direction.UP));
        adjacentPositions.add(pos.translateBy(Direction.DOWN));
        adjacentPositions.add(pos.translateBy(Direction.LEFT));
        adjacentPositions.add(pos.translateBy(Direction.RIGHT));

        for (Position p : adjacentPositions) {
            List<Entity> le = getEntitiesByPosition(p);
            for (Entity e : le) {
                l.add(e);
            }
        }

        return l;
    }

    public void updateActives() {
        for (Entity e : entityMap.values()) {
            if (e instanceof FloorSwitch) {
                FloorSwitch fl = (FloorSwitch) e;
                fl.setTriggeredLastTick(fl.isTriggered());
            } else if (e instanceof Wire) {
                Wire w = (Wire) e;
                w.setIsConnectedLastTick(w.getIsConnected());
            }
        }
    }

    public void setEntityMap(HashMap<String, Entity> entityMap) {
        this.entityMap = entityMap;
    }
    
}
