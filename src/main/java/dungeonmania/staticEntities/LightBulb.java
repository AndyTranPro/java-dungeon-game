package dungeonmania.staticEntities;

import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.Tick;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LightBulb extends staticEntity implements Tick{
    protected Position pos;
    protected String id;
    protected String type = "light_bulb_off";

    public LightBulb(Position p, String id) {
        this.id = id;
        this.pos = p;
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        return p;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return p;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Position getPos() {
        return this.pos;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    @Override
    public void setConfig() {}
    
    public void turnOn() {
        this.type = "light_bulb_on";
    }
    
    public void turnOff() {
        this.type = "light_bulb_off";
    }

    @Override
    public void tick() {
        List<Entity> l = dungeonInfo.getSurroundingEntities(pos);
        for (Entity e : l) {
            if (e instanceof FloorSwitch) {
                FloorSwitch fs = (FloorSwitch) e;
                if (fs.isTriggered()) {
                    turnOn();
                    return;
                }
            } 

            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    turnOn();
                    return;
                }
            }
        }

        turnOff();      
    }
}
