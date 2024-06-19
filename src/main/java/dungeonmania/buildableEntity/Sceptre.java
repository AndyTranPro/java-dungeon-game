package dungeonmania.buildableEntity;

import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.response.models.ItemResponse;

public class Sceptre implements InvItem, Buildable{
    private String id;
    private static String type = "sceptre";

    private int duration;
    private DungeonInfo dungeonInfo;

    public Sceptre() {
    }

    public Sceptre(String id, int duration) {
        this.id = id;
        this.duration = duration;
    }

    @Override
    public void craft() throws InvalidActionException {
        List<String> woodList = dungeonInfo.getInvItemIdsListByType("wood");
        List<String> arrowList = dungeonInfo.getInvItemIdsListByType("arrow");
        List<String> keyList = dungeonInfo.getInvItemIdsListByType("key");
        List<String> treasureList = dungeonInfo.getInvItemIdsListByType("treasure");
        List<String> sunStoneList = dungeonInfo.getInvItemIdsListByType("sun_stone");

        if ((woodList.size() < 1 && arrowList.size() < 2) || sunStoneList.size() < 1) {
            throw new InvalidActionException("Insufficient Materials to craft Sceptre!");
         }

        if (keyList.size()<1 && treasureList.size()<1 && sunStoneList.size()<2) {
            throw new InvalidActionException("Insufficient Materials to craft Sceptre!");
        }
        
        //Remove Wood or Arrow from Inventory (Give priority to Wood then Arrow)
        if (woodList.size() > 0) {
            dungeonInfo.removeInvItemById(woodList.get(0));
        } else {
            dungeonInfo.removeInvItemById(arrowList.get(0));
            dungeonInfo.removeInvItemById(arrowList.get(1));
        }

        //Remove Key or Treasure from Inventory (Give priority to Key then Treasure then Sun Stone)
        if (keyList.size() > 0) {
            dungeonInfo.removeInvItemById(keyList.get(0));
        } else if (treasureList.size() > 0) {
            dungeonInfo.removeInvItemById(treasureList.get(0));
        } else {
            dungeonInfo.removeInvItemById(sunStoneList.get(1));
            return;
        }
         
        dungeonInfo.removeInvItemById(sunStoneList.get(0));
    }

    @Override
    public Boolean checkCraftable() {
        int numWood = dungeonInfo.getNumInvItemType("wood");
        int numArrow = dungeonInfo.getNumInvItemType("arrow");
        int numKey = dungeonInfo.getNumInvItemType("key");
        int numTreasure = dungeonInfo.getNumInvItemType("treasure");
        int numSunStone = dungeonInfo.getNumInvItemType("sun_stone");

        if ((numWood < 1 && numArrow < 2) || numSunStone < 1) {
            return false;
        }

        if (numKey<1 && numTreasure<1 && numSunStone<2) {
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

	public Integer getDuration() {
		return duration;
	}

    @Override
    public String getType() {
        return type;
    }

}
