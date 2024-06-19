package dungeonmania.goal.basicGoal;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.goal.Goal;
import dungeonmania.staticEntities.Boulder;
import dungeonmania.staticEntities.FloorSwitch;

public class bouldersGoal implements Goal, Serializable{
    private DungeonInfo dungeonInfo;
    
    public bouldersGoal(DungeonInfo info) {
        this.dungeonInfo = info;
    }

    @Override
    public String evalGoal() {
        for (Entity e : dungeonInfo.getEntityMap().values()) {
            if (e instanceof FloorSwitch) {
                FloorSwitch f = (FloorSwitch) e;
                if (f.isTriggered() == false) {
                    return ":boulders";
                }
            }
        }
        return "";
    }
    
}
