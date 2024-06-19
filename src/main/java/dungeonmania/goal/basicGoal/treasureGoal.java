package dungeonmania.goal.basicGoal;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.goal.Goal;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.inventoryItem.Sunstone;
import dungeonmania.inventoryItem.Treasure;

public class treasureGoal implements Goal, Serializable{
    private DungeonInfo dungeonInfo;
    private int treasure;
    
    public treasureGoal(DungeonInfo info, int treasure) {
        this.dungeonInfo = info;
        this.treasure = treasure;
    }

    @Override
    public String evalGoal() {
        int counter = 0;
        for (InvItem e : dungeonInfo.getItemList()) {
            if (e instanceof Treasure || e instanceof Sunstone) {
                counter++;
            }
        }

        if (counter >= treasure) {
            return "";
        }
        
        return ":treasure";
    }
    
    
}
