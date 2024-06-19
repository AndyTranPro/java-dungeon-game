package dungeonmania.buildableEntity;

import java.io.Serializable;
import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.response.models.ItemResponse;

public class Bow implements InvItem, Buildable, Serializable {

    private String id;
    private static String type = "bow";

    private int durability;
    private DungeonInfo dungeonInfo;

    public Bow(){
        
    }

    public Bow(String id, int durability){
        this.id = id;
        this.durability = durability;
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void craft() throws InvalidActionException {
        List<String> woodList = dungeonInfo.getInvItemIdsListByType("wood");
        List<String> arrowList = dungeonInfo.getInvItemIdsListByType("arrow");

        if ((woodList.size()<1) || (arrowList.size()<3) ) {
            throw new InvalidActionException("Insufficient Materials to craft bow!");
        }

        //Remove one wood from inventory
        dungeonInfo.removeInvItemById(woodList.get(0));

        //Remove three arrows from inventory
        dungeonInfo.removeInvItemById(arrowList.get(0));
        dungeonInfo.removeInvItemById(arrowList.get(1));
        dungeonInfo.removeInvItemById(arrowList.get(2));
    }

    public Boolean checkCraftable(){
        int numWood = dungeonInfo.getNumInvItemType("wood");
        int numArrow = dungeonInfo.getNumInvItemType("arrow");

        if (numWood < 1 || numArrow < 3) {
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
