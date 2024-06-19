package dungeonmania.staticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Tick;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitch extends staticEntity{
    protected Boolean hasBoulder = false;
    protected String id;
    protected Position pos;
    protected boolean isTriggeredLastTick = false;
    protected boolean isTriggered;
    protected String type = "switch";

    public FloorSwitch(Position p, String id) {
        this.id = id;
        this.pos = p;
        this.isTriggered = false;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    public void setTriggered(boolean isTriggered) {
        this.isTriggered = isTriggered;
    }

    
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    @Override
    public void setConfig() {
        
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        return this.pos;
    }

    public void setHasBoulder(Boolean hasBoulder) {
        this.hasBoulder = hasBoulder;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return this.pos;
    } 
    
    public void activateWire() {

        //activate the wire that connect to this
        //get a list of adjencent positions
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(this.pos.translateBy(Direction.UP));
        adjacentPositions.add(this.pos.translateBy(Direction.DOWN));
        adjacentPositions.add(this.pos.translateBy(Direction.LEFT));
        adjacentPositions.add(this.pos.translateBy(Direction.RIGHT));

        for (Position p : adjacentPositions) {
            List<Entity> le = dungeonInfo.getEntitiesByPosition(p);
            for (Entity e : le) {
                if (e instanceof Wire) {
                    Wire w = (Wire) e;
                    if (!w.getIsConnected()) {
                        w.activate();
                    }
                }
            }
            
        }
    }

    public void deactivateWire() {
         //deactivate the wire that connect to this
        //get a list of adjencent positions
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(this.pos.translateBy(Direction.UP));
        adjacentPositions.add(this.pos.translateBy(Direction.DOWN));
        adjacentPositions.add(this.pos.translateBy(Direction.LEFT));
        adjacentPositions.add(this.pos.translateBy(Direction.RIGHT));

        for (Position p : adjacentPositions) {
            List<Entity> le = dungeonInfo.getEntitiesByPosition(p);
            for (Entity e : le) {
                if (e instanceof Wire) {
                    Wire w = (Wire) e;
                    if (w.getIsConnected()) {
                        w.deactivate();
                    }
                }
            }
            
        }

    }

    public boolean isTriggeredLastTick() {
        return isTriggeredLastTick;
    }

    public void setTriggeredLastTick(boolean isTriggeredLastTick) {
        this.isTriggeredLastTick = isTriggeredLastTick;
    }

    
}
