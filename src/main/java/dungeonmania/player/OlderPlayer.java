package dungeonmania.player;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class OlderPlayer extends Entity {
    private String id;
    private String type = "older_player";
    private Position pos;

    public OlderPlayer(String id, Player player) {
        this.id = id;
        this.pos = new Position(player.position.getX(), player.position.getY());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    @Override
    public void setConfig() {

    }
    
}
