package dungeonmania.staticEntities;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends staticEntity{
    String id;
    private Position pos;
    private String type = "time_travelling_portal";

    public TimeTravellingPortal(Position p, String id) {
        this.pos = p;
        this.id = id;
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        DungeonInfo info = getDungeonInfo();
        info.rewind(29);
        return info.getPlayer().getPos();
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return this.pos;
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

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    @Override
    public void setConfig() {
        
    }
    
}
