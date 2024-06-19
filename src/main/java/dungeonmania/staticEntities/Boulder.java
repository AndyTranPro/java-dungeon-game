package dungeonmania.staticEntities;

import java.io.Serializable;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends staticEntity{
    private String id;
    private Position position;
    private String type = "boulder";
    private FloorSwitch fs; //the current switch under this boulder

    public Boulder(Position position, String id) {
        this.position = position;
        this.id = id;
        this.fs = null;
    }

    public Position getPos() {
        return position;
    }

    public void setPos(Position p){
        //TODO: Floor switch check before and after set
        this.position = p;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, position, false);
        return response;
    }

    @Override
    public void setConfig() {
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        //check if the boulder can move in direction
        if (this.isAbleToMove(d) == false) {
            return p;
        } else {
            Position player = this.position;
            //before player moves in, move the boulder and check if can trigger the floor swtich
            this.position = this.position.translateBy(d);
            if (this.fs != null){
                fs.setHasBoulder(false);
                fs.setTriggered(false);
                fs.deactivateWire();
                this.fs = null;
            }
            List<Entity> checkEntity = dungeonInfo.getEntitiesByPosition(this.position);
            for (Entity e : checkEntity){
                if (e instanceof FloorSwitch){
                    FloorSwitch f = (FloorSwitch) e;
                    f.setTriggered(true);
                    this.fs = f;
                    fs.setHasBoulder(true);
                    fs.activateWire();
                }
            }

            return player;
        }

    }
    
    public boolean isAbleToMove(Direction d){
        Position temp = null;
        //init a checkPos to check what's in the position
        Position checkPos = this.position.translateBy(d);
        List<Entity> checkEntity = dungeonInfo.getEntitiesByPosition(checkPos);
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                temp = se.boulderMoveIn(this.position);
            }

            if (this.position.equals(temp)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return p;
    }
}
