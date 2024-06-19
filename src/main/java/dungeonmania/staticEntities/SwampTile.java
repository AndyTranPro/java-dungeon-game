package dungeonmania.staticEntities;


import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTile extends staticEntity {
    private String id;
    private Position pos;
    private int movement_factor;
    private String type = "swamp_tile";

    public SwampTile(Position p, String id, int movement_factor) {
        this.id = id;
        this.pos = p;
        this.movement_factor = movement_factor;
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
    public void setConfig() {
    }

    public int getMovementFactor() {
        return movement_factor;
    }
    
}
