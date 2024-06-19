package dungeonmania.staticEntities;

import java.io.Serializable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Exit extends staticEntity implements Serializable{
    private Position pos;
    private String id;
    private String type = "exit";

    public Exit(Position position, String id) {
        this.pos = position;
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
        return this.pos;
    }

    @Override
    public Position boulderMoveIn(Position p) {

        return this.pos;
    }
}
