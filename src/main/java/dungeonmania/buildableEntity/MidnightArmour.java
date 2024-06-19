package dungeonmania.buildableEntity;

import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.response.models.ItemResponse;

public class MidnightArmour implements InvItem, Buildable{
    private String id;
    private static String type = "midnight_armour";

    private int defenseBonus;
    private int attackBonus;
    private DungeonInfo dungeonInfo;

    public MidnightArmour() {
    }

    public MidnightArmour(String id, int defenseBonus, int attackBonus) {
        this.id = id;
        this.defenseBonus = defenseBonus;
        this.attackBonus = attackBonus;
    }

    @Override
    public void craft() throws InvalidActionException {
        List<String> swordList = dungeonInfo.getInvItemIdsListByType("sword");
        List<String> sunStoneList = dungeonInfo.getInvItemIdsListByType("sun_stone");
        int numZombie = dungeonInfo.getAllZombie().size();

        if (swordList.size() < 1 || sunStoneList.size() < 1) {
            throw new InvalidActionException("Insufficient Materials to craft Midnight Armour!");
         }

         if (numZombie > 0) {
            throw new InvalidActionException("Can't craft Midnight Armour when there is a zombie on the map!");
         }

         //remove items from inventory
         dungeonInfo.removeInvItemById(swordList.get(0));
         dungeonInfo.removeInvItemById(sunStoneList.get(0));
    }

    @Override
    public Boolean checkCraftable() {
        int numSword = dungeonInfo.getNumInvItemType("sword");
        int numSunStone = dungeonInfo.getNumInvItemType("sun_stone");
        int numZombie = dungeonInfo.getAllZombie().size();

        if ( numSword < 1 || numSunStone < 1 || numZombie > 0) {
           return false;
        }

        return true;
    }

    @Override
    public void use() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(id, type);
    }

    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {
        this.dungeonInfo = dungeonInfo;
        
    }

    @Override
    public String getId() {
        return id;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    @Override
    public String getType() {
        return type;
    }
    
}
