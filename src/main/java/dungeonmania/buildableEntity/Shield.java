package dungeonmania.buildableEntity;

import java.io.Serializable;
import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.response.models.ItemResponse;

public class Shield implements InvItem,Buildable, Serializable {
    private String id;
    private static String type = "shield";

    private int defenseBonus;
    private int durability;
    private DungeonInfo dungeonInfo;

    public Shield(){

    }

    public Shield(String id, int defenseBonus, int durability){
        this.id = id;
        this.defenseBonus = defenseBonus;
        this.durability = durability;
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void craft() throws InvalidActionException {
        List<String> woodList = dungeonInfo.getInvItemIdsListByType("wood");
        List<String> treasureList = dungeonInfo.getInvItemIdsListByType("treasure");
        List<String> keyList = dungeonInfo.getInvItemIdsListByType("key");
        List<String> sunStoneList = dungeonInfo.getInvItemIdsListByType("sun_stone");
         

        if ((woodList.size()<2) || ((treasureList.size()<1) && (keyList.size()<1) && sunStoneList.size()<1) ) {
            throw new InvalidActionException("Insufficient Materials to craft Shield!");
        }

        //Remove two wood from inventory
        dungeonInfo.removeInvItemById(woodList.get(0));
        dungeonInfo.removeInvItemById(woodList.get(1));

        //Remove Treasure or Key from Inventory (Give priority to Treasure then Key then Sun Stone)
        if (treasureList.size() > 0) {
            dungeonInfo.removeInvItemById(treasureList.get(0));
            return;
        }
        
        if (keyList.size() > 0) {
            dungeonInfo.removeInvItemById(keyList.get(0));
            return;
        }
    }

    public Boolean checkCraftable(){
        int numWood = dungeonInfo.getNumInvItemType("wood");
        int numTreasure = dungeonInfo.getNumInvItemType("treasure");
        int numKey = dungeonInfo.getNumInvItemType("key");
        int numSunStone = dungeonInfo.getNumInvItemType("sun_stone");

        if ( numWood <2 || (numTreasure<1 && numKey<1 && numSunStone<1)) {
           return false;
        }

        return true;
    }

    public Boolean isItemDestroyed() {
        if (durability > 0) {
            return false;
        }

        return true;
    }
    
    @Override
    public void use() {
        durability--;
        if (isItemDestroyed()) 
            dungeonInfo.removeInvItemById(id);
    }

    @Override
    public ItemResponse getItemResponse(){
        return new ItemResponse(this.id, type);
    }

    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {
        this.dungeonInfo = dungeonInfo;
    }

}
