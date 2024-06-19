package dungeonmania.staticEntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Logic implements Serializable{
    DungeonInfo info;
    String logic;
    Position pos;

    public Logic(DungeonInfo info, Position pos, String logic) {
        this.info = info;
        this.logic = logic;
        this.pos = pos;
    }

    public Boolean logicProcess() {
        if (logic.equals("and")) {
            return and();
        } else if (logic.equals("or")) {
            return or();
        } else if (logic.equals("xor")) {
            return xor();
        } else {
            return coand();
        }
    }

    //and logic
    public Boolean and() {
        List<Entity> le = info.getSurroundingEntities(pos);
        
        List<Entity> activeEntities = getActivatableEntities(le);
        if (activeEntities.size() < 2) {
            return false;
        } else if (activeEntities.size() == getNumActiveEntity(activeEntities)){
            return true;
        } else {
            return false;
        }
    }

    //or logic
    public Boolean or() {
        List<Entity> le = info.getSurroundingEntities(pos);
        
        List<Entity> activeEntities = getActivatableEntities(le);
        if (getNumActiveEntity(activeEntities) > 0) {
            return true;
        }

        return false;
    }

    //xor logic
    public Boolean xor() {
        List<Entity> le = info.getSurroundingEntities(pos);
        
        List<Entity> activeEntities = getActivatableEntities(le);
        if (getNumActiveEntity(activeEntities) == 1) {
            return true;
        }

        return false;
    }

    //Co And logic
    public Boolean coand() {
        List<Entity> le = info.getSurroundingEntities(pos);

        List<Entity> activeEntities = getActivatableEntities(le);
        if (activeEntities.size() < 2) {
            return false;
        } else if (activeEntities.size() == getNumActiveEntity(activeEntities)
                    && getNumactiveLastTick(activeEntities) == 0){
            return true;
        }

        return false;
    }

    public int getNumactiveLastTick(List<Entity> le) {
        //set a counter
        int counter = 0;

        //loop through the list
        for (Entity e : le) {
            if (e instanceof FloorSwitch) {
                FloorSwitch fs = (FloorSwitch) e;
                if (fs.isTriggeredLastTick()) {
                    counter = counter + 1;
                }
            } else {
                Wire w = (Wire) e;
                if (w.getIsConnectedLastTick()) {
                    counter = counter + 1;
                }
            }
        }

        return counter;
    }
    


    //given a list of entity, find all the entities that are able to be activated(switch, wire)
    public List<Entity> getActivatableEntities(List<Entity> le) {
        List<Entity> re = new ArrayList<>();
        for (Entity e : le) {
            if(e instanceof FloorSwitch || e instanceof Wire) {
                re.add(e);
            }
        }

        return re;
    }

    //get how many entities are active states in a given entity list.
    public int getNumActiveEntity(List<Entity> le){
        //set a counter
        int counter = 0;

        //loop through the list
        for (Entity e : le) {
            if (e instanceof FloorSwitch) {
                FloorSwitch fs = (FloorSwitch) e;
                if (fs.isTriggered()) {
                    counter = counter + 1;
                }
            } else {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    counter = counter + 1;
                }
            }
        }

        return counter;
    }
}
