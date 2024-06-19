package dungeonmania.staticEntities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Wall extends staticEntity implements Serializable{
    String id;
    private Position pos;
    private String type = "wall";

    public Wall(Position p, String id) {
        this.pos = p;
        this.id = id;
    }

    public Position getPos() {
        return pos;
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
        return p;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return p;
    }
    
}
