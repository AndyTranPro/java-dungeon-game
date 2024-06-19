package dungeonmania;

import dungeonmania.buildableEntity.BuildableFactory;
import dungeonmania.buildableEntity.Sceptre;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goal.Goal;
import dungeonmania.goal.basicGoal.exitGoal;
import dungeonmania.inventoryItem.Bomb;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.inventoryItem.Treasure;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.MercenaryType;
import dungeonmania.player.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntities.Exit;
import dungeonmania.staticEntities.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;


public class DungeonManiaController {
    private String dungeonId;
    private String dungeonName;
    private int DungeonCounter = 0;
    private HashMap<String, DungeonInfo> infoMap = new HashMap<>();

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     * @throws CloneNotSupportedException
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException{
        //Create a new dungeon with unique id.
        this.dungeonName = dungeonName;
        DungeonCounter = DungeonCounter + 1;
        String dungeonId = Integer.toString(DungeonCounter);
        this.dungeonId = dungeonId;
        DungeonInfo info = new DungeonInfo();
        infoMap.put(dungeonId, info);

        String jsonContent = null;


        List<String> dungeonList = dungeons();
        List<String> configList = configs();

        if (!dungeonList.contains(dungeonName)) {
            throw new IllegalArgumentException();
        }

        if (!configList.contains(configName)) {
            throw new IllegalArgumentException();
        }

        //read and set config file
        try {
            jsonContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        //convert json into JSONObject
        JSONObject configContent = new JSONObject(jsonContent);

        //set config
        info.setConfigs(configContent);

        //read json from dungeonName

        try {
            jsonContent = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        //convert json into JSONObject
        JSONObject dungeonContent = new JSONObject(jsonContent);

        //store entities from JSONOject into dungeonInfo
        JSONArray arrEntities = dungeonContent.getJSONArray("entities");
        info.storeEntitiesInMap(arrEntities);

        //get the list of entity response
        List<EntityResponse> entityResponses = info.getListEntityResponse();

        //read and store the goals
        JSONObject jsonGoals = dungeonContent.getJSONObject("goal-condition");
        info.storeGoals(jsonGoals);
        String goalString = info.getGoalString();
        //init spawner status
        info.initSpawnConfig();
        DungeonResponse response = new DungeonResponse(dungeonId, dungeonName, entityResponses, new ArrayList<ItemResponse>(), new ArrayList<BattleResponse>(), new ArrayList<String>(), goalString);

        //store the dungeonInfo for time travel when the game start(tick 0)
        try {
            info.storeDungeonInfo();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }
    
    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        DungeonInfo info = infoMap.get(this.dungeonId);
        BuildableFactory builder = new BuildableFactory();

        List<EntityResponse> entityResponses = info.getListEntityResponse();
        List<ItemResponse> itemResponses = info.getListItemResponse();
        List<String> buildables = builder.getCurrentBuildables(info);
        String goalString = info.getGoalString();
        List<BattleResponse> battle =  info.getBattleResponses();
        
        return new DungeonResponse(dungeonId, dungeonName, entityResponses, itemResponses, battle, buildables, goalString);
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        List<String> allowedItemTypes = Arrays.asList("bomb", "invincibility_potion", "invisibility_potion");
        //check exceptions
        // if (itemUsedId != "bomb" && itemUsedId != "invincibility_potion" && itemUsedId != "invisibility_potion"){
        //     throw new IllegalArgumentException("not usable item");
        // }
        DungeonInfo info = infoMap.get(this.dungeonId);
        if (info.isItemInList(itemUsedId) == false){
            throw new InvalidActionException("not in the player's inventory");
        } else if (!info.isItemAllowed(itemUsedId, allowedItemTypes)) {
            //check exceptions
            throw new IllegalArgumentException("not usable item");
        }
        if (info.getTimeTravelCounter() > 0) {
            rewind(info.getTimeTravelledTick());
        } else {
            info.removeOlderPlayerIfExist();
        }
        
        info.updateActives();
        InvItem item = info.getItemById(itemUsedId);      
        item.use();
        info.runTicks();
        info.getPlayer().tickPlayerState();
        info.moveAllMovingEntity();
        info.Spawn();
        
        //store the dungeonInfo everytime we tick
        try {
            info.storeDungeonInfo();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return this.getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        DungeonInfo info = infoMap.get(this.dungeonId);

        if (info.getTimeTravelCounter() > 0) {
            rewind(info.getTimeTravelledTick());
        } else {
            info.removeOlderPlayerIfExist();
        }

        //trigger player movement
        info.updateActives();
        info.movePLayer(movementDirection);
        info.runTicks();
        info.getPlayer().tickPlayerState();
        info.moveAllMovingEntity();
        info.Spawn();


        //store the dungeonInfo everytime we tick
        try {
            info.storeDungeonInfo();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return this.getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        DungeonInfo info = infoMap.get(this.dungeonId);
        BuildableFactory builder = new BuildableFactory();
        
        builder.build(buildable, info);

        return this.getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        DungeonInfo info = infoMap.get(this.dungeonId);
        HashMap<String, Entity> entityMap = info.getEntityMap();
        Entity e = entityMap.get(entityId);
        if (e == null) {
            throw new IllegalArgumentException("null");
        }
        if (e.getType() != "zombie_toast_spawner" && !(e instanceof MercenaryType)) {
            throw new IllegalArgumentException();
        }

        //case zombie spawner
        if (e.getType().equals("zombie_toast_spawner")) {
            //check player position
            if (isNearSpawner(e.getPos()) == false) {
                throw new InvalidActionException(" player is not cardinally adjacent to the spawner");
            }
            //check has weapon
            if (hasWeapon() == false) {
                throw new InvalidActionException("Player dose not have a weapon");
            }
            info.getEntityMap().remove(entityId);
        }

        if(e instanceof MercenaryType){
            //Bribes MercenaryType with sceptre if available
            if (info.getNumInvItemType("sceptre")>0) {
                ((MercenaryType) e).mindControl(info);

                return this.getDungeonResponseModel();
            }

            int amount = ((MercenaryType)e).getCostToBribe();
            // check if the player is within the bribe radius
            if (((MercenaryType) e).checkBribeDistance(info.getPlayer()) == false) {
                throw new InvalidActionException("Player is not in the mercenary's range");
            }
            // check if player has enough money
            if (((MercenaryType) e).checkBribeAmoountEnough(getTreasureCount()) == false) {
                throw new InvalidActionException("Player does not have enough treasure");
            }
            ((MercenaryType) e).bribe(getTreasureCount());
            removeTreasure(amount);
        }
        
        return this.getDungeonResponseModel();
    }

    public int getTreasureCount(){
        DungeonInfo info = infoMap.get(this.dungeonId);
        int counter = 0;
        for (InvItem e : info.getItemList()) {
            if (e instanceof Treasure) {
                counter++;
            }
        }
        return counter;
    }

    public void removeTreasure(int count) {
        DungeonInfo info = infoMap.get(this.dungeonId);
        List<InvItem> treasures = new ArrayList<InvItem>();
        for (InvItem e : info.getItemList()) {
            if (e instanceof Treasure || count == 0) {
                treasures.add(e);
                count--;
            }
        }
        info.getItemList().removeAll(treasures);
    }

    public boolean isNearSpawner(Position Spawner){
        DungeonInfo info = infoMap.get(this.dungeonId);
        Position p = info.getPlayer().getPos();
        Position up = Spawner.translateBy(Direction.UP);
        Position down = Spawner.translateBy(Direction.DOWN);
        Position left = Spawner.translateBy(Direction.LEFT);
        Position right = Spawner.translateBy(Direction.RIGHT);
        if (up.equals(p) || down.equals(p) 
        || left.equals(p) || right.equals(p)){
            return true;
        }

        return false;
    }

    public boolean hasWeapon(){
        DungeonInfo info = infoMap.get(this.dungeonId);
        List<InvItem> itemList = info.getItemList();
        for (InvItem i : itemList){
            if (i.getItemResponse().getType() == "sword") {
                return true;
            }
        }
        return false;
    }

    /**
     * /game/save
     * 
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        DungeonInfo info = infoMap.get(this.dungeonId);
        try {
            String path = Paths.get("").toAbsolutePath().toString();
            System.out.println("Working Directory = " + path);
            FileOutputStream fs = new FileOutputStream("src/main/resources/savedGames/" + name);
            ObjectOutputStream os;
            try {
                os = new ObjectOutputStream(fs);
                os.writeObject(info);
                os.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }         
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getDungeonResponseModel(); 
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        List<String> l = allGames();
        if (l.contains(name) == false) {
            throw new IllegalArgumentException("not a valid game name");
        }
        DungeonCounter = DungeonCounter + 1;
        this.dungeonId = Integer.toString(DungeonCounter);
        this.dungeonName = name;
        
        try {
            FileInputStream fi = new FileInputStream("src/main/resources/savedGames/" + name);
            ObjectInputStream os;
            try {
                os = new ObjectInputStream(fi);
                try {
                    DungeonInfo info = (DungeonInfo) os.readObject();
                    infoMap.put(this.dungeonId, info);
                    os.close();
                    fi.close();
                    return getDungeonResponseModel();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        //return FileLoader.listFileNamesInResourceDirectory("savedGames");
        File folder = new File("src/main/resources/savedGames");
        File[] listOfFiles = folder.listFiles();
        List<String> l = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            l.add(listOfFiles[i].getName());
        }

        return l;
    }

    
    public DungeonResponse generateDungeon (int xStart, int yStart, int xEnd, int yEnd, String configName) {
        DungeonGenerator generator = new DungeonGenerator(new Position (xStart, yStart), new Position(xEnd, yEnd));
        DungeonCounter = DungeonCounter + 1;
        DungeonInfo info = new DungeonInfo();
        String jsonContent = null;
        //read and set config file
        try {
            jsonContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        //convert json into JSONObject
        JSONObject configContent = new JSONObject(jsonContent);

        // get entities list
        List <EntityResponse> entities = generator.generate();

        // set entities in info
        for (EntityResponse entity : entities) {
            Entity new_entity;
            switch (entity.getType()) {
                case "wall" :
                    new_entity = new Wall(entity.getPosition(), entity.getId());
                    new_entity.setDungeonInfo(info);
                    info.getEntityMap().put(entity.getId(), new_entity);
                    break;
                case "player":
                    new_entity = new Player(entity.getPosition(), entity.getId());
                    new_entity.setDungeonInfo(info);
                    info.getEntityMap().put(entity.getId(), new_entity);
                    break;
                case "exit":
                    new_entity = new Exit(entity.getPosition(), entity.getId());
                    new_entity.setDungeonInfo(info);
                    info.getEntityMap().put(entity.getId(), new_entity);
                    break;
            }
        }
        // set config
        info.setConfigs(configContent);
        // set goal
        Goal goal = new exitGoal(info);
        info.setDungeonGoal(goal);
        // store info in controller
        this.dungeonId = String.valueOf(DungeonCounter);
        infoMap.put(dungeonId, info);
        DungeonResponse response = new DungeonResponse(dungeonId, "Generated_Dungeon", entities, new ArrayList<ItemResponse>(), new ArrayList<BattleResponse>(), new ArrayList<String>(), ":exit");
        return response;
    }

    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        DungeonInfo info = infoMap.get(this.dungeonId);
        if (ticks <= 0 || info.getDungeonInfoHistorySize() < ticks) {
            throw new IllegalArgumentException("tick can not be less than or equal to 0");
        }
        info.rewind(ticks);
        return getDungeonResponseModel();
    }
}
