package dungeonmania.staticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Wire extends staticEntity{
    private Position pos;
    private String id;
    private String type = "wire";
    private Boolean isConnected = false;
    private Boolean isConnectedLastTick = false;

    public Wire(Position p, String id) {
        this.id = id;
        this.pos = p;
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        return this.pos;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return this.pos;
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

    

    public void activate() {
        //activate this wire
        this.isConnected = true;

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

    public void deactivate() {
        this.isConnected = false;

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
                    if (w.getIsConnected()) {
                        w.deactivate();
                    }
                }
            }
            
        }

    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public Boolean getIsConnectedLastTick() {
        return isConnectedLastTick;
    }

    public void setIsConnectedLastTick(Boolean isConnectedLastTick) {
        this.isConnectedLastTick = isConnectedLastTick;
    }

    
}
