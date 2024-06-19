package dungeonmania.staticEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Tick;
import dungeonmania.util.Position;

public class SwitchDoor extends Door implements Tick{

    public SwitchDoor(Position position, int key, String id) {
        super(position, key, id);    
        setType("switch_door");
    }

    @Override
    public void tick() {
        List<Entity> l = dungeonInfo.getSurroundingEntities(this.pos);
        for (Entity e : l) {
            if (e instanceof FloorSwitch) {
                FloorSwitch fs = (FloorSwitch) e;
                if (fs.isTriggered()) {
                    Open();
                    return;
                }
            } 

            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                   Open();
                    return;
                }
            }
        }

        if (canClose == true) {
            Close();
        }   
    }
        
}
    
